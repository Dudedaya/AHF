package xyz.dudedaya.myfriends;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoginFragment.AuthClickListener, AuthFragment.AuthListener {

    private SharedPreferences sharedPreferences;
    private String token;
    private MenuItem logoutMenuItem;
    private ProgressBar progressBar;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initializing views
        progressBar = findViewById(R.id.progress_bar);

        // Load token data, check it and update UI accordingly
        sharedPreferences = getSharedPreferences("tokenData", Context.MODE_PRIVATE);
        if (savedInstanceState == null) {
            setUI(checkToken());
        } else {
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
    }

    private boolean checkToken() {
        long currentTime = System.currentTimeMillis();
        token = sharedPreferences.getString("token", "");
        long expirationDate = sharedPreferences.getLong("expirationDate", currentTime);
        // Return false if we don't have a token or the token is expired
        return (token.length() != 0 && expirationDate >= currentTime);
    }

    private void setUI(boolean isTokenOk) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (isTokenOk) {
            // Set Friends fragment
            FriendsFragment friendsFragment = new FriendsFragment();
            fragment = friendsFragment;
            ft.add(R.id.fragment_container, friendsFragment);
        } else {
            // Set Authorize fragment
            LoginFragment loginFragment = new LoginFragment();
            fragment = loginFragment;
            ft.add(R.id.fragment_container, loginFragment);
        }
        ft.commit();
    }


    public void buttonClick(View view) {
        // todo: remove
        logoutMenuItem.setVisible(!logoutMenuItem.isVisible());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        logoutMenuItem = menu.findItem(R.id.action_logout);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void authButtonClicked() {
        showProgressBar();
        fragment = new AuthFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void authComplete(String url) {
        fragment = new FriendsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void authError() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator), R.string.auth_error, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snackbar_retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authButtonClicked();
            }
        });
        snackbar.show();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment != null && fragment.isAdded()) {
            ft.remove(fragment);
            ft.commit();
            getSupportFragmentManager().popBackStack();
        } else {
            fragment = new LoginFragment();
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
        }
    }

    @Override
    public void authLoaded() {
        hideProgressBar();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void setFriendsFragment() {
        fragment = new FriendsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "fragment", fragment);
        }
    }

    public class RetriveDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            showProgressBar();
        }

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder response = new StringBuilder();
            try {
                URL request = new URL("https://api.vk.com/method/users.get?fields=nickname,photo_100&v=5.89&token=" + token);
                HttpURLConnection urlConnection = (HttpURLConnection) request.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    response.append(stringBuilder);
                } finally {
                    urlConnection.disconnect();
                }
                request = new URL("https://api.vk.com/method/friends.get?fields=nickname,photo_100,status&order=random&count=5&v=5.8&token=" + token);
                urlConnection = (HttpURLConnection) request.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    response.append(stringBuilder);
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response.length() > 0 ? response.toString() : null;
        }

        @Override
        protected void onPostExecute(String s) {
            hideProgressBar();
            if (s == null) {
                // todo: error
            } else {
                setFriendsFragment();
            }
        }
    }
}

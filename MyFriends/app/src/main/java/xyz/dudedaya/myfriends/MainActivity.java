package xyz.dudedaya.myfriends;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoginFragment.AuthClickListener, AuthFragment.AuthListener, DataHelper.DataEventListener {

    private SharedPreferences sharedPreferences;
    private String token;
    private MenuItem logoutMenuItem;
    private ProgressBar progressBar;
    private Fragment fragment;
    private AppBarLayout appBarLayout;
    private DataHelper dataHelper;
    private LinearLayout appbarLinearLayout;
    private ImageView userImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initializing views
        progressBar = findViewById(R.id.progress_bar);
        appBarLayout = findViewById(R.id.appbar);
        appbarLinearLayout = findViewById(R.id.appbar_layout);

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
        Log.d("checkToken", token);
        long expirationDate = sharedPreferences.getLong("expirationDate", currentTime);
        // Return false if we don't have a token or the token is expired
        return (token.length() != 0 && expirationDate >= currentTime);
    }

    private void saveToken() {
        long currentTime = System.currentTimeMillis() + 86400 * 1000;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.putLong("expirationDate", currentTime);
        editor.apply();
    }

    private void resetToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    private void setUI(boolean isTokenOk) {
        if (isTokenOk) {
            retrieveData();
        } else {
            // Set Login fragment
            appBarLayout.setExpanded(false, false);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            LoginFragment loginFragment = new LoginFragment();
            fragment = loginFragment;
            ft.add(R.id.fragment_container, loginFragment);
            ft.commit();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "fragment", fragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        logoutMenuItem = menu.findItem(R.id.action_logout);
        logoutMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                resetToken();
                logoutMenuItem.setVisible(false);
                logout();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void logout() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment = new LoginFragment();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
        appBarLayout.setExpanded(false, true);
        appbarLinearLayout.setVisibility(View.GONE);
        dataHelper = null;
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
        token = url.split("access_token=")[1].split("&")[0];
        if (token.length() > 0) {
            saveToken();
            retrieveData();
        } else showErrorSnackbar();
    }

    @Override
    public void authError() {
        showErrorSnackbar();
    }

    @Override
    public void authLoaded() {
        hideProgressBar();
    }

    @Override
    public void dataLoaded() {
        Log.d("dataLoaded", "dataLoaded");
        setFriendsFragment();
        showLogoutButton();
    }

    @Override
    public void errorLoadingData() {
        showErrorSnackbar();
    }

    private void retrieveData() {
        Log.d("retrieveData", "retrieveData");
        dataHelper = DataHelper.initInstance(token);
        dataHelper.setListener(this);
        dataHelper.loadData();
    }

    private void showErrorSnackbar() {
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

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void showLogoutButton() {
        Log.d("showLogoutButton", "logoutShow");
        logoutMenuItem.setVisible(true);
    }

    private void setFriendsFragment() {
        fragment = new FriendsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        appBarLayout.setExpanded(true, true);
        userImage = findViewById(R.id.user_image);
        DataHelper.User user = dataHelper.getUser();
        if (user.getPhoto_100() != null) {
            new RetrievePhotoTask().execute(user.getPhoto_100());
        }
        TextView textView = findViewById(R.id.user_name);
        textView.setText(user.getName());
        appbarLinearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        if (dataHelper != null) {
            dataHelper.removeListener(this);
            dataHelper = null;
        }
        super.onDestroy();
    }

    private class RetrievePhotoTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlString = urls[0];
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    return BitmapFactory.decodeStream(urlConnection.getInputStream());
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.d("MA/RetrievePhotoTask","Error loading photo.");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            userImage.setImageBitmap(bitmap);
        }
    }
}

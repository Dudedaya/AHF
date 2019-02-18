package xyz.dudedaya.myfriends;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements LoginFragment.AuthClickListener, AuthFragment.AuthListener, DataHelper.DataEventListener {

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
        Log.d("checkToken",token);
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
            // Set Authorize fragment
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
                setUI(false);
                return true;
            }
        });
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
        DataHelper dh = new DataHelper(token, this);
        dh.loadData();
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
    }


}

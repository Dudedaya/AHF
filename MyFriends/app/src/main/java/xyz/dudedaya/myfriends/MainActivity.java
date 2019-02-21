package xyz.dudedaya.myfriends;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    private ProgressBar progressBar;
    private AppBarLayout appBarLayout;
    private DataHelper dataHelper;
    private LinearLayout appbarLinearLayout;
    private ImageView userImage;
    private FloatingActionButton fab;


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
        fab = findViewById(R.id.fab);
        configureFab();


        // Load token data, check it and update UI accordingly
        sharedPreferences = getSharedPreferences("tokenData", Context.MODE_PRIVATE);
        setUI(checkToken());
    }

    private void configureFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetToken();
                logout();
                v.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setUI(checkToken());
    }

    private boolean checkToken() {
        long currentTime = System.currentTimeMillis();
        token = sharedPreferences.getString("token", "");
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
            ft.replace(R.id.fragment_container, new LoginFragment());
            ft.commitAllowingStateLoss();
        }
    }


    private void logout() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new LoginFragment());
        ft.commit();
        appBarLayout.setExpanded(false, true);
        appbarLinearLayout.setVisibility(View.GONE);
        dataHelper = null;
    }


    @Override
    public void authButtonClicked() {
        showProgressBar();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new AuthFragment());
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

    private void retrieveData() {
        dataHelper = DataHelper.initInstance(token);
        dataHelper.setListener(this);
        dataHelper.loadData();
    }

    @Override
    public void dataLoaded() {
        setFriendsFragment();
        showLogoutButton();
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
    public void errorLoadingData() {
        showErrorSnackbar();
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
        ft.replace(R.id.fragment_container, new LoginFragment());
        ft.commit();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void showLogoutButton() {
        ((View) fab).setVisibility(View.VISIBLE);
    }

    private void setFriendsFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new FriendsFragment());
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

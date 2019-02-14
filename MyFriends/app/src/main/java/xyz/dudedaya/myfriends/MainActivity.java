package xyz.dudedaya.myfriends;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private Button button;
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private String token;
    private long expirationDate;
    private MenuItem logoutMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initializing views
        progressBar = findViewById(R.id.progress_bar);
        button = findViewById(R.id.auth_button);
        recyclerView = findViewById(R.id.recycler_view);

        // Load token data, check it and update UI accordingly
        sharedPreferences = getSharedPreferences("tokenData", Context.MODE_PRIVATE);
        updateUI(checkToken());


    }

    private boolean checkToken() {
        long currentTime = System.currentTimeMillis();
        token = sharedPreferences.getString("token", "");
        expirationDate = sharedPreferences.getLong("expirationDate", currentTime);
        // Return false if we don't have a token or the token is expired
        return (token.length() != 0 && expirationDate >= currentTime);
    }

    private void updateUI(boolean isTokenOk) {
        if (isTokenOk) {
            // Load friends here
            getFriends();
        } else {
            // Authorize
            webView = findViewById(R.id.web_view);
            webView.setVerticalScrollBarEnabled(false);
            webView.setHorizontalScrollBarEnabled(false);
            // Adding ProgressBar while loading page
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this,"Не удалось авторизоваться ВКонтакте", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getFriends() {

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


}

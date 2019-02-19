package xyz.dudedaya.myfriends;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * A simple {@link Fragment} subclass.
 */
public class AuthFragment extends Fragment {

    public interface AuthListener {
        void authComplete(String url);

        void authLoaded();

        void authError();
    }

    private AuthListener authListener;


    public AuthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        this.authListener = (AuthListener) context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_auth, container, false);

        WebView webView = layout.findViewById(R.id.web_view);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.clearCache(true);
        webView.clearHistory();
        clearCookies();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("AuthFragment", "page started: " + url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("AuthFragment", "page finished: " + url);
                authListener.authLoaded();
                if (url.contains("error")) {
                    authListener.authError();
                } else if (url.startsWith("https://oauth.vk.com/blank.html")) {
                    authListener.authComplete(url);
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.d("AuthFragment", "error");
                authListener.authError();
                super.onReceivedError(view, request, error);
            }
        });
        String authUrl = "https://oauth.vk.com/authorize?client_id=6864969&display=mobile" +
                "&redirect_uri=https://oauth.vk.com/blank.html&scope=friends&response_type=token&v=5.92&state=123456";
        webView.loadUrl(authUrl);

        return layout;
    }

    private void clearCookies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(getActivity());
            cookieSyncManager.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncManager.stopSync();
            cookieSyncManager.sync();
        }
    }

}

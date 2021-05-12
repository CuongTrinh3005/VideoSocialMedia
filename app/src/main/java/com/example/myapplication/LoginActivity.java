package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static String cookies;//Truy cap cookies o bat ki dau
    public static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19";
    public static String googleId="";
    private WebView webView;
    private LoginActivity self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.web_view);
        self = this;

        webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setUserAgentString(USER_AGENT);
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("https://video-vds.herokuapp.com/comment/auth/google");
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //True if the host application wants to leave the current WebView and handle the url itself, otherwise return false.
                webView.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                cookies = CookieManager.getInstance().getCookie(url);//Lay cookie
                Log.d("cookies", cookies);

                if (!url.equals("https://video-vds.herokuapp.com/comment/auth/google/success"))
                    return;

                //Move into new activity here >
                webView.evaluateJavascript("(function(){return document.getElementsByTagName('pre')[0].innerHTML})();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String html) {
                                googleId = html;
                                Log.d("GoogleID", googleId);
                            }
                        });
                // Already subscribed
                startActivity(new Intent(LoginActivity.this, MenuActivity.class));
//                startActivity(new Intent(LoginActivity.this, HomeActivity.class));// Intent params ( current activity, next activity)
                // If have not subscribed yet
//                String videoId = "jqZR9GiGjBWdvEoQ";
//                String channelId = "suSMSjAvbBfDGomh";
//                Intent intent = new Intent(getApplicationContext(), DetailedVideoActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("videoId", videoId);
//                bundle.putString("channelId", channelId);
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        });
    }
}

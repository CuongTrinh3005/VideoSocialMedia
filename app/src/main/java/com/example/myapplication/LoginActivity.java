package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static String cookies;//Truy cap cookies o bat ki dau
    public static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19";
    private WebView webView;
    private LoginActivity self;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.web_view);
        self=this;

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


                if(!url.equals("https://video-vds.herokuapp.com/comment/auth/google/success")) return;

                //Move into new activity here >
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));// Intent params ( current activity, next activity)





                ////////////////////////////////
                //Sample post request
                Log.d("Error","Sending request");
                String path = "https://video-vds.herokuapp.com/comment";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, path, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Error", "onResponse: " + response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if(error.networkResponse.data!=null) {
                            try {
                                Log.e("Error", "onErrorResponse: "+new String(error.networkResponse.data,"UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
//                    params.put("para1", "value1");
//                    params.put("para1", "value2");
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("Cookie", LoginActivity.cookies);

                        return params;
                    }
//                    @Override
//                    public byte[] getBody() throws AuthFailureError {
//                        String httpPostBody="grant_type=password&username=Alice&password=password123";
//                        // usually you'd have a field with some values you'd want to escape, you need to do it yourself if overriding getBody. here's how you do it
//                        try {
//                            httpPostBody=httpPostBody+"&randomFieldFilledWithAwkwardCharacters="+URLEncoder.encode("{{%stuffToBe Escaped/","UTF-8");
//                        } catch (UnsupportedEncodingException exception) {
//                            Log.e("ERROR", "exception", exception);
//                            // return null and don't pass any POST string if you encounter encoding error
//                            return null;
//                        }
//                        return httpPostBody.getBytes();
//                    }
                };
                RequestQueue queue = Volley.newRequestQueue(self);
                queue.add(stringRequest);
            }

        });
    }
}

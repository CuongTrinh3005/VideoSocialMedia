package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapters.CommentsAdapter;
import com.example.myapplication.Entities.Comment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FullCommentsActivity extends AppCompatActivity {
    ImageButton ibtnBack;
    ListView listViewFullComments;
    ArrayList<Comment> commentList = new ArrayList<>();
    CommentsAdapter commentsAdapter;
    String videoId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_comments);

        Bundle bundle = getIntent().getExtras();
        videoId = bundle.getString("videoId");
        Toast.makeText(getApplicationContext(), videoId, Toast.LENGTH_SHORT).show();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setControl();
        initListComments();
        setEvent();
    }

    private void setControl(){
        ibtnBack = findViewById(R.id.btnBack);
        listViewFullComments = findViewById(R.id.listFullComments);
    }

    private void setEvent(){
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        commentsAdapter = new CommentsAdapter(this, commentList, R.layout.list_comments);
        listViewFullComments.setAdapter(commentsAdapter);
        listViewFullComments.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initListComments();
                String[] likes = commentList.get(position).getLike();
                if(likes != null)  {
                    int numLike = likes.length;
                    if(numLike == 1)
                        Toast.makeText(getApplicationContext(), numLike + " like" , Toast.LENGTH_LONG).show();
                    else  Toast.makeText(getApplicationContext(), numLike + " likes" , Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(getApplicationContext(), "Nobody likes" , Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initListComments(){
        commentList = DetailedVideoActivity.getAllComments(videoId);
    }

    public boolean postService(String url, HashMap inputParams){
        try{
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("response message", response);
                    Toast.makeText(FullCommentsActivity.this,response,Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    if (error.networkResponse.data != null) {
                        try {
                            Log.e("Error", "onErrorResponse: " + new String(error.networkResponse.data, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    if(inputParams == null){
                        HashMap<String, String> params = new HashMap<>();
                        return params;
                    }
                    else
                        return inputParams;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Cookie", LoginActivity.cookies);

                    return params;
                }};
            queue.add(request);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteService(String url){
        try{
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("response message", response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    if (error.networkResponse.data != null) {
                        try {
                            Log.e("Error", "onErrorResponse: " + new String(error.networkResponse.data, "UTF-8"));
                            throw new RuntimeException("Runtime error");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Cookie", LoginActivity.cookies);

                    return params;
                }};
            queue.add(request);
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
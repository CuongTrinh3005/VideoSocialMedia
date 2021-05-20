package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.myapplication.Adapters.TopUsersAdapter;
import com.example.myapplication.Entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class StatisticInChannelActivity extends AppCompatActivity {
    ListView lvTopUsers;
    public static int totalLikes = 0;
    ArrayList<User> listTopUsers = new ArrayList<>();
    TopUsersAdapter topUsersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_in_channel);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // These lines of code are used with the purpose of avoiding asynchronous thread exception
        // Also avoid os.NetworkOnMainThreadException
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setControl();
        Bundle bundle = getIntent().getExtras();
        String channelId = bundle.getString("channelId");
        String endpoint = String.format("https://video-vds.herokuapp.com/channel/%s/top", channelId);
        listTopUsers = getTopUser(endpoint);
        setEvent();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void setControl(){
        lvTopUsers = findViewById(R.id.listTopUsers);
    }

    private void setEvent(){
        topUsersAdapter = new TopUsersAdapter(StatisticInChannelActivity.this, listTopUsers, R.layout.list_top_users);
        lvTopUsers.setAdapter(topUsersAdapter);
    }

    private ArrayList<User> getTopUser(String url){
        String infoJson = DetailedVideoActivity.getService(url);
        ArrayList<User> list = new ArrayList<>();
        // Parse json to user object
        try {
            JSONArray jsonArray = new JSONArray(infoJson);
            for(int index=0; index<jsonArray.length();index++){
                JSONObject userJsonObj = (JSONObject) jsonArray.get(index);
                if(userJsonObj != null) {
                    String _id = userJsonObj.getString("_id");
                    String email = userJsonObj.getString("email");
                    String name = userJsonObj.getString("name");
                    String image = userJsonObj.getString("image");
                    totalLikes = userJsonObj.getInt("totalLike");
                    User user = new User(_id, email, name, image);
                    list.add(user);
                }
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return list;
    }
}
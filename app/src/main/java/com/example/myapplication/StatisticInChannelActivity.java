package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.myapplication.Adapters.TopUsersAdapter;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Entities.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

public class StatisticInChannelActivity extends AppCompatActivity {
    ListView lvTopUsers;
    VideoView videoView;
    TextView tvVideoInfo;
    public static int totalLikes = 0;
    ArrayList<User> listTopUsers = new ArrayList<>();
    TopUsersAdapter topUsersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_in_channel);
        this.getSupportActionBar().setTitle("Channel Feature");
        this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#343d46")));
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
        // Get video feature
        Video topVideo = getTopVideo(channelId);
        renderTopVideo(topVideo);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void setControl(){
        videoView = findViewById(R.id.videoTopChannel);
        tvVideoInfo = findViewById(R.id.topVideoInfo);
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

    private Video getTopVideo(String channelID){
        for(Video v : MenuActivity.listVideosTrending){
            if(v.getChannelId().equals(channelID))
                return v;
        }
        return null;
    }

    private void renderTopVideo(Video video){
        if(video == null)   return;

        String videoPath = video.getVideoPaths()[0];
        String videoAsset = "https://video-vds.herokuapp.com" + videoPath;
        // Creating media controller
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        // Get video from resource
        Uri resource = Uri.parse(videoAsset);

        // Setting for starting video
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(resource);
        videoView.requestFocus();
        videoView.start();

        // Render Info
        String info = decodeUTF8(video.getTitle());
        info += "\nDescription: " + decodeUTF8(video.getDescription());
        info += "\n Views: " + video.getView();
        String[] likes = video.getLikes();
        if(likes != null){
            int numLike = likes.length;
            if(numLike>1){
                numLike = likes.length;
                info += "\t- " + numLike + " likes";
            }
            else info += "\t- " + numLike + " like";
        }
        tvVideoInfo.setText(info);
    }

    public String decodeUTF8(String str){
        try {
            // Avoid font error when displaying
            str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Html.fromHtml(str).toString();
    }
}

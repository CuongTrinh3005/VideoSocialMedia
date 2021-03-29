package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.brightcove.player.model.DeliveryType;
import com.brightcove.player.model.Video;
import com.brightcove.player.view.BrightcoveExoPlayerVideoView;

import java.net.URISyntaxException;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.home);

        BrightcoveExoPlayerVideoView brightcoveVideoView = (BrightcoveExoPlayerVideoView) findViewById(R.id.brightcove_video_view);

        Video video = Video.createVideo("https://video-vds.herokuapp.com/videoAsset/1616634228179/720p/prog.m3u8",
                DeliveryType.HLS);
        // Load a remote poster image
        try {
            java.net.URI myposterImage = new java.net.URI("https://video-vds.herokuapp.com/imageAsset/1616588389523.jpg");
            video.getProperties().put(Video.Fields.STILL_IMAGE_URI, myposterImage);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // Add video to the view
        brightcoveVideoView.add(video);
        // Start video playback
        brightcoveVideoView.start();


    }
}

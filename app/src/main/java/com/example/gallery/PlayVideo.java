package com.example.gallery;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;

public class PlayVideo extends AppCompatActivity {
    VideoView videoView;
    private ArrayList<File> files;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_video);
        videoView=findViewById(R.id.videoView);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //initialize
        Intent intent=getIntent();
        files= (ArrayList<File>)intent.getSerializableExtra("files");
        position=intent.getIntExtra("position",0);

        //play the file
        play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
    }

    private void play(){
        MediaController mediaController=new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(files.get(position).toString()));
        videoView.requestFocus();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.setVideoURI(Uri.parse(files.get(position=position+1).toString()));
                videoView.start();
            }
        });
    }
}
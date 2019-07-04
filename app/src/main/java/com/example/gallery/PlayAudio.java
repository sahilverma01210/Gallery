package com.example.gallery;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class PlayAudio extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    private ArrayList<File> files;
    ImageView imageView;
    TextView textView;
    SeekBar seekBar;
    ImageButton next,previous,play_pause;
    int mark,index;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_audio);
        imageView=findViewById(R.id.imageView2);
        textView=findViewById(R.id.textView2);
        seekBar=findViewById(R.id.seekBar);
        next=findViewById(R.id.next);
        previous=findViewById(R.id.previous);
        play_pause=findViewById(R.id.play_pause);

        //get file from main activity and play
         final Intent intent=getIntent();
        files= (ArrayList<File>) intent.getSerializableExtra("files");
        index=intent.getIntExtra("position",0);
        play(files.get(index));

        //handle seekBar current position using another thread
        handler=new Handler();
        PlayAudio.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    int currentPosition=mediaPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(currentPosition);
                }
                handler.postDelayed(this,1000);
            }
        });

        //previous, next, play-pause
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                index--;
                mark=0;
                if(index>=0){
                    play(files.get(index));
                } else{
                    index=files.size()-1;
                    play(files.get(index));
                }
            }
        });

        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    play_pause.setImageResource(R.drawable.play);
                    mark=mediaPlayer.getCurrentPosition();
                }
                else {
                    play_pause.setImageResource(R.drawable.pause);
                    mediaPlayer.seekTo(mark);
                    mediaPlayer.start();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                index++;
                mark=0;
                if(index<=(files.size()-1)){
                    play(files.get(index));
                } else{
                    index=0;
                    play(files.get(index));
                }
            }
        });

        //change to next on end
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.stop();
                index++;
                mark=0;
                try{
                    play(files.get(index));
                }catch (Exception e){
                    index--;
                    play(files.get(index));
                }
            }
        });
    }

    protected void play(File file){
        mediaPlayer=MediaPlayer.create(PlayAudio.this,Uri.fromFile(file));
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(file.getPath());
            byte[] bytes=mmr.getEmbeddedPicture();
            if(bytes!=null){
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                imageView.setImageBitmap(bitmap);
            }
        } catch (RuntimeException ex) {
            // Do nothing
        }
        textView.setText(file.getName().replace(".mp3",""));
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration()/1000);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress*1000);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.stop();
                index++;
                mark=0;
                try{
                    play(files.get(index));
                }catch (Exception e){
                    index--;
                    play(files.get(index));
                }
            }
        });

        //Add file name to playlist cache
        File file1=new File(getApplicationContext().getCacheDir(),"playlist");
        savefile("playlist",file.getName()+"#");
    }

    //Save Playlist cache
    private void savefile(String file,String text){
        try{
            FileOutputStream foe=openFileOutput(file, Context.MODE_APPEND);
            foe.write(text.getBytes());
            foe.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}
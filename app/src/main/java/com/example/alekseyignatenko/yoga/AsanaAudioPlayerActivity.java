package com.example.alekseyignatenko.yoga;

import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class AsanaAudioPlayerActivity extends AppCompatActivity {

    private TextView AsanaNameView;
    private ImageView AsanaImage;
    Bitmap AsanaImageBitmap;
    MediaPlayer mediaPlayer;
    AudioManager am;
    private SeekBar seekBar;
    private final Handler handler = new Handler();
    Uri AudioPath;
    Uri ImagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asana_audio_player);

        AsanaNameView = (TextView) findViewById(R.id.AsanaNameInPlayer);
        AsanaImage = (ImageView) findViewById(R.id.AsanaImageInPlayer);
        seekBar = (SeekBar) findViewById(R.id.seekBarInAsanaAudioPlayer);
        am = (AudioManager) getSystemService(AUDIO_SERVICE);


        Intent intent = getIntent();
        String AsanaName =intent.getExtras().getString("AsanaName");

        String AudioPath = getFilesDir()+File.separator+AsanaName+"Audio.mp3";

        File internalStorageDir = getFilesDir();
        File SaveAsanaImage = new File(internalStorageDir,AsanaName+"Image.png");
        try {
            FileInputStream fis = new FileInputStream(SaveAsanaImage);
            try {
                AsanaImageBitmap=BitmapFactory.decodeStream(fis);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        AsanaNameView.setText(AsanaName);
        AsanaImage.setImageBitmap(AsanaImageBitmap);

        try{
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(AudioPath);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            seekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.start();
        }catch (IOException e){}
        SeekBarProgressUpdate();


        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(mediaPlayer.isPlaying()){
                    SeekBar SB = (SeekBar) view;
                 mediaPlayer.seekTo(SB.getProgress());
                }
                return false;
            }
        });
    }

    public void onClickPause (View view){
        if(mediaPlayer.isPlaying()){
        mediaPlayer.pause();
        }
    }

    public void onClickPlay(View view) {
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            SeekBarProgressUpdate();
        }
    }

    public void SeekBarProgressUpdate(){
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        if(mediaPlayer.isPlaying()){
            Runnable Notification = new Runnable() {
                @Override
                public void run() {
                    SeekBarProgressUpdate();
                }
            };
            handler.postDelayed(Notification,1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

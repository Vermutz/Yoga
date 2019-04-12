package com.example.alekseyignatenko.yoga;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PracticeAudioPlayer extends AppCompatActivity {

    private TextView AsanaNameView;
    private ImageView AsanaImage;
    private SeekBar seekBar;

    SavePractice savePractice;
    String PracticeName;
    LinkedList<String> LinkedListAsanasList;
    Map AsanaTimeHashMap;

    Integer AsanaTime;
    Integer NumberAsanaToPlay;
    Boolean PausActiv=false;
    Integer SeekBarMaxValue = 10000;
    //Boolean Complet=true;

    MediaPlayer mediaPlayer;
    AudioManager am;

    private final Handler handler = new Handler();
    //Uri AudioPath;
    //Uri ImagePath;
   ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_audio_player);

        AsanaNameView = findViewById(R.id.AsanaNameInPracticeAudioPlayer);
        AsanaImage = findViewById(R.id.ImageViewInPracticeAudioPlayer);
        seekBar = findViewById(R.id.seekBarInPracticeAudioPlayer);

        //seekBar.setEnabled(false);
        //seekBar.setClickable(false);

        PracticeName = getIntent().getExtras().getString("PracticeName");
        File internalStorageDir = getFilesDir();
        File Practic = new File(internalStorageDir,PracticeName);

        try {
            FileInputStream fis = new FileInputStream(Practic);
            try {
                ObjectInputStream is = new ObjectInputStream(fis);
                savePractice = (SavePractice) is.readObject();
                is.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        LinkedListAsanasList = savePractice.GetPractic();
        AsanaTimeHashMap = savePractice.GetAsanaTime();


        NumberAsanaToPlay = 0;


        SwitchAsanasInPlayList();

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }


   public void ProgressUpdate(){
       if(!PausActiv) {
           if (seekBar.getProgress() == SeekBarMaxValue) {
               if (mediaPlayer.isPlaying()) {
                   mediaPlayer.stop();
               }
                                  NextAsana();
           } else {
               Runnable Notification = new Runnable() {
                   @Override
                   public void run() {
                       ProgressUpdate();
                       seekBar.incrementProgressBy(SeekBarMaxValue / (AsanaTime * 10));
                   }
               };
               handler.postDelayed(Notification, 100);
           }
       }

    }

    public void NextAsana(){
        if(!PausActiv&((NumberAsanaToPlay+1)<=LinkedListAsanasList.size())) {
            NumberAsanaToPlay++;
            if((NumberAsanaToPlay+1)<=LinkedListAsanasList.size()) {
                SwitchAsanasInPlayList();
            }
        }else {
            if(!PausActiv){
                Toast.makeText(this, "Практика завершена", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onClickPause (View view){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        PausActiv=true;
    }

    public void onClickPlay(View view) {
        if(!mediaPlayer.isPlaying()&((NumberAsanaToPlay+1)<=LinkedListAsanasList.size())&PausActiv){
            mediaPlayer.start();
            PausActiv=false;
            ProgressUpdate();
        }
    }

    public void SwitchAsanasInPlayList() {
        if (((NumberAsanaToPlay + 1) <= LinkedListAsanasList.size())) {
            releaseMP();
            mediaPlayer = new MediaPlayer();

            String AsanaName = LinkedListAsanasList.get(NumberAsanaToPlay);
            AsanaTime = (Integer) AsanaTimeHashMap.get(AsanaName + String.valueOf(NumberAsanaToPlay + 1));
            String AsanaAudioPath = getFilesDir()+File.separator+AsanaName+"Audio.mp3";
            String AsanaImagePath= getFilesDir()+File.separator+AsanaName+"Image.png";

            AsanaNameView.setText(AsanaName);
            Bitmap BM = BitmapFactory.decodeFile(AsanaImagePath);
            AsanaImage.setImageBitmap(BM);


            try {
                mediaPlayer.setDataSource(AsanaAudioPath);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.prepare();
                seekBar.setMax(SeekBarMaxValue);
                seekBar.setProgress(0);
                mediaPlayer.start();
            } catch (IOException e) {
            }
            ProgressUpdate();
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

    private void releaseMP() {
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

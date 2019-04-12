package com.example.alekseyignatenko.yoga;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.provider.MediaStore;

import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.net.Uri;
import android.content.Intent;

import android.text.TextUtils;
import android.util.TimeUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CreatyAsanaActivity extends AppCompatActivity {

    //Объявляем используемые переменные:
    private ImageView imageView;
    private  EditText etName;
    private final int Pick_image = 1;
    private final int Pick_audio = 2;
    Bitmap selectedImage;
    public Uri imageUri;
    public Uri audioUri;
    //String AudioPath;
    public Button Stop, Play;
    public MediaPlayer mediaPlayer;

    private SharedPreferences sPrefAsanaName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creaty_asana);


        //Связываемся с нашим ImageView:
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setVisibility(View.INVISIBLE);

        //Связываемся с нашей кнопкой Button:
        Button PickImage = (Button) findViewById(R.id.AddPicture);
        Button PickAudio = (Button)findViewById(R.id.addAudioFailBotton);
        Play = (Button) findViewById(R.id.PlayInAsanaCreaty);
        Stop = (Button) findViewById(R.id.StopInAsanaCreaty);
        etName = (EditText) findViewById(R.id.AsanaName);

        sPrefAsanaName = getSharedPreferences("AsanaName", MODE_PRIVATE);

        //Невидимая кнопка проигрывателя
        Play.setVisibility(View.INVISIBLE);
        Stop.setVisibility(View.INVISIBLE);

        //Кнопки проигрывания
        //Настраиваем для нее обработчик нажатий OnClickListener:
        PickImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                releaseMP();
                //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                //Тип получаемых объектов - image:
                intent.setType("image/*");
                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                startActivityForResult(intent, Pick_image);
            }
        });

        PickAudio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseMP();
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.setType("audio/*");
                startActivityForResult(intent,Pick_audio);
            }
        });
    }

    //Обрабатываем результат выбора в галерее:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case Pick_image:

                     try {

                    //Получаем URI изображения, преобразуем его в Bitmap
                    //объект и отображаем в элементе ImageView нашего интерфейса:
                    imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageView.setImageBitmap(selectedImage);
                    //imageView.setImageURI(imageUri);
                    imageView.setVisibility(View.VISIBLE);
                    //File myFile = new File(imageUri.toString());

                    //Toast.makeText(getApplicationContext(), myFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException e) {
                       e.printStackTrace();
                     }
                    break;
                case Pick_audio:
                    //the selected audio.
                    audioUri = data.getData();
                    //File myFile = new File(audioUri.toString());

                    //AudioPath = audioUri.getPath().toString();

                    Play.setVisibility(View.VISIBLE);
                    Stop.setVisibility(View.VISIBLE);
                    break;
            }
        }

    };


    public void onClickStart(View view){
        releaseMP();
        //mediaPlayer = MediaPlayer.create(this,audioUri);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this,audioUri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();        }

        mediaPlayer.start();
    }

    public void onClickStop(View view){
        mediaPlayer.stop();
        releaseMP();
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

    private boolean UniqueName (){
        boolean uniqueName = true;
        String AsanName =etName.getText().toString();
        Map MapAllName = sPrefAsanaName.getAll();
        Set<String> SetAllName = MapAllName.keySet();
        Iterator<String> iterator = SetAllName.iterator();
        while (iterator.hasNext()){
            String item = iterator.next();
            if(AsanName.equals(item)){
                uniqueName=false;
            }
        }
        return  uniqueName;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMP();
    }


    public void onBottomSaveAsanaClik(View view) {

            if (!TextUtils.isEmpty(etName.getText().toString().trim())) {
                if(UniqueName()) {
                    if (imageUri != null) {

                        if (audioUri != null) {
                            releaseMP();
                            String AsanName = etName.getText().toString();

                            File InternalStorageDir = getFilesDir();
                            File AsanaImage = new File(InternalStorageDir,AsanName+"Image.png");

                            try{
                                FileOutputStream fos = new FileOutputStream(AsanaImage);
                                try {
                                    selectedImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                }finally {
                                    if(fos!=null){
                                        fos.flush();
                                        fos.close();
                                    }
                                }
                            }catch (IOException e){
                                e.printStackTrace();
                            }

                            SaveAudio(audioUri,AsanName);

                            Editor AsanaNameEditor = sPrefAsanaName.edit();
                            AsanaNameEditor.putString(AsanName, AsanName);
                            AsanaNameEditor.commit();

                            finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Добавьте аудио файл", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Добавьте картинку", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Асана с таким названием уже существует", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Введите название асаны", Toast.LENGTH_LONG).show();
        }
    }

    void SaveAudio(Uri sourceuri, String AsanaName) {
        String OutPutFile = getFilesDir() + File.separator + AsanaName + "Audio.mp3";

        try {
            InputStream ins = getContentResolver().openInputStream(sourceuri);
            File f = new File(OutPutFile);
            f.setWritable(true, false);
            OutputStream ops = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int lenght = 0;
            while ((lenght = ins.read(buffer)) > 0) {
                ops.write(buffer,0,lenght);
            }
            ins.close();
            ops.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

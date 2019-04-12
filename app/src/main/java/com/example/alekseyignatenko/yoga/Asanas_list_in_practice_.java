package com.example.alekseyignatenko.yoga;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

public class Asanas_list_in_practice_ extends AppCompatActivity {

    SavePractice savePractice;
    String PracticeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asanas_list_in_practice_);

        ListView AsanasList = findViewById(R.id.AsanaListViewInAsanasListInPractice);
        Button Play = findViewById(R.id.PlayInAsanasListInPractice);

        PracticeName = getIntent().getExtras().getString("PracticeName")+".csv";
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
        LinkedList<String> LinkedListAsanasList = savePractice.GetPractic();

      ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this,
               android.R.layout.simple_list_item_1, LinkedListAsanasList);
       AsanasList.setAdapter(Adapter);

       Play.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent inten = new Intent(".PracticeAudioPlayer");
               inten.putExtra("PracticeName",PracticeName);
               startActivity(inten);
           }
       });
    }
}

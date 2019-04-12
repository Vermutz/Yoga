package com.example.alekseyignatenko.yoga;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class CreatyPracticeActivity extends AppCompatActivity {

    private Button AddAsana,RemoveAsana,SavePracticeList;
    private ListView AsanaListInNewPractice;
    private LinkedList<String> AsanasInNewPractice = new LinkedList<String>();
    private ArrayAdapter<String> Adapter;
    private EditText etPracticeName;
    private HashMap<String,Integer> AsanaTimeInNewPractice = new HashMap<String, Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creaty_practice);

        AddAsana = (Button) findViewById(R.id.AddNewAsanaInPracticeCreaty);
        RemoveAsana = (Button) findViewById(R.id.RemoveAsanaInPracticeCreaty);
        SavePracticeList = (Button) findViewById(R.id.SavePracticeInPracticeCreaty);
        AsanaListInNewPractice = (ListView) findViewById(R.id.AsanaListInPracticeCreaty);
        etPracticeName = (EditText) findViewById(R.id.PracticeNameInPracticeCreaty);

        Adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, AsanasInNewPractice);

        AsanaListInNewPractice.setAdapter(Adapter);

        AddAsana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(".add_asana_in_practice_list");
                startActivityForResult(intent, 1);
            }
        });

        SavePracticeList.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(etPracticeName.getText().toString().trim())) {
                    if(UniquePracticeName()) {
                        if (AsanasInNewPractice.size() != 0) {
                            String PracticeName = etPracticeName.getText().toString();

                            SharedPreferences sPrefPracticeName=getSharedPreferences("PracticeName",MODE_PRIVATE);
                            SharedPreferences sPrefAsansInPractice = getSharedPreferences(PracticeName,MODE_PRIVATE);

                            SharedPreferences.Editor EditorPracticeName =sPrefPracticeName.edit();
                            EditorPracticeName.putString(PracticeName,PracticeName);
                            EditorPracticeName.commit();

                            Iterator IT = AsanaTimeInNewPractice.entrySet().iterator();
                            while(IT.hasNext()){
                                Map.Entry pair =(Map.Entry)IT.next();
                                SharedPreferences.Editor SaveAsansEditor =sPrefAsansInPractice.edit();
                                SaveAsansEditor.putInt((String)pair.getKey(),(Integer) pair.getValue());
                                SaveAsansEditor.commit();
                            }

                            File internalStorageDir = getFilesDir();
                            File SaveNewPractic = new File(internalStorageDir, PracticeName + ".csv");

                            if (!SaveNewPractic.exists()) {
                                try {
                                    SaveNewPractic.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            SavePractice savePractice = new SavePractice();
                            savePractice.SavePractic(AsanasInNewPractice);
                            savePractice.SaveAsanaTime(AsanaTimeInNewPractice);

                            try {
                                FileOutputStream fos = new FileOutputStream(SaveNewPractic);
                                try {
                                    ObjectOutputStream os = new ObjectOutputStream(fos);
                                    os.writeObject(savePractice);
                                    os.close();
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Добавьте асану", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Практика с таким названием уже существует", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Введите название практики", Toast.LENGTH_LONG).show();
                }
            }
        });

        RemoveAsana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AsanaListInNewPractice.getCheckedItemCount()!=0) {
                    SparseBooleanArray sbArray = AsanaListInNewPractice.getCheckedItemPositions();
                    for (int i = 0; i < sbArray.size(); i++) {
                        if (sbArray.get(sbArray.keyAt(i))) {
                            String AN = AsanasInNewPractice.get(sbArray.keyAt(i));
                            AsanasInNewPractice.remove(sbArray.keyAt(i));
                            AsanaListInNewPractice.setItemChecked(sbArray.keyAt(i), false);
                            AsanaTimeInNewPractice.remove(AN + String.valueOf(sbArray.keyAt(i)));
                        }
                    }
                    Adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getApplicationContext(), "Выберите асаны для удаления", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            String AsanaName = data.getStringExtra("AsanaName");
            Integer AsanaTime = data.getIntExtra("AsanaLong",0);
            AsanasInNewPractice.add(AsanaName);
            Adapter.notifyDataSetChanged();
            int AsanaNumber = AsanasInNewPractice.size();
            AsanaTimeInNewPractice.put(AsanaName+String.valueOf(AsanaNumber),AsanaTime);
        }
    }

    private Boolean UniquePracticeName(){
        boolean UniquePractice=true;

        String PN = etPracticeName.getText().toString();
        SharedPreferences sPrefPracticeName=getSharedPreferences("PracticeName",MODE_PRIVATE);
        Map AllPracticeName = sPrefPracticeName.getAll();
        Set<String> SetAllPracticeName = AllPracticeName.keySet();
        String[] PracticeListString = SetAllPracticeName.toArray(new String[SetAllPracticeName.size()]);

        for(int i = 0; i < PracticeListString.length;i++){
            String PracticeName = PracticeListString[i];
            PracticeName = PracticeName.substring(0, PracticeName.length() - 4);
            if(PracticeName.equals(PN)){
                UniquePractice=false;
            }
        };
        return UniquePractice;
    }
}

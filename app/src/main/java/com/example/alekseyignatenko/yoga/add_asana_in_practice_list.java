package com.example.alekseyignatenko.yoga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;

public class add_asana_in_practice_list extends AppCompatActivity {

    ListView AsanaListView;
    EditText Time;
    Button AddAsana;
    String[] AllName = {};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_asana_in_practice_list);

        AsanaListView = (ListView) findViewById(R.id.AsanaListInAddAsanaInPracticeList);
        Time = findViewById(R.id.TimeInAddAsanaInPractice);
        AddAsana = findViewById(R.id.ButtonAddAsanaInPractice);

        SharedPreferences sPrefAsanaName = getSharedPreferences("AsanaName",MODE_PRIVATE);
        Map MapAllName = sPrefAsanaName.getAll();
        Set<String> SetAllName = MapAllName.keySet();
        SetAllName.add("Закончили");
        AllName = SetAllName.toArray(new String[SetAllName.size()]);

        AsanaListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice, AllName);

        AsanaListView.setAdapter(adapter);
        AddAsana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AsanaListView.getCheckedItemCount()==1) {
                    if (!TextUtils.isEmpty(Time.getText().toString().trim())) {
                        Intent intent = new Intent();
                        String AsanaName = AllName[AsanaListView.getCheckedItemPosition()];
                        Integer AsanaLong = Integer.parseInt(Time.getText().toString());
                        intent.putExtra("AsanaLong", AsanaLong);
                        intent.putExtra("AsanaName", AsanaName);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Введите длительность асаны", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Выберите асану", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}

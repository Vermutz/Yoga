package com.example.alekseyignatenko.yoga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;


import java.util.Map;
import java.util.Set;

public class AsanaListActivity extends AppCompatActivity {

    ListView AsanaListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asana_list);

        final Intent intent = new Intent(".AsanaAudioPlayerActivity");

        String[] AllName = {};

        AsanaListView = (ListView) findViewById(R.id.AsanaListListView);

        SharedPreferences sPrefAsanaName = getSharedPreferences("AsanaName",MODE_PRIVATE);
        Map MapAllName = sPrefAsanaName.getAll();
        Set<String> SetAllName = MapAllName.keySet();
        AllName = SetAllName.toArray(new String[SetAllName.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, AllName);

        AsanaListView.setAdapter(adapter);

        AsanaListView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                TextView AsanaNameTextView =  (TextView) view;
                String AsanaName = AsanaNameTextView.getText().toString();
                intent.putExtra("AsanaName",AsanaName);
                startActivity(intent);
            }
        });
    }

}

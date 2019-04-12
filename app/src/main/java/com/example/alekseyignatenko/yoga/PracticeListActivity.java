package com.example.alekseyignatenko.yoga;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Map;
import java.util.Set;

public class PracticeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_list);

        ListView PracticeListView=(ListView) findViewById(R.id.PracticeListInPracticeListLayout);

        SharedPreferences PracticeName = getSharedPreferences("PracticeName",MODE_PRIVATE);
        Map AllPracticeName = PracticeName.getAll();
        Set<String> SetAllPracticeName = AllPracticeName.keySet();
        String[] PracticeListString = SetAllPracticeName.toArray(new String[SetAllPracticeName.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, PracticeListString);

        PracticeListView.setAdapter(adapter);

        PracticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView PracticeNameTextView =  (TextView) view;
                String PracticeName = PracticeNameTextView.getText().toString();
                Intent inten = new Intent(".Asanas_list_in_practice_");
                inten.putExtra("PracticeName",PracticeName);
                startActivity(inten);
            }
        });
    }
}

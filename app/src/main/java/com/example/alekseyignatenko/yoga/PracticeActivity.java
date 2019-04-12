package com.example.alekseyignatenko.yoga;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PracticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
    }

    public void onBottomCreatyPracticeClik(View view){
        Intent intent = new Intent(".CreatyPracticeActivity");
        startActivity(intent);
    }
    public void onBottomPracticeListClik(View view){
        Intent intent = new Intent(".PracticeListActivity");
        startActivity(intent);
    }
}

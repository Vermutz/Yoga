package com.example.alekseyignatenko.yoga;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AsanaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asana);
    }
    public void onBottomCreatyAsanaClik(View view){
        Intent intent = new Intent(".CreatyAsanaActivity");
        startActivity(intent);
    }
    public void onBottomAsanaListClik(View view){
        Intent intent = new Intent(".AsanaListActivity");
        startActivity(intent);
    }

}

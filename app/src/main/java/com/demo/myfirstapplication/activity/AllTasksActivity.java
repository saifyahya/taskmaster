package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

import com.demo.myfirstapplication.R;

public class AllTasksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView back = findViewById(R.id.backbutton);
        back.setOnClickListener(view ->  {
                Intent gobackFormIntent = new Intent(AllTasksActivity.this, MainActivity.class);
                startActivity(gobackFormIntent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_meneu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
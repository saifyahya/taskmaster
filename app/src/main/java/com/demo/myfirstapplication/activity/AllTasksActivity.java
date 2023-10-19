package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.demo.myfirstapplication.R;

public class AllTasksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

        ImageView back = findViewById(R.id.backbutton);
        back.setOnClickListener(view ->  {
                Intent gobackFormIntent = new Intent(AllTasksActivity.this, MainActivity.class);
                startActivity(gobackFormIntent);
        });
    }
}
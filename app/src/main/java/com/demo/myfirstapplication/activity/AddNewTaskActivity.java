package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.demo.myfirstapplication.R;

public class AddNewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        Button addTask = findViewById(R.id.addtaskbutton2);
        addTask.setOnClickListener(view -> {
            Toast.makeText(AddNewTaskActivity.this,"Task added",Toast.LENGTH_LONG).show();
        });
        ImageView back = findViewById(R.id.backbutton);
        back.setOnClickListener(view ->  {
            Intent gobackFormIntent = new Intent(AddNewTaskActivity.this, MainActivity.class);
            startActivity(gobackFormIntent);
        });

    }
}
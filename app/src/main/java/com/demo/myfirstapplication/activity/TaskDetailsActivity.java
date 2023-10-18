package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.demo.myfirstapplication.R;

public class TaskDetailsActivity extends AppCompatActivity {
TextView taskTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        taskTitle= findViewById(R.id.textViewTitle);

    }
}
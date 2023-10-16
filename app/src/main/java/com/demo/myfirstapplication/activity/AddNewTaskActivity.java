package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.demo.myfirstapplication.R;

public class AddNewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
    }
}
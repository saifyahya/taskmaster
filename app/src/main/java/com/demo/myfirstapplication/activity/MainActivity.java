package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.demo.myfirstapplication.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         Button addTaskButton = (Button) findViewById(R.id.addTaskButton);
         addTaskButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 System.out.println("Add Task Button Clicked");
             }
         });
    }

}
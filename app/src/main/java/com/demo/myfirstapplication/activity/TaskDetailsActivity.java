package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.myfirstapplication.R;

public class TaskDetailsActivity extends AppCompatActivity {
    TextView taskTitle;
    String taskTitleStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        ImageView back = findViewById(R.id.backbutton);
        back.setOnClickListener(view ->  {
            Intent gobackFormIntent = new Intent(TaskDetailsActivity.this, MainActivity.class);
            startActivity(gobackFormIntent);
        });

        Intent callingIntent = getIntent();
        if (callingIntent != null)
            taskTitleStr = callingIntent.getStringExtra(MainActivity.TASK_TAG);
        taskTitle = findViewById(R.id.textViewTitle);
        if ((taskTitleStr != null))
            taskTitle.setText(taskTitleStr);
        else
            taskTitle.setText("Not Specified");
    }
}
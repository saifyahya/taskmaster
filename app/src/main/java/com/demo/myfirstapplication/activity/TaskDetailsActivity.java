package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.demo.myfirstapplication.R;
import com.demo.myfirstapplication.activity.database.DatabaseSingleton;
import com.demo.myfirstapplication.activity.database.TaskDatabase;
import com.demo.myfirstapplication.activity.enums.TaskState;
import com.google.android.material.snackbar.Snackbar;

public class TaskDetailsActivity extends AppCompatActivity {
    TextView taskTitle;
    String taskTitleStr;
    TextView taskBody;
    String taskBodyStr;
    TextView taskEndDate;
    String taskEndDateStr;
    TaskDatabase taskDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        /*Displaying Task Info*/
        Intent callingIntent = getIntent();
        if (callingIntent != null){
            taskTitleStr = callingIntent.getStringExtra(MainActivity.TASK_TAG);
            taskBodyStr=callingIntent.getStringExtra("taskBody");
            taskEndDateStr = callingIntent.getStringExtra("taskDate");
        }
        taskTitle = findViewById(R.id.textViewTitle);
        settingTaskInfo(taskTitle,taskTitleStr);

        taskBody = findViewById(R.id.taskDetailsDescription);
        settingTaskInfo(taskBody,taskBodyStr);

        taskEndDate = findViewById(R.id.taskDateView);
        settingTaskInfo(taskEndDate,taskEndDateStr);

        /*Database connection*/
taskDatabase= DatabaseSingleton.getInstance(getApplicationContext());

        /*updating Task State Part*/
        Spinner taskState = findViewById(R.id.spinner);
        taskState.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                TaskState.values()));
        Button changeState = findViewById(R.id.changeStateButton);
        changeState.setOnClickListener(view -> {
           long retrievedTaskId = callingIntent.getLongExtra("taskId",-1);
            TaskState newTaskState = TaskState.fromString(taskState.getSelectedItem().toString());
            if(retrievedTaskId!=-1)
           taskDatabase.taskDAO().updateTaskState(newTaskState,retrievedTaskId);
            Snackbar.make(findViewById(R.id.TaskDetailsLayout), "Task state updated", Snackbar.LENGTH_SHORT).show();
        });

        ImageView back = findViewById(R.id.backbutton);
        back.setOnClickListener(view ->  {
            Intent gobackFormIntent = new Intent(TaskDetailsActivity.this, MainActivity.class);
            startActivity(gobackFormIntent);
        });
    }
    private void settingTaskInfo(TextView textView , String textViewStr){
        if ((textViewStr != null))
            textView.setText(textViewStr);
        else
            textView.setText("Not Specified");
    }
}
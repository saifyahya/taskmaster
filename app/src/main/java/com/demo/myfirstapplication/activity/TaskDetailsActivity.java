package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.demo.myfirstapplication.R;
//import com.demo.myfirstapplication.activity.database.DatabaseSingleton;
//import com.demo.myfirstapplication.activity.database.TaskDatabase;
import com.demo.myfirstapplication.activity.enums.TaskState;
import com.demo.myfirstapplication.activity.models.Task;
import com.google.android.material.snackbar.Snackbar;

public class TaskDetailsActivity extends AppCompatActivity {
    TextView taskTitle;
    TextView taskBody;
    TextView taskEndDate;
//    TaskDatabase taskDatabase;
    TextView taskState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        /*Database connection*/
//        taskDatabase= DatabaseSingleton.getInstance(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*Displaying Task Info*/
        Intent callingIntent = getIntent();
        if (callingIntent != null){
            long retrievedTaskId = callingIntent.getLongExtra("taskId",-1);
if(retrievedTaskId!=-1){
//            Task retrievedTask = taskDatabase.taskDAO().findById(retrievedTaskId);
//            if(retrievedTask!=null){
//                taskState = findViewById(R.id.stateView);
//                settingTaskInfo(taskState,retrievedTask.getState().toString());
//                taskTitle = findViewById(R.id.textViewTitle);
//                settingTaskInfo(taskTitle,retrievedTask.getTitle());
//
//                taskBody = findViewById(R.id.taskDetailsDescription);
//                settingTaskInfo(taskBody,retrievedTask.getBody());
//
//                taskEndDate = findViewById(R.id.taskDateView);
//                settingTaskInfo(taskEndDate,retrievedTask.getEndDate().toString());
//            }
        }}

        /*updating Task State Part*/
        Spinner taskState = findViewById(R.id.spinner);
        taskState.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                TaskState.values()));
        Button changeState = findViewById(R.id.changeStateButton);
        changeState.setOnClickListener(view -> {
           long retrievedTaskId = callingIntent.getLongExtra("taskId",-1);
            TaskState newTaskState = TaskState.fromString(taskState.getSelectedItem().toString());
            if(retrievedTaskId!=-1) {
//                taskDatabase.taskDAO().updateTaskState(newTaskState, retrievedTaskId);
                Snackbar.make(findViewById(R.id.TaskDetailsLayout), "Task state updated", Snackbar.LENGTH_SHORT).show();
            }
        });

        Button deleteTask = findViewById(R.id.deleteTask);
        deleteTask.setOnClickListener(view -> {
            long retrievedTaskId = callingIntent.getLongExtra("taskId",-1);
            if(retrievedTaskId!=-1){
//                taskDatabase.taskDAO().deleteById(retrievedTaskId);
                Snackbar.make(findViewById(R.id.TaskDetailsLayout), "Task Removed", Snackbar.LENGTH_SHORT).show();
            }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_meneu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
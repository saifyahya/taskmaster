package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.demo.myfirstapplication.R;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    public static final String TASK_TAG="taskName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        Button addTaskButton = (Button) findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(view -> {
            Intent goToNewTaskFormIntent = new Intent(MainActivity.this, AddNewTaskActivity.class);
            startActivity(goToNewTaskFormIntent);
        });

        ImageButton userProfile = (ImageButton) findViewById(R.id.userProfile);
        userProfile.setOnClickListener(view -> {
            Intent goToUserSettings = new Intent(MainActivity.this, UserSettingsActivity.class);
            startActivity(goToUserSettings);
        });

        Button allTasksButton = (Button) findViewById(R.id.allTasksButton);
        allTasksButton.setOnClickListener(v -> {
            Intent goToAllTasksFormIntent = new Intent(MainActivity.this, AllTasksActivity.class);
            startActivity(goToAllTasksFormIntent);
        });

        Button task1 = findViewById(R.id.task1);
        task1.setOnClickListener(view -> {
            Intent goToTaskDetails = new Intent(MainActivity.this, TaskDetailsActivity.class);
            goToTaskDetails.putExtra(TASK_TAG,task1.getText().toString());
            startActivity(goToTaskDetails);
        });
        Button task2 = findViewById(R.id.task2);
        task2.setOnClickListener(view -> {
            Intent goToTaskDetails = new Intent(MainActivity.this, TaskDetailsActivity.class);
            goToTaskDetails.putExtra(TASK_TAG,task2.getText().toString());
            startActivity(goToTaskDetails);
        });
        Button task3 = findViewById(R.id.task3);
        task3.setOnClickListener(view -> {
            Intent goToTaskDetails = new Intent(MainActivity.this, TaskDetailsActivity.class);
            goToTaskDetails.putExtra(TASK_TAG,task3.getText().toString());
            startActivity(goToTaskDetails);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView userTasks;
        userTasks = findViewById(R.id.usertasks);
        //SharedPreferences sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        String name = sp.getString(UserSettingsActivity.USERNAME_TAG, "no name");
        userTasks.setText(name.isEmpty() ? "tasks" : name + "'s tasks");
    }

}

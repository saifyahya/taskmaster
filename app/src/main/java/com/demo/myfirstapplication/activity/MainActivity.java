package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.demo.myfirstapplication.R;
import com.demo.myfirstapplication.activity.adapter.TasksRecyclerViewAdapter;
import com.demo.myfirstapplication.activity.database.DatabaseConverter;
import com.demo.myfirstapplication.activity.database.DatabaseSingleton;
import com.demo.myfirstapplication.activity.database.TaskDatabase;
import com.demo.myfirstapplication.activity.enums.TaskState;
import com.demo.myfirstapplication.activity.models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    public static final String TASK_TAG="taskName";
//    public static final String DATABASE_TAG="taskDatabase";
    TaskDatabase taskDatabase;
    List<Task> tasks;
    TasksRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                    /*Room Database*/
        taskDatabase= DatabaseSingleton.getInstance(getApplicationContext());
        tasks=taskDatabase.taskDAO().findAll();

                /*for toolbar*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView userTasks;
        userTasks = findViewById(R.id.usertasks);
        //SharedPreferences sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        String name = sp.getString(UserSettingsActivity.USERNAME_TAG, "no name");
        userTasks.setText(name.isEmpty() ? "tasks" : name + "'s tasks");


        // filter tasks based on user settings
        String taskFilter = sp.getString("filterState","");
        if(!taskFilter.isEmpty()){
            tasks.clear();
            tasks.addAll(taskDatabase.taskDAO().findTaskByState(TaskState.fromString(taskFilter)));
            adapter.notifyDataSetChanged();
        }
        else{
            // retrieving list of tasks from room database
            tasks.clear();
            tasks.addAll(taskDatabase.taskDAO().findAll());
            adapter.notifyDataSetChanged();
        }
    }

    private void setupRecyclerView(){
        RecyclerView tasksRV= (RecyclerView) findViewById(R.id.tasksRV);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        tasksRV.setLayoutManager(layoutManager);
         adapter = new TasksRecyclerViewAdapter(tasks,this);
        tasksRV.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_meneu,menu);
        return super.onCreateOptionsMenu(menu);
    }

}

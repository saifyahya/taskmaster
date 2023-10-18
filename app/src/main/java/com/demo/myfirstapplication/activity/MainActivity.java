package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
                 Intent goToNewTaskFormIntent = new Intent(MainActivity.this, AddNewTaskActivity.class);
                 startActivity(goToNewTaskFormIntent);
             }
         });
        Button userProfile = (Button) findViewById(R.id.userProfile);
userProfile.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent goToUserSettings = new Intent(MainActivity.this,UserSettingsActivity.class);
        startActivity(goToUserSettings);
    }
});

        Button allTasksButton = (Button) findViewById(R.id.allTasksButton);
    allTasksButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent goToAllTasksFormIntent = new Intent(MainActivity.this, AllTasksActivity.class);
            startActivity(goToAllTasksFormIntent);
        }
    });


    }
    @Override
    protected void onResume() {
        super.onResume();
        TextView userTasks;
        userTasks=findViewById(R.id.usertasks);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        String name = sp.getString("name","");
        userTasks.setText(name+"'s tasks");
    }

}

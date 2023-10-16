package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.demo.myfirstapplication.R;

public class AddNewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        Toast toast = Toast.makeText(this, "submitted!", Toast.LENGTH_SHORT);


        Button addTaskButton = (Button) findViewById(R.id.addtaskbutton2);
addTaskButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        toast.show();
    }
});
        Button backButton = (Button) findViewById(R.id.backbutton);
backButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent gobackFormIntent = new Intent(AddNewTaskActivity.this, MainActivity.class);
        startActivity(gobackFormIntent);
    }
});

    }
}
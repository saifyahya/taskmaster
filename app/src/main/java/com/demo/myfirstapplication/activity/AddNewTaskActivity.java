package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.demo.myfirstapplication.R;
import com.demo.myfirstapplication.activity.database.DatabaseSingleton;
import com.demo.myfirstapplication.activity.database.TaskDatabase;
import com.demo.myfirstapplication.activity.enums.TaskState;
import com.demo.myfirstapplication.activity.models.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

public class AddNewTaskActivity extends AppCompatActivity {
    TaskDatabase taskDatabase;
    Date selectedDate;
    Spinner taskStateSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        /*database connection*/
        taskDatabase = DatabaseSingleton.getInstance(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        taskStateSpinner = findViewById(R.id.spinner2);
        taskStateSpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                TaskState.values()));

        Button startDateButton = (Button) findViewById(R.id.startDateButton);
        startDateButton.setOnClickListener(view1 -> {
            openDialog();
        });

        Button addTask = (Button) findViewById(R.id.saveToDBButton);
        addTask.setOnClickListener(view -> {
            saveTaskToDatabase();
        });

        ImageView back = findViewById(R.id.backbutton);
        back.setOnClickListener(view -> {
            Intent gobackFormIntent = new Intent(AddNewTaskActivity.this, MainActivity.class);
            startActivity(gobackFormIntent);
        });
    }

    private void openDialog() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                // Convert the Calendar instance to a Date object
                selectedDate = calendar.getTime();
            }
        }, currentYear, currentMonth, currentDay);
        dialog.show();
    }

    private void saveTaskToDatabase() {
        EditText taskTitleEditText = (EditText) findViewById(R.id.taskTitleText);
        EditText taskDescriptionEditText = (EditText) findViewById(R.id.taskDescriptionText);
        String title = taskTitleEditText.getText().toString();
        String body = taskDescriptionEditText.getText().toString();
        TaskState selectedTaskState = TaskState.fromString(taskStateSpinner.getSelectedItem().toString());
        if (selectedDate == null) {
            selectedDate = Calendar.getInstance().getTime();  //setting default end day today
        }
        Task newTask = new Task(title, body, selectedTaskState, selectedDate);
        taskDatabase.taskDAO().insertTask(newTask);
        Snackbar.make(findViewById(R.id.addNewTaskActicity), "Task Saved", Snackbar.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_meneu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
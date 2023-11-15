package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStateEnum;
import com.demo.myfirstapplication.R;
//import com.demo.myfirstapplication.activity.database.DatabaseSingleton;
//import com.demo.myfirstapplication.activity.database.TaskDatabase;
import com.google.android.material.snackbar.Snackbar;

public class TaskDetailsActivity extends AppCompatActivity {
    Task retrievedTask;
    TextView taskTitle;
    TextView taskBody;
    TextView taskEndDate;
//    TaskDatabase taskDatabase;
    TextView taskState;
public static final String TAG ="retrievedTASK";
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
            String retrievedTaskId = callingIntent.getStringExtra("taskId");
if(retrievedTaskId!=null){
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
    Amplify.API.query(
            ModelQuery.get(Task.class, retrievedTaskId),
            response -> {
                 retrievedTask = response.getData();
                if (retrievedTask != null) {
                    updateUIWithTaskDetails(retrievedTask);
                } else {
                    // Task not found
                    // Handle the case where the task with the given ID is not found
                }
            },
            error -> {
                // Handle query error
                Log.e("GetTaskError", "Error fetching task by ID", error);
            }
    );
        }}

        /*updating Task State Part*/
//        Spinner taskState = findViewById(R.id.spinner);
//        taskState.setAdapter(new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_item,
//                TaskStateEnum.values()));
        Button changeState = findViewById(R.id.changeStateButton);
        changeState.setOnClickListener(view -> {
           String retrievedTaskId = callingIntent.getStringExtra("taskId");
            Log.d(TAG, "Retrieved Task ID: " + retrievedTaskId);

           // TaskStateEnum newTaskState =(TaskStateEnum) taskState.getSelectedItem();
            if(retrievedTaskId!=null) {
                Intent goToTaskDetails = new Intent(TaskDetailsActivity.this, EditTaskActivity.class);
                goToTaskDetails.putExtra("taskId",retrievedTaskId);
                startActivity(goToTaskDetails);
//                taskDatabase.taskDAO().updateTaskState(newTaskState, retrievedTaskId);
//                Task updatedTask = Task.builder()
//                        .title(retrievedTask.getTitle())
//                        .teamPerson(retrievedTask.getTeamPerson())
//                        .body(retrievedTask.getBody())
//                        .endDate(retrievedTask.getEndDate())
//                        .state(newTaskState)
//                        .build();
//
//                Amplify.API.mutate(
//                        ModelMutation.update(updatedTask),
//                        response -> {
//                            // Handle successful state update
//                            System.out.println("Task state updated successfully");
//                        },
//                        error -> {
//                            // Handle state update error
//                            System.err.println("Error updating task state: " + error);
//                        }
//                );
//                Snackbar.make(findViewById(R.id.TaskDetailsLayout), "Task state updated", Snackbar.LENGTH_SHORT).show();
            }
        });

        Button deleteTask = findViewById(R.id.deleteTask);
        deleteTask.setOnClickListener(view -> {
            String retrievedTaskId = callingIntent.getStringExtra("taskId");
            if(retrievedTaskId!=null){
//                taskDatabase.taskDAO().deleteById(retrievedTaskId);
                Amplify.API.mutate(
                        ModelMutation.delete(retrievedTask),
                        response -> {
                            // Handle successful deletion
                            System.out.println("Task deleted successfully");
                        },
                        error -> {
                            // Handle deletion error
                            System.err.println("Error deleting task: " + error);
                        }
                );
                Snackbar.make(findViewById(R.id.TaskDetailsLayout), "Task Removed", Snackbar.LENGTH_SHORT).show();
            }
        });

        ImageView back = findViewById(R.id.backbutton);
        back.setOnClickListener(view ->  {
            Intent gobackFormIntent = new Intent(TaskDetailsActivity.this, MainActivity.class);
            startActivity(gobackFormIntent);
        });
    }

    private void updateUIWithTaskDetails(Task retrievedTask) {
        runOnUiThread(() ->{
            taskState = findViewById(R.id.stateView);
            settingTaskInfo(taskState,retrievedTask.getState().toString());
            taskTitle = findViewById(R.id.textViewTitle);
            settingTaskInfo(taskTitle,retrievedTask.getTitle());

            taskBody = findViewById(R.id.taskDetailsDescription);
            settingTaskInfo(taskBody,retrievedTask.getBody());

            taskEndDate = findViewById(R.id.taskDateView);
            settingTaskInfo(taskEndDate,retrievedTask.getEndDate().format().split("T")[0]);
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
package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStateEnum;
import com.amplifyframework.datastore.generated.model.Team;
import com.demo.myfirstapplication.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class EditTaskActivity extends AppCompatActivity {
    TextView taskState;
    Task retrievedTask;

    EditText taskTitle;
    EditText taskBody;
    TextView taskTeam;
    CompletableFuture<List<Team>> teamFuture = new CompletableFuture<>();


    Spinner teamSpinner;
    Spinner taskStateSpinner;

    Date selectedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setContentView(R.layout.activity_edit_task);
        Button startDateButton = findViewById(R.id.startDateButton_editTask);
        startDateButton.setOnClickListener(view1 -> {
            openDialog();
        });

        setUpSpinners();
        Intent callingIntent = getIntent();
        if (callingIntent != null) {
            String retrievedTaskId = callingIntent.getStringExtra("taskId");
            if (retrievedTaskId != null) {


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

                Button changeState = findViewById(R.id.saveToDBButton_editTask);
                changeState.setOnClickListener(view -> {

                    List<Team> teams = null;
                    try {
                        teams = teamFuture.get();
                    } catch (InterruptedException ie) {
                        Log.e("Edit Task Activity", " InterruptedException while getting teams");
                    } catch (ExecutionException ee) {
                        Log.e("Edit Task Activity", " ExecutionException while getting teams");
                    }

                    if (selectedDate == null) {
                        selectedDate = Calendar.getInstance().getTime();  //setting default end day today
                    }
                    String selectedTeamString = teamSpinner.getSelectedItem().toString();
                    Team selectedTeam = teams.stream().filter(c -> c.getName().equals(selectedTeamString)).findAny().orElseThrow(RuntimeException::new);

                    Log.d("Edit Task Activity", "Retrieved Task ID: " + retrievedTaskId);
                    Log.d("Edit Task Activity", "Retrieved Task title: " + taskTitle.getText().toString());
                    Log.d("Edit Task Activity", "Retrieved Task body: " + taskBody.getText().toString());
                    Log.d("Edit Task Activity", "Retrieved Task new state: " + taskStateSpinner.getSelectedItem());
                    Log.d("Edit Task Activity", "Retrieved Task new team: " + selectedTeam.getName());
                    Log.d("Edit Task Activity", "Retrieved Task end date: " + retrievedTask.getEndDate());
                    Task updatedTask = Task.builder()
                            .title(taskTitle.getText().toString())
                            .id(retrievedTaskId)
                            .teamPerson(selectedTeam)
                            .body(taskBody.getText().toString())
                            .endDate(new Temporal.DateTime(selectedDate,0 ))
                            .state((TaskStateEnum) taskStateSpinner.getSelectedItem())
                            .build();


                    Amplify.API.mutate(
                            ModelMutation.update(updatedTask),
                            response -> {
                                // Handle successful state update
                                System.out.println("Task state updated successfully");
                            },
                            error -> {
                                // Handle state update error
                                System.err.println("Error updating task state: " + error);
                            }
                    );
//                    Snackbar.make(findViewById(R.id.editTaskActicity), "Task updated", Snackbar.LENGTH_SHORT).show();
                    Intent goToTaskDetails = new Intent(EditTaskActivity.this, TaskDetailsActivity.class);
                    goToTaskDetails.putExtra("taskId", retrievedTaskId);
                    startActivity(goToTaskDetails);
                });
            }
        }
        ImageView back = findViewById(R.id.backbutton_editTask);
        back.setOnClickListener(view -> {
            Intent gobackFormIntent = new Intent(EditTaskActivity.this, MainActivity.class);
            startActivity(gobackFormIntent);
        });
    }

    private void updateUIWithTaskDetails(Task retrievedTask) {
        runOnUiThread(() -> {
            taskState = findViewById(R.id.textView_editTask1);
            taskState.setText(retrievedTask.getState().toString());

            taskTitle = findViewById(R.id.taskTitleText_editTask);
            taskTitle.setText(retrievedTask.getTitle());

            taskBody = findViewById(R.id.taskDescriptionText_editTask);
            taskBody.setText(retrievedTask.getBody());

            taskTeam = findViewById(R.id.textView_editTask);
            taskTeam.setText(retrievedTask.getTeamPerson().getName());
        });

    }

    private void setUpSpinners() {

        taskStateSpinner = findViewById(R.id.spinner_editTask1);
        taskStateSpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                TaskStateEnum.values()));


        teamSpinner = findViewById(R.id.spinner_editTask2);
        Amplify.API.query(
                ModelQuery.list(Team.class),
                success ->
                {
                    Log.i("User settings activity", "Read Team Successfully");
                    ArrayList<String> teamNames = new ArrayList<>();
                    ArrayList<Team> teams = new ArrayList<>();
                    for (Team team : success.getData()) {
                        teams.add(team);
                        teamNames.add(team.getName());
                    }
                    teamFuture.complete(teams);

                    runOnUiThread(() ->
                    {
                        teamSpinner.setAdapter(new ArrayAdapter<>(
                                this,
                                (android.R.layout.simple_spinner_item),
                                teamNames
                        ));
                    });
                },
                failure -> {
                    teamFuture.complete(null);
                    Log.i("Edit Task Activity", "Did not read teams successfully");
                }
        );

    }
    private void openDialog() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(year, month, day);
            // Convert the Calendar instance to a Date object
            selectedDate = calendar1.getTime();
        }, currentYear, currentMonth, currentDay);
        dialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_meneu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
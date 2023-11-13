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

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.TaskStateEnum;
import com.amplifyframework.datastore.generated.model.Team;
import com.demo.myfirstapplication.R;
//import com.demo.myfirstapplication.activity.database.DatabaseSingleton;
//import com.demo.myfirstapplication.activity.database.TaskDatabase;

import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddNewTaskActivity extends AppCompatActivity {
//    TaskDatabase taskDatabase;
    Date selectedDate;
    Spinner taskStateSpinner;
    Spinner teamSpinner;
    public static final String TAG = "AddTaskActivity";
    CompletableFuture<List<Team>> teamFuture = new CompletableFuture<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        /*database connection*/
//        taskDatabase = DatabaseSingleton.getInstance(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        teamFuture = new CompletableFuture<>();

        taskStateSpinner = findViewById(R.id.spinner2);
        taskStateSpinner.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                TaskStateEnum.values()));
        setUpSpinners();

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
        TaskStateEnum selectedTaskState =(TaskStateEnum) taskStateSpinner.getSelectedItem();
        if (selectedDate == null) {
            selectedDate = Calendar.getInstance().getTime();  //setting default end day today
        }
        String currentDateString = com.amazonaws.util.DateUtils.formatISO8601Date(selectedDate);
        String selectedTeamString = teamSpinner.getSelectedItem().toString();

        List<Team> teams=null;
        try {
            teams=teamFuture.get();
        }catch (InterruptedException ie){
            Log.e(TAG, " InterruptedException while getting teams");
        }catch (ExecutionException ee){
            Log.e(TAG," ExecutionException while getting teams");
        }

        Team selectedTeam = teams.stream().filter(c -> c.getName().equals(selectedTeamString)).findAny().orElseThrow(RuntimeException::new);
//        Task newTask = new Task(title, body, selectedTaskState, selectedDate);
        Task newTask = Task.builder()
                .title(title)
                .body(body)
                .endDate( new Temporal.DateTime(selectedDate,0 ))
                .state(selectedTaskState)
                .teamPerson(selectedTeam)
                .build();
//        taskDatabase.taskDAO().insertTask(newTask);
        Amplify.API.mutate(
                ModelMutation.create(newTask),
                successResponse -> Log.i(TAG, "AddTaskActivity.onCreate(): Task added successfully"),//success response
                failureResponse -> Log.e(TAG, "AddTaskActivity.onCreate(): failed with this response" + failureResponse)// in case we have a failed response
        );
        Snackbar.make(findViewById(R.id.addNewTaskActicity), "Task Saved", Snackbar.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_meneu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setUpSpinners(){
        teamSpinner = (Spinner) findViewById(R.id.spinner3);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success ->
                {
                    Log.i(TAG, "Read Team Successfully");
                    ArrayList<String> teamNames = new ArrayList<>();
                    ArrayList<Team> teams = new ArrayList<>();
                    for(Team team: success.getData()){
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
                failure-> {
                    teamFuture.complete(null);
                    Log.i(TAG, "Did not read teams successfully");
                }
        );
}
}
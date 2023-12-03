package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.demo.myfirstapplication.R;
import com.demo.myfirstapplication.activity.adapter.TasksRecyclerViewAdapter;
//import com.demo.myfirstapplication.activity.database.DatabaseConverter;
//import com.demo.myfirstapplication.activity.database.DatabaseSingleton;
//import com.demo.myfirstapplication.activity.database.TaskDatabase;
import com.demo.myfirstapplication.activity.enums.TaskState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    public final String TAG = "Main Activity";

    public static final String TASK_TAG="taskName";
//    public static final String DATABASE_TAG="taskDatabase";
//    TaskDatabase taskDatabase;
    List<Task> tasks;
    TasksRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                    /*Room Database*/
//        taskDatabase= DatabaseSingleton.getInstance(getApplicationContext());
//        tasks=taskDatabase.taskDAO().findAll();

 init();
        analytics();  // record opening the app
//createTeam();
setupRecyclerView();
setUpLoginAndLogoutButton();
    }

    @Override
    protected void onResume() {
        super.onResume();

        auth();

        filterTasks();
    }

    private void setupRecyclerView(){
        RecyclerView tasksRV= (RecyclerView) findViewById(R.id.tasksRV);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        tasksRV.setLayoutManager(layoutManager);

        tasks = new ArrayList<>();
//        tasks.add(new Task("english homework","homework unit 4",TaskState.NEW,new Date()));
//        Amplify.API.query(
//                ModelQuery.list(Task.class),
//                success ->
//                {
//                    Log.i("MainActivity", "Read Tasks successfully");
//                    tasks.clear();
//                    for (Task databaseTask : success.getData()){
//                        tasks.add(databaseTask);
//                    }
//                    runOnUiThread(() ->{
//                        adapter.notifyDataSetChanged();
//                    });
//                },
//                failure -> Log.i("MainActivity", "Did not read Tasks successfully")
//        );
         adapter = new TasksRecyclerViewAdapter(tasks,this);
        tasksRV.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_meneu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void createTeam(){
        Team team1 = Team.builder()
                .name("Developers")
                .build();
        Team team2 = Team.builder()
                .name("Testing")
                .build();
        Team team3 = Team.builder()
                .name("Managers")
                .build();
        Amplify.API.mutate(
                ModelMutation.create(team1),
                successResponse-> Log.i("Main Activity",  "create a team successfully"),
                failureResponse-> Log.i("Main Activity", "create a team successfully")
);
        Amplify.API.mutate(
                ModelMutation.create(team2),
                successResponse-> Log.i("Main Activity",  "create a team successfully"),
                failureResponse-> Log.i("Main Activity", "create a team successfully")
        );
        Amplify.API.mutate(
                ModelMutation.create(team3),
                successResponse-> Log.i("Main Activity",  "create a team successfully"),
                failureResponse-> Log.i("Main Activity", "create a team successfully")
        );
    }
    private void setUpLoginAndLogoutButton(){
        Button loginButton = (Button) findViewById(R.id.productListLoginButton);
        loginButton.setOnClickListener(v ->
        {
            Intent goToLogInIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(goToLogInIntent);
        });

        Button logoutButton = (Button) findViewById(R.id.productListLogoutButton);
        logoutButton.setOnClickListener(v->
        {
            Amplify.Auth.signOut(
                    () ->
                    {
                        Log.i(TAG,"Logout succeeded");
                        runOnUiThread(() ->
                        {
                            ((TextView)findViewById(R.id.userNicknameTextView)).setText("");
                        });
                        Intent goToLogInIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(goToLogInIntent);
                    },
                    failure ->
                    {
                        Log.i(TAG, "Logout failed");
                        runOnUiThread(() ->
                        {
                            Toast.makeText(MainActivity.this, "Log out failed", Toast.LENGTH_LONG);
                        });
                    }
            );
        });
    }

    private void init() {
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
    }
    private void auth(){

        // auth part
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        String username="";
        if (authUser == null){
            Button loginButton = (Button) findViewById(R.id.productListLoginButton);
            loginButton.setVisibility(View.VISIBLE);
            Button logoutButton = (Button) findViewById(R.id.productListLogoutButton);
            logoutButton.setVisibility(View.INVISIBLE);
        }else{
            username = authUser.getUsername();
            Log.i(TAG, "Username is: "+ username);
            Button loginButton = (Button) findViewById(R.id.productListLoginButton);
            loginButton.setVisibility(View.INVISIBLE);
            Button logoutButton = (Button) findViewById(R.id.productListLogoutButton);
            logoutButton.setVisibility(View.VISIBLE);
            // show user email in the main activity
            String username2 = username; // ugly way for lambda hack
            Amplify.Auth.fetchUserAttributes(
                    success ->
                    {
                        Log.i(TAG, "Fetch user attributes succeeded for username: "+username2);
                        for (AuthUserAttribute userAttribute: success){
                            if(userAttribute.getKey().getKeyString().equals("email")){
                                String userEmail = userAttribute.getValue();
                                runOnUiThread(() ->
                                {
                                    ((TextView)findViewById(R.id.userNicknameTextView)).setText(userEmail);
                                });
                            }
                        }
                    },
                    failure ->
                    {
                        Log.i(TAG, "Fetch user attributes failed: "+failure.toString());
                    }
            );
        }

        ////////////////
        TextView userTasks;
        userTasks = findViewById(R.id.usertasks);
        //SharedPreferences sp = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        String name = sp.getString(UserSettingsActivity.USERNAME_TAG, "no name");
        userTasks.setText(name.isEmpty() ? "tasks" : name + "'s tasks");
    }

    private void filterTasks(){
        // filter tasks based on user settings
        TextView userTeam;
        userTeam = findViewById(R.id.team_home);
        String teamName = sp.getString("filterTeam", "no name");
        userTeam.setText(teamName.isEmpty() ? "team" : teamName);

        String taskFilter = sp.getString("filterState","");
        if(!taskFilter.isEmpty()){
            Log.i("MainActivity", "inside not empty filter");

//            tasks.clear();
//            tasks.addAll(taskDatabase.taskDAO().findTaskByState(TaskState.fromString(taskFilter)));
//            adapter.notifyDataSetChanged();
            Amplify.API.query(
                    ModelQuery.list(Task.class),
                    success ->
                    {
                        Log.i("MainActivity", "Read Tasks successfully"+taskFilter);
                        tasks.clear();
                        for (Task databaseTask : success.getData()){
                            if(databaseTask.getState().toString().equals(taskFilter.toUpperCase()) && databaseTask.getTeamPerson().getName().equals(teamName) ) {
                                Log.i("MainActivity", "team associated:"+databaseTask.getTeamPerson().getName()+"team filter:"+teamName);

                                tasks.add(databaseTask);}
                        }
                        runOnUiThread(() ->{
                            adapter.notifyDataSetChanged();
                        });
                    },
                    failure -> Log.i("MainActivity", "Did not read Tasks successfully")
            );
        }
        else{
            // retrieving list of tasks from room database
//            tasks.clear();
//            tasks.addAll(taskDatabase.taskDAO().findAll());
//            adapter.notifyDataSetChanged();
            Log.i("MainActivity", "inside  empty filter");

            Amplify.API.query(
                    ModelQuery.list(Task.class),
                    success ->
                    {
                        Log.i("MainActivity", "Read Tasks successfully");
                        tasks.clear();
                        for (Task databaseTask : success.getData()){
                            tasks.add(databaseTask);
                        }
                        runOnUiThread(() ->{
                            adapter.notifyDataSetChanged();
                        });
                    },
                    failure -> Log.i("MainActivity", "Did not read Tasks successfully")
            );
        }
    }
    private void analytics(){  // recording an event which is opening the app
        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("openedApp")
                .addProperty("time", Long.toString(new Date().getTime()))
                .addProperty("trackingEvent", " main activity opened")
                .build();

        Amplify.Analytics.recordEvent(event);
    }
}

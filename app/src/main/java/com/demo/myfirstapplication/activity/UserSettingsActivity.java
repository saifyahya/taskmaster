package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.demo.myfirstapplication.R;
import com.demo.myfirstapplication.activity.enums.TaskState;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class UserSettingsActivity extends AppCompatActivity {
Button saveButton;
EditText username;
String usernameStr;
SharedPreferences sp;
Spinner teamSpinner;
public static final String USERNAME_TAG="username";
    CompletableFuture<List<Team>> teamFuture = new CompletableFuture<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView back = findViewById(R.id.backbutton);
        back.setOnClickListener(view ->  {
            Intent gobackFormIntent = new Intent(UserSettingsActivity.this, MainActivity.class);
            startActivity(gobackFormIntent);
        });

        saveButton= findViewById(R.id.buttonSaveUsername);
        username= findViewById(R.id.editTextUsername);
        //sp =getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE); //point to SP file in case of many SP
        sp= PreferenceManager.getDefaultSharedPreferences(this);
        saveButton.setOnClickListener(view ->  {

                usernameStr=username.getText().toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(USERNAME_TAG,usernameStr);
                editor.apply();
                Snackbar.make(findViewById(R.id.userSettingsActivity), "Username Saved", Snackbar.LENGTH_SHORT).show();  // the id represents the id of the layout
        });

        /*team filter spinner*/

        setUpSpinners();
//        String selectedTeamString = teamSpinner.getSelectedItem().toString();

        List<Team> teams=null;
        try {
            teams=teamFuture.get();
        }catch (InterruptedException ie){
            Log.e("User settings activity", " InterruptedException while getting teams");
        }catch (ExecutionException ee){
            Log.e("User settings activity"," ExecutionException while getting teams");
        }
        //Team selectedTeam = teams.stream().filter(c -> c.getName().equals(selectedTeamString)).findAny().orElseThrow(RuntimeException::new);

        /*state filter spinner*/

        Spinner taskState = findViewById(R.id.spinner_user);
        taskState.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                TaskState.values()));
        Button changeState = findViewById(R.id.changeStateButtonFilter);
        changeState.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("filterState",taskState.getSelectedItem().toString());
            editor.putString("filterTeam",teamSpinner.getSelectedItem().toString());
            editor.apply();
            Snackbar.make(findViewById(R.id.userSettingsActivity), "Tasks filtered", Snackbar.LENGTH_SHORT).show();
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_meneu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    private void setUpSpinners(){
        teamSpinner = (Spinner) findViewById(R.id.spinner_user2);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success ->
                {
                    Log.i("User settings activity", "Read Team Successfully");
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
                    Log.i("User settings activity", "Did not read teams successfully");
                }
        );
    }

}
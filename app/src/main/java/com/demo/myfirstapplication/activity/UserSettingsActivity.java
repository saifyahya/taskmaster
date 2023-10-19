package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.demo.myfirstapplication.R;
import com.google.android.material.snackbar.Snackbar;

public class UserSettingsActivity extends AppCompatActivity {
Button saveButton;
EditText username;
String usernameStr;
SharedPreferences sp;
public static final String USERNAME_TAG="username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        ImageView back = findViewById(R.id.backbutton);
        back.setOnClickListener(view ->  {
            Intent gobackFormIntent = new Intent(UserSettingsActivity.this, MainActivity.class);
            startActivity(gobackFormIntent);
        });

        saveButton= findViewById(R.id.buttonSaveUsername);
        username= findViewById(R.id.editTextUsername);
        //sp =getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE); //point to SP file incase of many SP
        sp= PreferenceManager.getDefaultSharedPreferences(this);
        saveButton.setOnClickListener(view ->  {

                usernameStr=username.getText().toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(USERNAME_TAG,usernameStr);
                editor.apply();
//                Toast.makeText(UserSettingsActivity.this,"Username Saved",Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.userSettingsActivity), "Username Saved", Snackbar.LENGTH_SHORT).show();  // the id represents the id of the layout
        });
    }

}
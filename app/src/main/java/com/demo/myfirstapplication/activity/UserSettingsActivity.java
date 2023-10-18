package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.myfirstapplication.R;

public class UserSettingsActivity extends AppCompatActivity {
Button saveButton;
EditText username;
String usernameStr;
SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        saveButton= findViewById(R.id.buttonSaveUsername);
        username= findViewById(R.id.editTextUsername);
        sp =getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameStr=username.getText().toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("name",usernameStr);
                editor.commit();
                Toast.makeText(UserSettingsActivity.this,"Username Saved",Toast.LENGTH_LONG).show();
            }
        });
    }

}
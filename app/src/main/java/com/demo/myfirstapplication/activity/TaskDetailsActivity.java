package com.demo.myfirstapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TaskDetailsActivity extends AppCompatActivity {
    Task retrievedTask;
    TextView taskTitle;
    TextView taskBody;
    TextView taskTeam;
    TextView taskEndDate;
    TextView taskState;
    TextView taskLocation;
    private String s3ImageKey = "";
    String cityName="";
    String countryName="";

    public static final String TAG ="retrievedTASK";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        init();
        displayTask();
        editTask();
        deleteTask();

    }



    private void updateUIWithTaskDetails(Task retrievedTask) {
        runOnUiThread(() ->{
            taskState = findViewById(R.id.stateView);
            settingTaskInfo(taskState,retrievedTask.getState().toString());
            taskTitle = findViewById(R.id.textViewTitle);
            settingTaskInfo(taskTitle,retrievedTask.getTitle());

            taskBody = findViewById(R.id.taskDetailsDescription);
            settingTaskInfo(taskBody,retrievedTask.getBody());

            taskTeam = findViewById(R.id.taskDetailsDescription2);
            settingTaskInfo(taskTeam,retrievedTask.getTeamPerson().getName());

            taskEndDate = findViewById(R.id.taskDateView);
            settingTaskInfo(taskEndDate,retrievedTask.getEndDate().format().split("T")[0]);

            taskLocation = findViewById(R.id.taskLocation);
            taskLocation.setText(cityName+" \n"+ countryName);
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

    private void   init(){

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView back = findViewById(R.id.backbutton);
        back.setOnClickListener(view ->  {
            Intent gobackFormIntent = new Intent(TaskDetailsActivity.this, MainActivity.class);
            startActivity(gobackFormIntent);
        });

    }
    private void displayTask(){
        /*Displaying Task Info*/
        Intent callingIntent = getIntent();
        if (callingIntent != null){
            String retrievedTaskId = callingIntent.getStringExtra("taskId");
            if(retrievedTaskId!=null){

                Amplify.API.query(
                        ModelQuery.get(Task.class, retrievedTaskId),
                        response -> {
                            retrievedTask = response.getData(); // the result (retrieved task) is asynronous, it will not block the code exection ad then return retrieved task through a callback
                            if (retrievedTask != null) {
                                getAddressFromLocation(TaskDetailsActivity.this,Double.valueOf( retrievedTask.getLocationLatitude()),Double.valueOf( retrievedTask.getLocationLongitude()));
                                updateUIWithTaskDetails(retrievedTask);
                                renderTaskImage();
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


            }
        }


    }
    private void deleteTask(){
        Button deleteTask = findViewById(R.id.deleteTask);
        deleteTask.setOnClickListener(view -> {
            Intent callingIntent = getIntent();
            String retrievedTaskId = callingIntent.getStringExtra("taskId");
            if(retrievedTaskId!=null){
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
//                Snackbar.make(findViewById(R.id.TaskDetailsLayout), "Task Removed", Snackbar.LENGTH_SHORT).show();
                Intent gobackFormIntent = new Intent(TaskDetailsActivity.this, MainActivity.class);
                startActivity(gobackFormIntent);
            }
        });
    }
    private void editTask(){
        /*updating Task State Part*/

        Button changeState = findViewById(R.id.changeStateButton);
        changeState.setOnClickListener(view -> {
            Intent callingIntent = getIntent();
            String retrievedTaskId = callingIntent.getStringExtra("taskId");
            Log.d(TAG, "Retrieved Task ID: " + retrievedTaskId);

            if(retrievedTaskId!=null) {
                Intent goToTaskDetails = new Intent(TaskDetailsActivity.this, EditTaskActivity.class);
                goToTaskDetails.putExtra("taskId",retrievedTaskId);
                startActivity(goToTaskDetails);

            }
        });
    }
    private void renderTaskImage() {
        // display task image
        Log.d(TAG, "retrievedTaskinside " + retrievedTask);

        s3ImageKey = retrievedTask.getImage();
        if (s3ImageKey != null && !s3ImageKey.isEmpty()) {
            Amplify.Storage.downloadFile(
                    s3ImageKey,
                    new File(getApplication().getFilesDir(), s3ImageKey),
                    success -> {
                        // Load a scaled-down version of the image
                        int targetWidth = 400; // Adjust the target width as needed
                        int targetHeight = 400; // Adjust the target height as needed

                        // Options for image resizing
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(success.getFile().getPath(), options);
                        int imageWidth = options.outWidth;
                        int imageHeight = options.outHeight;
                        int scaleFactor = Math.min(imageWidth / targetWidth, imageHeight / targetHeight);

                        options.inJustDecodeBounds = false;
                        options.inSampleSize = scaleFactor;

                        // Decode the scaled-down image
                        Bitmap resizedBitmap = BitmapFactory.decodeFile(success.getFile().getPath(), options);

                        // Set the resized bitmap to the ImageView
                        ImageView productImageView = findViewById(R.id.taskImage_taskDetails);
                        productImageView.setImageBitmap(resizedBitmap);
                    },
                    failure -> {
                        Log.e(TAG, "Unable to get image from S3 for the product for S3 key: " + s3ImageKey + " for reason: " + failure.getMessage());
                    }
            );
        }
    }
        private void getAddressFromLocation(Context context, double latitude, double longitude) {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);

                    // You can get various address details like city, country, etc.
                     cityName = address.getLocality();
                    countryName = address.getCountryName();

//                    return cityName + ", " + countryName;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

//            return null;
        }


}
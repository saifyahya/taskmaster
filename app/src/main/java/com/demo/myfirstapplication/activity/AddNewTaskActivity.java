package com.demo.myfirstapplication.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.OpenableColumns;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
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

    ActivityResultLauncher<Intent> activityResultLauncher;
    private String s3ImageKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        /*database connection*/
//        taskDatabase = DatabaseSingleton.getInstance(getApplicationContext());



        activityResultLauncher = getImagePickingActivityResultLauncher();  // You MUST set this up in onCreate() in the lifecycle




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
            saveTaskToDatabase(s3ImageKey);
        });

        ImageView back = findViewById(R.id.backbutton);
        back.setOnClickListener(view -> {
            Intent gobackFormIntent = new Intent(AddNewTaskActivity.this, MainActivity.class);
            startActivity(gobackFormIntent);
        });

        setupAddImageButton();
    }
    @Override
    protected void onResume() {
        super.onResume();

       //Intent Filter
        Intent callingIntent = getIntent();
        if(callingIntent != null && callingIntent.getType() != null && callingIntent.getType().startsWith("image") ){
            Uri incomingImageFileUri= callingIntent.getParcelableExtra(Intent.EXTRA_STREAM);

            if (incomingImageFileUri != null){
                InputStream incomingImageFileInputStream = null;
                Log.d(TAG, "incoming iamge uri"+incomingImageFileUri.toString());

                try {
                    incomingImageFileInputStream = getContentResolver().openInputStream(incomingImageFileUri);

                    ImageView productImageView = findViewById(R.id.taskImage);

                    if (productImageView != null) {
                        Log.d(TAG, "image input stream"+incomingImageFileInputStream.toString());
                        productImageView.setImageBitmap(BitmapFactory.decodeStream(incomingImageFileInputStream));
                    }else {
                        Log.e(TAG, "ImageView is null for some reasons");
                    }
                }catch (FileNotFoundException fnfe){
                    Log.e(TAG," Could not get file stream from the URI "+fnfe.getMessage(),fnfe);
                }
            }
        }
    }
//    private void setUpSaveButton()
//    {
//        Button saveButton = (Button)findViewById(R.id.editProductSaveButton);
//        saveButton.setOnClickListener(v ->
//        {
//            saveProduct(s3ImageKey);
//        });
//    }
    private ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher()
    {
        // Part 2: Create an image picking activity result launcher
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>()
                        {
                            @Override
                            public void onActivityResult(ActivityResult result)
                            {
                                if (result.getResultCode() == Activity.RESULT_OK)
                                {
                                    if (result.getData() != null)
                                    {
                                        Uri pickedImageFileUri = result.getData().getData();
                                        try
                                        {
                                            InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                                            String pickedImageFilename = getFileNameFromUri(pickedImageFileUri);
                                            Log.i(TAG, "Succeeded in getting input stream from file on phone! Filename is: " + pickedImageFilename);
                                            // Part 3: Use our InputStream to upload file to S3
                                            //switchFromAddButtonToDeleteButton(addImageButton);
                                            uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename,pickedImageFileUri);

                                        } catch (FileNotFoundException fnfe)
                                        {
                                            Log.e(TAG, "Could not get file from file picker! " + fnfe.getMessage(), fnfe);
                                        }
                                    }
                                }
                                else
                                {
                                    Log.e(TAG, "Activity result error in ActivityResultLauncher.onActivityResult");
                                }
                            }
                        }
                );

        return imagePickingActivityResultLauncher;
    }

    private void uploadInputStreamToS3(InputStream pickedImageInputStream, String pickedImageFilename,Uri pickedImageFileUri)
    {
        Amplify.Storage.uploadInputStream(
                pickedImageFilename,  // S3 key
                pickedImageInputStream,
                success ->
                {
                    Log.i(TAG, "Succeeded in getting file uploaded to S3! Key is: " + success.getKey());
                    // Part 4: Update/save our Product object to have an image key
                   // saveTaskToDatabase(success.getKey());
                    s3ImageKey=success.getKey();
                    //updateImageButtons();
                    ImageView productImageView = findViewById(R.id.taskImage);
                    InputStream pickedImageInputStreamCopy = null;  // need to make a copy because InputStreams cannot be reused!
                    try
                    {
                        pickedImageInputStreamCopy = getContentResolver().openInputStream(pickedImageFileUri);
                    }
                    catch (FileNotFoundException fnfe)
                    {
                        Log.e(TAG, "Could not get file stream from URI! " + fnfe.getMessage(), fnfe);
                    }
                    productImageView.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStreamCopy));
                },
                failure ->
                {
                    Log.e(TAG, "Failure in uploading file to S3 with filename: " + pickedImageFilename + " with error: " + failure.getMessage());
                }
        );
    }
    private void setupAddImageButton(){
        Button pickImage = findViewById(R.id.pickImageButton);
        pickImage.setOnClickListener(view -> {
            launchImageSelectionIntent();
        });
    }
    private void launchImageSelectionIntent()
    {
        // Part 1: Launch activity to pick file

        Intent imageFilePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickingIntent.setType("*/*");  // only allow one kind or category of file; if you don't have this, you get a very cryptic error about "No activity found to handle Intent"
        imageFilePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png"});
        // Below is simple version for testing
        //startActivity(imageFilePickingIntent);

        // Part 2: Create an image picking activity result launcher
        activityResultLauncher.launch(imageFilePickingIntent);

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

    private void saveTaskToDatabase(String s3ImageKey) {
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
                .image(s3ImageKey)
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

    // Taken from https://stackoverflow.com/a/25005243/16889809
    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
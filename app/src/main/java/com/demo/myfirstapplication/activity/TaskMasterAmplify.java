package com.demo.myfirstapplication.activity;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;

public class TaskMasterAmplify extends Application {
    public static final String TAG = "taskMasterApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException ae) {
            Log.e(TAG, "Error initializing Amplify" + ae.getMessage(), ae);
        }
    }

}
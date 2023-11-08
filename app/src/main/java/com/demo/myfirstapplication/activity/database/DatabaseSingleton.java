//package com.demo.myfirstapplication.activity.database;
//
//import android.content.Context;
//
//import androidx.room.Room;
//
//public class DatabaseSingleton {
//    private static TaskDatabase instance;
//    public static final String DATABASE_TAG="taskDatabase";
//
//    public static TaskDatabase getInstance(Context context) {
//        if (instance == null) {
//            synchronized (DatabaseSingleton.class) {
//                if (instance == null) {
//                    instance = Room.databaseBuilder(context.getApplicationContext(),
//                                    TaskDatabase.class, DATABASE_TAG)
//                            .fallbackToDestructiveMigration()
//                            .allowMainThreadQueries()
//                            .build();
//                }
//            }
//        }
//        return instance;
//    }
//}
//

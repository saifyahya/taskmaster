package com.demo.myfirstapplication.activity.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.demo.myfirstapplication.activity.dao.TaskDAO;
import com.demo.myfirstapplication.activity.models.Task;

@Database(entities = {Task.class},version = 3)
@TypeConverters(DatabaseConverter.class)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDAO taskDAO();
}

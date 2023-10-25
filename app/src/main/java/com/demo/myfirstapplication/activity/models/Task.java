package com.demo.myfirstapplication.activity.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.demo.myfirstapplication.activity.enums.TaskState;

import java.util.Date;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String title;
    private String body;
    private TaskState state;
    private Date endDate;

    public Task(String title, String body, TaskState state,Date endDate) {
        this.title = title;
        this.body = body;
        this.state = state;
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public TaskState getState() {
        return state;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setState(TaskState state) {
        this.state = state;
    }
}

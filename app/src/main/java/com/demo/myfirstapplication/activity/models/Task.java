package com.demo.myfirstapplication.activity.models;

import com.demo.myfirstapplication.activity.enums.TaskState;

public class Task {
    private String title;
    private String body;
    private TaskState state;

    public Task(String title, String body, TaskState state) {
        this.title = title;
        this.body = body;
        this.state = state;
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

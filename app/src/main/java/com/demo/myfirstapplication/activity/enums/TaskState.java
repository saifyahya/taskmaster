package com.demo.myfirstapplication.activity.enums;

import androidx.annotation.NonNull;

public enum TaskState {
    NEW("New"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    COMPLETE("Complete");

    private final String TASK_TEXT;

    TaskState(String taskText) {
        TASK_TEXT = taskText;
    }

    public String getTASK_TEXT() {
        return TASK_TEXT;
    }

    public static TaskState fromString(String possibleText) {
        for (TaskState state : TaskState.values()) {
            if (state.getTASK_TEXT().equals(possibleText)) {
                return state;
            }
        }
        return null;
    }

    @NonNull
    @Override
    public String toString(){
        if(TASK_TEXT == null){
            return "";
        }
        return TASK_TEXT;
    }
}

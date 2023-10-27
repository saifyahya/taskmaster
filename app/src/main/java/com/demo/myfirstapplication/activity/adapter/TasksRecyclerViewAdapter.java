package com.demo.myfirstapplication.activity.adapter;

import static com.demo.myfirstapplication.activity.MainActivity.TASK_TAG;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.myfirstapplication.R;
import com.demo.myfirstapplication.activity.TaskDetailsActivity;
import com.demo.myfirstapplication.activity.models.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.TaskHolder> {

    List<Task> taskList;
    Context callingActivity;
    public TasksRecyclerViewAdapter(List<Task> taskList, Context callingActivity) {
        this.taskList = taskList;
        this.callingActivity= callingActivity;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task_list,parent,false);
        return new TaskHolder(taskFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        TextView taskFragment = (TextView) holder.itemView.findViewById(R.id.fragmentTextView);
        String taskName= taskList.get(position).getTitle();
        String taskEndDate = dateToIso8601(taskList.get(position).getEndDate());
        String taskState =  taskList.get(position).getState().getTASK_TEXT();
        Long taskId = taskList.get(position).getId();
        taskFragment.setText(position+". "+taskName+"\n" +"Due date: "+taskEndDate+ " Status: "+taskState);

        View taskViewHolder=holder.itemView;
        taskViewHolder.setOnClickListener(view -> {
            Intent goToTaskDetails = new Intent(callingActivity, TaskDetailsActivity.class);
            goToTaskDetails.putExtra("taskId",taskId);

            callingActivity.startActivity(goToTaskDetails);
        });
    }
    private String dateToIso8601(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder{

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

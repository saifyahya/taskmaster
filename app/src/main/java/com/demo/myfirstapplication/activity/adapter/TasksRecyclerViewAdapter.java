package com.demo.myfirstapplication.activity.adapter;

import static com.demo.myfirstapplication.activity.MainActivity.TASK_TAG;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.myfirstapplication.R;
import com.demo.myfirstapplication.activity.TaskDetailsActivity;
import com.demo.myfirstapplication.activity.models.Task;

import java.util.List;

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
        taskFragment.setText(position+". "+taskName);

        View taskViewHolder=holder.itemView;
        taskViewHolder.setOnClickListener(view -> {
            Intent goToTaskDetails = new Intent(callingActivity, TaskDetailsActivity.class);
            goToTaskDetails.putExtra(TASK_TAG,taskName);
            callingActivity.startActivity(goToTaskDetails);
        });
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

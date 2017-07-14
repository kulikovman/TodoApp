package ru.kulikovman.todoapp;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.kulikovman.todoapp.models.Task;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private List<Task> mTasks;

    public static class TaskHolder extends RecyclerView.ViewHolder {
        private TextView mTaskTitle;

        private Task mTask;

        public TaskHolder(View v) {
            super(v);
            mTaskTitle = (TextView) v.findViewById(R.id.item_task_title);
        }

        public void bindTask(Task task) {
            mTask = task;
            mTaskTitle.setText(mTask.getTitle());
        }
    }

    public TaskAdapter(List<Task> tasks) {
        mTasks = tasks;
    }

    @Override
    public TaskAdapter.TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskHolder(v);
    }

    @Override
    public void onBindViewHolder(TaskHolder holder, int position) {
        Task task = mTasks.get(position);
        holder.bindTask(task);
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }
}
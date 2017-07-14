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
    public boolean[] mSelects;

    public static class TaskHolder extends RecyclerView.ViewHolder {
        public TextView mTaskTitle;

        public TaskHolder(View v) {
            super(v);
            mTaskTitle = (TextView) v.findViewById(R.id.item_task_title);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = getLayoutPosition();
                }
            });
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
        holder.mTaskTitle.setText(mTasks.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }
}
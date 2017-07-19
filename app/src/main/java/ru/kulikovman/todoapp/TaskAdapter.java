package ru.kulikovman.todoapp;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.kulikovman.todoapp.models.Task;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    public static OnItemClickListener mListener;
    private List<Task> mTasks;

    public static class TaskHolder extends RecyclerView.ViewHolder {
        private TextView mTaskTitle, mTaskDate, mTaskPriority, mTaskRepeat;
        private ImageButton mTaskColor;
        private Task mTask;

        public TaskHolder(View view) {
            super(view);
            mTaskTitle = (TextView) view.findViewById(R.id.item_task_title);
            mTaskDate = (TextView) view.findViewById(R.id.item_task_date);
            mTaskPriority = (TextView) view.findViewById(R.id.item_task_priority);
            mTaskRepeat = (TextView) view.findViewById(R.id.item_task_repeat);
            mTaskColor = (ImageButton) view.findViewById(R.id.item_task_color);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TaskAdapter.mListener != null) {
                        TaskAdapter.mListener.onItemClick(v, getLayoutPosition(), mTask);
                    }
                }
            });
        }

        public void bindTask(Task task) {
            mTask = task;

            // Получаем значения полей
            String taskTitle = mTask.getTitle();
            String taskDate = mTask.getDate();
            String taskPriority = mTask.getPriority();
            String taskRepeat = mTask.getRepeat();
            String taskColor = mTask.getColor();

            Log.d("myLog", taskTitle + " | " +
                    taskDate + " | " +
                    taskPriority + " | " +
                    taskColor);

            // Устанавливаем заголовок
            mTaskTitle.setText(taskTitle);

            // Устанавливаем дату
            if (!taskDate.equals("Не установлена")) {
                long dateLong = Long.parseLong(taskDate);
                Date date = new Date(dateLong);
                DateFormat dateFormat = new SimpleDateFormat("dd MMMM", Locale.getDefault());
                taskDate = dateFormat.format(date);

                mTaskDate.setText(taskDate);
            }

            // Устанавливаем приоритет
            switch (taskPriority) {
                case "0":
                    mTaskPriority.setText("Чрезвычайный");
                    break;
                case "1":
                    mTaskPriority.setText("Высокий");
                    break;
                case "2":
                    mTaskPriority.setText("Обычный");
                    break;
                case "3":
                    mTaskPriority.setText("Низкий");
                    break;
                case "4":
                    mTaskPriority.setText("Самый низкий");
                    break;
            }

            // Устанавливаем повтор
            mTaskRepeat.setText(taskRepeat);

            // Устанавливаем цвет
            switch (taskColor) {
                case "Красный":
                    mTaskColor.setBackgroundResource(R.color.red);
                    break;
                case "Оранжевый":
                    mTaskColor.setBackgroundResource(R.color.orange);
                    break;
                case "Желтый":
                    mTaskColor.setBackgroundResource(R.color.yellow);
                    break;
                case "Зеленый":
                    mTaskColor.setBackgroundResource(R.color.green);
                    break;
                case "Синий":
                    mTaskColor.setBackgroundResource(R.color.blue);
                    break;
                case "Фиолетовый":
                    mTaskColor.setBackgroundResource(R.color.violet);
                    break;
                case "Розовый":
                    mTaskColor.setBackgroundResource(R.color.pink);
                    break;
                case "Без цвета":
                    mTaskColor.setBackgroundResource(R.color.transparent);
                    break;
            }
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

    public void setTasks(List<Task> tasks) {
        mTasks = tasks;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position, Task task);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
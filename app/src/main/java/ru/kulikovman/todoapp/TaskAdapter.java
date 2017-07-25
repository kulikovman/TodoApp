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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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


            // Обнуляем все текстовые поля
            mTaskTitle.setText(null);
            mTaskDate.setText(null);
            mTaskPriority.setText(null);
            mTaskRepeat.setText(null);


            // Получаем значения полей
            String taskTitle = mTask.getTitle();
            String taskDate = mTask.getDate();
            String taskPriority = mTask.getPriority();
            String taskRepeat = mTask.getRepeat();
            String taskColor = mTask.getColor();


            // Устанавливаем заголовок
            mTaskTitle.setText(taskTitle);


            // Устанавливаем дату
            if (!taskDate.equals("Не установлена")) {
                long dateLong = Long.parseLong(taskDate);
                Date date = new Date(dateLong);

                Calendar targetDate = new GregorianCalendar();
                targetDate.setTime(date);
                int targetYear = targetDate.get(Calendar.YEAR);

                Calendar currentDate = Calendar.getInstance();
                int currentYear = currentDate.get(Calendar.YEAR);

                DateFormat dateFormatShort = new SimpleDateFormat("dd MMMM", Locale.getDefault());
                DateFormat dateFormatLong = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

                if (targetYear == currentYear) {
                    taskDate = dateFormatShort.format(date);
                } else {
                    taskDate = dateFormatLong.format(date);
                }

                mTaskDate.setText(taskDate);
            }


            // Устанавливаем приоритет
            switch (taskPriority) {
                case "0":
                    mTaskPriority.setText(R.string.priority_0);
                    break;
                case "1":
                    mTaskPriority.setText(R.string.priority_1);
                    break;
                case "3":
                    mTaskPriority.setText(R.string.priority_3);
                    break;
                case "4":
                    mTaskPriority.setText(R.string.priority_4);
                    break;
            }


            // Устанавливаем повтор
            if (!taskRepeat.equals("Без повтора")) {
                mTaskRepeat.setText(taskRepeat);
            }


            // Устанавливаем цвет
            switch (taskColor) {
                case "8_not_set":
                    mTaskColor.setBackgroundResource(R.color.gray_2);
                    break;
                case "1_red":
                    mTaskColor.setBackgroundResource(R.color.red);
                    break;
                case "2_orange":
                    mTaskColor.setBackgroundResource(R.color.orange);
                    break;
                case "3_yellow":
                    mTaskColor.setBackgroundResource(R.color.yellow);
                    break;
                case "4_green":
                    mTaskColor.setBackgroundResource(R.color.green);
                    break;
                case "5_blue":
                    mTaskColor.setBackgroundResource(R.color.blue);
                    break;
                case "6_violet":
                    mTaskColor.setBackgroundResource(R.color.violet);
                    break;
                case "7_pink":
                    mTaskColor.setBackgroundResource(R.color.pink);
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

    //Здесь мы добавляем элемент в набор данных
    public void addItem(int position, Task task){
        mTasks.add(position, task);
        super.notifyItemInserted(position);
    }

    //А здесь - удаляем
    public void deleteItem(int position){
        mTasks.remove(position);
        super.notifyItemRemoved(position);
    }
}
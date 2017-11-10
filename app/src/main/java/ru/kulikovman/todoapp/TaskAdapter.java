package ru.kulikovman.todoapp;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import ru.kulikovman.todoapp.models.Task;

import static ru.kulikovman.todoapp.Helper.convertLongToLongTextDate;
import static ru.kulikovman.todoapp.Helper.convertLongToShortTextDate;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private static OnItemClickListener mListener;
    private Context mContext;
    private List<Task> mTasks;

    private int mPosition = RecyclerView.NO_POSITION;

    public class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTaskTitle, mTaskDate, mTaskPriority, mTaskRepeat;
        private ImageButton mTaskColor;
        private ImageView mTaskWarning;
        private Task mTask;

        public TaskHolder(View view) {
            super(view);

            // Слушатель нажатий по элементу
            view.setOnClickListener(this);

            // Инициализируем вью элемента списка
            mTaskTitle = (TextView) view.findViewById(R.id.item_task_title);
            mTaskDate = (TextView) view.findViewById(R.id.item_task_date);
            mTaskPriority = (TextView) view.findViewById(R.id.item_task_priority);
            mTaskRepeat = (TextView) view.findViewById(R.id.item_task_repeat);
            mTaskColor = (ImageButton) view.findViewById(R.id.item_group_color);
            mTaskWarning = (ImageView) view.findViewById(R.id.item_task_warning);
        }

        @Override
        public void onClick(View v) {
            // Обновляем айтем нажатый ранее
            notifyItemChanged(mPosition);

            // Сохраняем старую и получаем новую позицию
            int oldPosition = mPosition;
            mPosition = getLayoutPosition();

            // Если старая и новая позиции совпадают, то удаляем позицию
            if (mPosition == oldPosition) {
                resetSelection();
            }

            // Обновляем айтем нажатый сейчас
            notifyItemChanged(mPosition);

            // Код для проброса слушателя
            if (TaskAdapter.mListener != null) {
                TaskAdapter.mListener.onItemClick(v, getLayoutPosition(), mTask, mPosition);
            }
        }

        // Назначаем содержимое для текущего элемента списка
        public void bindTask(Task task) {
            mTask = task;

            // Устанавливаем название задачи
            mTaskTitle.setText(task.getTitle());

            // Если задача завершена, то делаем ее светло-серой
            if (task.isDone()) {
                mTaskTitle.setTextColor(ContextCompat.getColor(mContext, R.color.gray_4);
            }

            // Устанавливаем дату
            long date = task.getTargetDate();

            if (date != 0) {
                // Получаем год даты задачи и текущий год
                Calendar targetDate = new GregorianCalendar();
                targetDate.setTimeInMillis(date);
                int targetYear = targetDate.get(Calendar.YEAR);

                Calendar currentDate = Calendar.getInstance();
                int currentYear = currentDate.get(Calendar.YEAR);

                // Сравниваем года и устанавливаем дату в нужном формате
                if (targetDate == currentDate) {
                    mTaskDate.setText(convertLongToShortTextDate(date));
                } else {
                    mTaskDate.setText(convertLongToLongTextDate(date));
                }
            }

            // Устанавливаем приоритет





            switch (taskPriority) {
                case "0":
                    mTaskPriority.setText(R.string.priority_emergency);
                    break;
                case "1":
                    mTaskPriority.setText(R.string.priority_high);
                    break;
                case "3":
                    mTaskPriority.setText(R.string.priority_low);
                    break;
                case "4":
                    mTaskPriority.setText(R.string.priority_lowest);
                    break;
            }



















            // Это предыдущая версия кода
            //
            //
            // Обнуляем все текстовые поля
            //int defaultColor = mContext.getResources().getColor(R.color.gray_7);

            mTaskTitle.setText(null);
            mTaskDate.setText(null);
            mTaskPriority.setText(null);
            mTaskRepeat.setText(null);

            // Получаем значения полей
            String taskTitle = mTask.getTitle();
            String taskDate = mTask.getTargetDate();
            String taskPriority = mTask.getPriority();
            String taskRepeat = mTask.getRepeatDate();
            String taskColor = mTask.getColor();





            // Устанавливаем приоритет
            switch (taskPriority) {
                case "0":
                    mTaskPriority.setText(R.string.priority_emergency);
                    break;
                case "1":
                    mTaskPriority.setText(R.string.priority_high);
                    break;
                case "3":
                    mTaskPriority.setText(R.string.priority_low);
                    break;
                case "4":
                    mTaskPriority.setText(R.string.priority_lowest);
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

    public TaskAdapter(Context context, List<Task> tasks) {
        mTasks = tasks;
        mContext = context;
    }

    @Override
    public TaskAdapter.TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskHolder(v);
    }

    @Override
    public void onBindViewHolder(final TaskHolder holder, int position) {
        Task task = mTasks.get(position);
        holder.bindTask(task);

        // Если установленная позиция равна текущей, то делаем элемент "нажатым"
        holder.itemView.setPressed(mPosition == position);

        // Запасной вариант - выделение выбранным цветом, но без риппл эффекта
        //holder.itemView.setBackgroundColor(mPosition == position ? Color.GREEN : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public void setTasks(List<Task> tasks) {
        mTasks = tasks;
    }

    // Интерфейс для проброса слушателя наружу
    public interface OnItemClickListener {
        void onItemClick(View itemView, int itemPosition, Task task, int selectedPosition);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // Добавляем элемент в набор данных
    public void addItem(int position, Task task) {
        mTasks.add(position, task);
        super.notifyItemInserted(position);
    }

    // Удаляем элемент из набора данных
    public void deleteItem(int position) {
        mTasks.remove(position);
        super.notifyItemRemoved(position);
    }

    public void resetSelection() {
        mPosition = RecyclerView.NO_POSITION;   //NO_POSITION == -1
    }
}
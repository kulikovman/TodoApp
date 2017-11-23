package ru.kulikovman.todoapp.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ru.kulikovman.todoapp.Helper;
import ru.kulikovman.todoapp.R;
import ru.kulikovman.todoapp.models.Task;

import static android.view.ViewGroup.*;
import static ru.kulikovman.todoapp.Helper.convertLongToShortTextDate;
import static ru.kulikovman.todoapp.Helper.convertLongToLongTextDate;

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
            mTaskColor = (ImageButton) view.findViewById(R.id.item_task_color);
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

            // Устанавливаем состояние айтема по умолчанию
            defaultStateItem();

            // Устанавливаем название задачи
            mTaskTitle.setText(task.getTitle());

            // Если задача завершена, то делаем ее светло-серой
            if (task.isDone()) {
                mTaskTitle.setTextColor(ContextCompat.getColor(mContext, R.color.gray_4));
            }

            // Устанавливаем дату
            long targetDate = task.getTargetDate();

            if (targetDate != 0) {
                // Получаем год даты задачи и текущий год
                Calendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(targetDate);
                int targetYear = calendar.get(Calendar.YEAR);

                int currentYear = Calendar.getInstance().get(Calendar.YEAR);

                // Сравниваем года и записываем дату в нужном формате
                if (targetYear == currentYear) {
                    mTaskDate.setText(convertLongToLongTextDate(targetDate));
                } else {
                    mTaskDate.setText(convertLongToShortTextDate(targetDate));
                }
            }

            // Устанавливаем приоритет
            int priority = task.getPriority();

            if (priority != 2) {
                if (priority == 0) {
                    mTaskPriority.setText(R.string.priority_emergency);
                } else if (priority == 1) {
                    mTaskPriority.setText(R.string.priority_high);
                } else if (priority == 3) {
                    mTaskPriority.setText(R.string.priority_low);
                } else if (priority == 4) {
                    mTaskPriority.setText(R.string.priority_lowest);
                }
            }

            // Устанавливаем повтор
            String repeat = task.getRepeatDate();

            if (repeat != null) {
                switch (repeat) {
                    case "day":
                        mTaskRepeat.setText(R.string.repeat_day);
                        break;
                    case "week":
                        mTaskRepeat.setText(R.string.repeat_week);
                        break;
                    case "month":
                        mTaskRepeat.setText(R.string.repeat_month);
                        break;
                    case "year":
                        mTaskRepeat.setText(R.string.repeat_year);
                        break;
                }
            }

            // Получаем название цвета и закрашиваем ярлычок
            if (task.getGroup() != null) {
                String color = task.getGroup().getColor();

                if (color != null) {
                    // Получаем id цвета из его названия
                    int colorId = mContext.getResources().getIdentifier(color, "color", mContext.getPackageName());
                    mTaskColor.setBackgroundResource(colorId);
                }
            }

            // Показываем иконку предупреждения
            Calendar taskDate = Helper.convertLongToCalendar(task.getTargetDate());
            Calendar todayDate = Helper.getTodayRoundCalendar();

            int daysBeforeTaskDate = (int) ((taskDate.getTimeInMillis() - todayDate.getTimeInMillis()) / 1000 / 60 / 60 / 24);
            Log.d("log", "Разница в днях: " + daysBeforeTaskDate);

            String reminder = task.getReminderDate();

            if (reminder != null || daysBeforeTaskDate < 0) {
                // Делаем иконку видимой
                mTaskWarning.setVisibility(View.VISIBLE);

                // Двигаем вправо до начала заголовка задачи
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTaskWarning.getLayoutParams();
                params.setMarginStart(Helper.convertDpToPx(mContext, 50)); // пиксели...
                mTaskWarning.setLayoutParams(params);

                // Если дата просрочена, то меняем иконку
                if (daysBeforeTaskDate < 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mTaskWarning.setImageResource(R.drawable.ic_error_outline_24dp);
                    } else {
                        mTaskWarning.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_error_outline_24dp));
                    }
                }
            }
        }

        private void defaultStateItem() {
            // Обнуляем все поля
            mTaskTitle.setText(null);
            mTaskDate.setText(null);
            mTaskPriority.setText(null);
            mTaskRepeat.setText(null);

            // Цвет ярлычка по умолчанию
            mTaskColor.setBackgroundResource(R.color.gray_2);

            // Прячем и сдвигаем иконку предупреждения
            mTaskWarning.setVisibility(View.INVISIBLE);

            /*MarginLayoutParams params = (MarginLayoutParams) mTaskWarning.getLayoutParams();
            params.setMargins(50, params.topMargin, params.rightMargin, params.bottomMargin);*/

            // Устанавливаем иконку предупреждения по умолчанию
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mTaskWarning.setImageResource(R.drawable.ic_notifications_outline_24dp);
            } else {
                mTaskWarning.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_notifications_outline_24dp));
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
        holder.itemView.setSelected(mPosition == position);
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
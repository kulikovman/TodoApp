package ru.kulikovman.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import ru.kulikovman.todoapp.database.DbHelper;
import ru.kulikovman.todoapp.dialogs.ColorFragment;
import ru.kulikovman.todoapp.dialogs.DateFragment;
import ru.kulikovman.todoapp.dialogs.PriorityFragment;
import ru.kulikovman.todoapp.dialogs.RepeatFragment;
import ru.kulikovman.todoapp.models.Group;
import ru.kulikovman.todoapp.models.Task;

public class EditTaskActivity extends AppCompatActivity {
    private DbHelper mDbHelper;
    private Task mTask;

    private EditText mTaskTitle;
    private TextView mDateState, mPriorityState, mGroupState, mRepeatState, mReminderState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Инициализируем необходимые вью элементы
        mTaskTitle = (EditText) findViewById(R.id.task_title);
        mDateState = (TextView) findViewById(R.id.date_state);
        mPriorityState = (TextView) findViewById(R.id.priority_state);
        mGroupState = (TextView) findViewById(R.id.group_state);
        mRepeatState = (TextView) findViewById(R.id.repeat_state);
        mReminderState = (TextView) findViewById(R.id.reminder_state);

        // Подключаем базу данных
        mDbHelper = new DbHelper(this);

        // Читаем uuid из интента
        UUID uuid = (java.util.UUID) getIntent().getSerializableExtra("task_uuid");

        // Если uuid не пустой, то получаем задачу и обновляем поля
        if (uuid != null) {
            mTask = mDbHelper.getTaskByUUID(uuid);
            loadTask();
        }
    }

    public void taskOptions(View view) {
        // Вызываем диалоги с выбором соответствующих опций
        switch (view.getId()) {
            case R.id.date_layout:
                DialogFragment dateFragment = new DateFragment();
                dateFragment.show(getSupportFragmentManager(), "dateFragment");
                break;

            case R.id.priority_layout:
                DialogFragment priorityFragment = new PriorityFragment();
                priorityFragment.show(getSupportFragmentManager(), "priorityFragment");
                break;

            case R.id.color_layout:
                DialogFragment colorFragment = new ColorFragment();
                colorFragment.show(getSupportFragmentManager(), "colorFragment");
                break;

            case R.id.repeat_layout:
                DialogFragment repeatFragment = new RepeatFragment();
                repeatFragment.show(getSupportFragmentManager(), "repeatFragment");
                break;
        }
    }

    private void loadTask() {
        // Устанавливаем заголовок
        mTaskTitle.setText(mTask.getTitle());

        // Устанавливаем дату
        long targetDate = mTask.getTargetDate();

        if (targetDate == 0) {
            mDateState.setText(getString(R.string.date_without));
        } else {
            mDateState.setText(convertLongDateToText(targetDate));
        }

        // Устанавливаем приоритет
        int priority = mTask.getPriority();

        if (priority == 0) {
            mPriorityState.setText(getString(R.string.priority_emergency));
        } else if (priority == 1) {
            mPriorityState.setText(getString(R.string.priority_high));
        } else if (priority == 2) {
            mPriorityState.setText(getString(R.string.priority_common));
        } else if (priority == 3) {
            mPriorityState.setText(getString(R.string.priority_low));
        } else if (priority == 4) {
            mPriorityState.setText(getString(R.string.priority_lowest));
        }

        // Устанавливаем группу
        Group group = mTask.getGroup();

        if (group == null) {
            mGroupState.setText(getString(R.string.group_without));
        } else {
            mGroupState.setText(group.getName());
        }

        // Устанавливаем повтор
        String repeat = mTask.getRepeatDate();

        if (repeat == null) {
            mRepeatState.setText(getString(R.string.repeat_not));
        } else if (repeat.equals("day")) {
            mRepeatState.setText(getString(R.string.repeat_day));
        } else if (repeat.equals("week")) {
            mRepeatState.setText(getString(R.string.repeat_week));
        } else if (repeat.equals("month")) {
            mRepeatState.setText(getString(R.string.repeat_month));
        } else if (repeat.equals("year")) {
            mRepeatState.setText(getString(R.string.repeat_year));
        }

        // Устанавливаем напоминание
        long reminderDate = mTask.getReminderDate();

        if (reminderDate == 0) {
            mReminderState.setText(getString(R.string.reminder_without));
        } else {
            mDateState.setText(convertLongDateToText(reminderDate));
        }
    }

    public String convertLongDateToText(long sourceDate) {
        Date date = new Date(sourceDate);
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        return dateFormat.format(date);
    }

    public long convertTextDateToLong(String sourceDate) {
        long date = 0;
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

        try {
            date = dateFormat.parse(sourceDate).getTime();
        } catch (ParseException ignored) {
        }

        return date;
    }

    public void saveTask(View view) {
        // Получаем заголовок задачи
        String title = mTaskTitle.getText().toString().trim();

        // Если заголовок есть, то делаем все остальное
        if (title.length() > 0) {
            // Создаем задачу
            Task task = new Task(title);

            // Получаем дату
            String date = mDateState.getText().toString().trim();

            if (!date.equals(getString(R.string.date_without))) {
                task.setTargetDate(convertTextDateToLong(date));
            }

            // Получаем приоритет
            String priority = mPriorityState.getText().toString().trim();

            if (priority.equals(getString(R.string.priority_emergency))) {
                task.setPriority(0);
            } else if (priority.equals(getString(R.string.priority_high))) {
                task.setPriority(1);
            } else if (priority.equals(getString(R.string.priority_common))) {
                task.setPriority(2);
            } else if (priority.equals(getString(R.string.priority_low))) {
                task.setPriority(3);
            } else if (priority.equals(getString(R.string.priority_lowest))) {
                task.setPriority(4);
            }

            // Получаем группу
            String group = mGroupState.getText().toString().trim();

            if (!group.equals(getString(R.string.group_without))) {
                task.setGroup(mDbHelper.getGroupByName(group));
            }

            // Получаем повтор
            String repeat = mRepeatState.getText().toString().trim();

            if (!repeat.equals(getString(R.string.repeat_without))) {
                if (!repeat.equals(getString(R.string.repeat_day))) {
                    task.setRepeatDate("day");
                } else if (!repeat.equals(getString(R.string.repeat_week))) {
                    task.setRepeatDate("week");
                } else if (!repeat.equals(getString(R.string.repeat_month))) {
                    task.setRepeatDate("month");
                } else if (!repeat.equals(getString(R.string.repeat_year))) {
                    task.setRepeatDate("year");
                }
            }

            // Получаем напоминание










            // Сохраняем задачу в базу
            if (mTask == null) {
                mTask = new Task(mTitle, mDate, mPriority, mColor, mRepeat);

                mDbHelper.addTask(mTask);
            } else {
                mTask.setTitle(mTitle);
                mTask.setTargetDate(mDate);
                mTask.setPriority(mPriority);
                mTask.setColor(mColor);
                mTask.setRepeatDate(mRepeat);

                mDbHelper.updateTask(mTask);
            }

            // Удаляем текущий активити из стека и возвращаемся в список задач
            Intent intent = new Intent(this, TaskListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


}

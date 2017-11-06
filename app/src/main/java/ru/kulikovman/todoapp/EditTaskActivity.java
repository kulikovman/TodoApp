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

    //private String mTitle, mDate, mPriority, mColor, mRepeat = "";

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
        UUID uuid = (java.util.UUID) getIntent().getSerializableExtra("task_id");

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
        // Подготовка
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());


        // Устанавливаем заголовок
        mTaskTitle.setText(mTask.getTitle());

        // Устанавливаем дату
        long targetDate = mTask.getTargetDate();

        if (targetDate != 0) {
            Date date = new Date(targetDate);
            mDateState.setText(dateFormat.format(date));
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

        if (group != null) {
            mGroupState.setText(group.getName());
        } else {
            mGroupState.setText(getString(R.string.group_not_set));
        }

        // Устанавливаем повтор




        mRepeatState.setText(mRepeat);












    }

    public void saveTask(View view) {
        // Получаем заголовок
        mTitle = mTaskTitle.getText().toString().trim();

        // Если заголовок есть, то делаем все остальное
        if (!mTitle.equals("")) {
            // Получаем дату
            mDate = mDateState.getText().toString();
            DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            try {
                mDate = String.valueOf(dateFormat.parse(mDate).getTime());
            } catch (ParseException ignored) {
            }

            // Получаем приоритет
            mPriority = mPriorityState.getText().toString();
            switch (mPriority) {
                case "Чрезвычайный":
                    mPriority = "0";
                    break;
                case "Высокий":
                    mPriority = "1";
                    break;
                case "Обычный":
                    mPriority = "2";
                    break;
                case "Низкий":
                    mPriority = "3";
                    break;
                case "Самый низкий":
                    mPriority = "4";
                    break;
            }

            // Получаем цвет
            mColor = mColorState.getText().toString();
            switch (mColor) {
                case "Не выбран":
                    mColor = "8_not_set";
                    break;
                case "Красный":
                    mColor = "1_red";
                    break;
                case "Оранжевый":
                    mColor = "2_orange";
                    break;
                case "Желтый":
                    mColor = "3_yellow";
                    break;
                case "Зеленый":
                    mColor = "4_green";
                    break;
                case "Синий":
                    mColor = "5_blue";
                    break;
                case "Фиолетовый":
                    mColor = "6_violet";
                    break;
                case "Розовый":
                    mColor = "7_pink";
                    break;
            }

            // Получаем повтор
            mRepeat = mRepeatState.getText().toString();

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

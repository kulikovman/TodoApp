package ru.kulikovman.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import ru.kulikovman.todoapp.database.TodoBaseHelper;
import ru.kulikovman.todoapp.dialogs.ColorFragment;
import ru.kulikovman.todoapp.dialogs.DateFragment;
import ru.kulikovman.todoapp.dialogs.PriorityFragment;
import ru.kulikovman.todoapp.dialogs.RepeatFragment;
import ru.kulikovman.todoapp.models.Task;

public class TaskActivity extends AppCompatActivity {
    private TodoBaseHelper mDbHelper;
    private Task mTask;

    private EditText mTitleField;
    private TextView mDateField, mPriorityField, mColorField, mRepeatField;
    private String mTitle, mDate, mPriority, mColor, mRepeat = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitleField = (EditText) findViewById(R.id.task_title);
        mDateField = (TextView) findViewById(R.id.date_field);
        mPriorityField = (TextView) findViewById(R.id.priority_field);
        mColorField = (TextView) findViewById(R.id.color_field);
        mRepeatField = (TextView) findViewById(R.id.repeat_field);

        mDbHelper = new TodoBaseHelper(this);
        UUID uuid = (java.util.UUID) getIntent().getSerializableExtra("task_id");

        if (uuid != null) {
            mTask = mDbHelper.getTaskByUUID(uuid);
            readTask();
        }

        Log.d("myLog", "Активити успешно запущен");
    }

    public void taskOptions(View view) {
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

    public void fabAddTask(View view) {
        // Получаем заголовок
        mTitle = mTitleField.getText().toString();

        // Если заголовок есть, то делаем все остальное
        if (!mTitle.equals("")) {
            // Получаем дату
            mDate = mDateField.getText().toString();
            DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            try {
                mDate = String.valueOf(dateFormat.parse(mDate).getTime());
            } catch (ParseException ignored) {
            }

            // Получаем приоритет
            mPriority = mPriorityField.getText().toString();
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
            mColor = mColorField.getText().toString();
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
            mRepeat = mRepeatField.getText().toString();

            // Сохраняем задачу в базу
            if (mTask == null) {
                mTask = new Task(mTitle, mDate, mPriority, mColor, mRepeat);

                mDbHelper.addTask(mTask);
            } else {
                mTask.setTitle(mTitle);
                mTask.setDate(mDate);
                mTask.setPriority(mPriority);
                mTask.setColor(mColor);
                mTask.setRepeat(mRepeat);

                mDbHelper.updateTask(mTask);
            }

            // Возвращаемся в список задач
            Intent intent = new Intent(this, TodoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void readTask() {
        mTitle = mTask.getTitle();
        mDate = mTask.getDate();
        mPriority = mTask.getPriority();
        mColor = mTask.getColor();
        mRepeat = mTask.getRepeat();


        // Устанавливаем заголовок
        mTitleField.setText(mTitle);


        // Устанавливаем дату
        if (!mDate.equals("Не установлена")) {
            long dateLong = Long.parseLong(mDate);
            Date date = new Date(dateLong);
            DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            mDate = dateFormat.format(date);
        }

        mDateField.setText(mDate);


        // Устанавливаем приоритет
        switch (mPriority) {
            case "0":
                mPriority = "Чрезвычайный";
                break;
            case "1":
                mPriority = "Высокий";
                break;
            case "2":
                mPriority = "Обычный";
                break;
            case "3":
                mPriority = "Низкий";
                break;
            case "4":
                mPriority = "Самый низкий";
                break;
        }

        mPriorityField.setText(mPriority);


        // Устанавливаем цвет
        switch (mColor) {
            case "8_not_set":
                mColor = "Не выбран";
                break;
            case "1_red":
                mColor = "Красный";
                break;
            case "2_orange":
                mColor = "Оранжевый";
                break;
            case "3_yellow":
                mColor = "Желтый";
                break;
            case "4_green":
                mColor = "Зеленый";
                break;
            case "5_blue":
                mColor = "Синий";
                break;
            case "6_violet":
                mColor = "Фиолетовый";
                break;
            case "7_pink":
                mColor = "Розовый";
                break;
        }

        mColorField.setText(mColor);


        // Устанавливаем повтор
        mRepeatField.setText(mRepeat);
    }
}

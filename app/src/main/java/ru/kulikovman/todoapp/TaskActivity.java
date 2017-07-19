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
    TodoBaseHelper mTodoBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTodoBaseHelper = new TodoBaseHelper(this);

        Log.d("myLog", "Добавление задачи");
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

    public void addTask(View view) {
        EditText taskTitle = (EditText) findViewById(R.id.task_title);
        TextView taskDate = (TextView) findViewById(R.id.date_field);
        TextView taskPriority = (TextView) findViewById(R.id.priority_field);
        TextView taskColor = (TextView) findViewById(R.id.color_field);
        TextView taskRepeat = (TextView) findViewById(R.id.repeat_field);

        // Получаем заголовок
        String title = taskTitle.getText().toString();

        if (!title.equals("")) {
            // Получаем дату
            String date = taskDate.getText().toString();
            if (!date.equals("Не установлена")) {
                DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                try {
                    date = String.valueOf(dateFormat.parse(date).getTime());
                } catch (ParseException ignored) {
                }
            }

            // Получаем приоритет
            String priority = taskPriority.getText().toString();
            switch (priority) {
                case "Чрезвычайный":
                    priority = "0";
                    break;
                case "Высокий":
                    priority = "1";
                    break;
                case "Обычный":
                    priority = "2";
                    break;
                case "Низкий":
                    priority = "3";
                    break;
                case "Самый низкий":
                    priority = "4";
                    break;
            }

            // Получаем цвет
            String color = taskColor.getText().toString();

            // Получаем повтор
            String repeat = taskRepeat.getText().toString();

            // Сохраняем задачу в базу
            Task task = new Task(title, date, priority, color, repeat);
            mTodoBaseHelper.addTask(task);

            // Возвращаемся в список задач
            Intent intent = new Intent(this, TodoListActivity.class);
            startActivity(intent);
        }
    }
}

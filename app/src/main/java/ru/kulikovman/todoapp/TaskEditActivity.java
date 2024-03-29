package ru.kulikovman.todoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;

import ru.kulikovman.todoapp.dialogs.DateDialog;
import ru.kulikovman.todoapp.dialogs.GroupDialog;
import ru.kulikovman.todoapp.dialogs.PriorityDialog;
import ru.kulikovman.todoapp.dialogs.RepeatDialog;
import ru.kulikovman.todoapp.messages.FirstSetTaskDate;
import ru.kulikovman.todoapp.models.Group;
import ru.kulikovman.todoapp.models.Task;

import static ru.kulikovman.todoapp.Helper.convertLongTextDateToLong;
import static ru.kulikovman.todoapp.Helper.convertLongToLongTextDate;

public class TaskEditActivity extends AppCompatActivity {
    private Task mTask;
    private Realm mRealm;

    private EditText mTaskTitle;
    private TextView mDateState, mPriorityState, mGroupState, mRepeatState, mReminderState;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
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

        // Подключаем базу данных и читаем id из интента
        mRealm = Realm.getDefaultInstance();

        // Получаем задачу если она есть
        if (getIntent().getSerializableExtra("task_id") != null) {
            long taskId = (long) getIntent().getSerializableExtra("task_id");
            mTask = mRealm.where(Task.class).equalTo(Task.ID, taskId).findFirst();
            loadTask();
        }

        Log.d("log", "Завершен onCreate в TaskEditActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    public void taskOptions(View view) {
        // Прячем клавиатуру
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }

        // Получаем дату задачи
        String date = mDateState.getText().toString().trim();

        // Вызываем диалоги с выбором соответствующих опций
        switch (view.getId()) {
            case R.id.date_layout:
                DialogFragment dateDialog = new DateDialog();
                dateDialog.show(getSupportFragmentManager(), "dateDialog");
                break;
            case R.id.priority_layout:
                DialogFragment priorityDialog = new PriorityDialog();
                priorityDialog.show(getSupportFragmentManager(), "priorityDialog");
                break;
            case R.id.group_layout:
                DialogFragment groupDialog = new GroupDialog();
                groupDialog.show(getSupportFragmentManager(), "groupDialog");
                break;
            case R.id.repeat_layout:
                if (date.equals(getString(R.string.date_without))) {
                    // Если у задачи нет даты, то показываем сообщение
                    DialogFragment firstSetTaskDate = new FirstSetTaskDate();
                    firstSetTaskDate.show(getSupportFragmentManager(), "firstSetTaskDate");
                } else {
                    // Показываем диалог выбора повтора
                    DialogFragment repeatDialog = new RepeatDialog();
                    repeatDialog.show(getSupportFragmentManager(), "repeatDialog");
                }
                break;
            case R.id.reminder_layout:
                if (date.equals(getString(R.string.date_without))) {
                    // Если у задачи нет даты, то показываем сообщение
                    DialogFragment firstSetTaskDate = new FirstSetTaskDate();
                    firstSetTaskDate.show(getSupportFragmentManager(), "firstSetTaskDate");
                } else {
                    // Меняем статус напоминания
                    String reminder = mReminderState.getText().toString();

                    if (reminder.equals(getString(R.string.reminder_disabled))) {
                        mReminderState.setText(R.string.reminder_enabled);
                    } else {
                        mReminderState.setText(R.string.reminder_disabled);
                    }
                }
                break;
        }
    }

    private void loadTask() {
        // Устанавливаем заголовок
        mTaskTitle.setText(mTask.getTitle());

        // Устанавливаем дату
        long targetDate = mTask.getTargetDate();

        if (targetDate == Long.MAX_VALUE) {
            mDateState.setText(getString(R.string.date_without));
        } else {
            mDateState.setText(convertLongToLongTextDate(targetDate));
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
        String repeat = mTask.getRepeat();

        if (repeat == null) {
            mRepeatState.setText(getString(R.string.repeat_without));
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
        boolean reminder = mTask.getReminder();

        if (reminder) {
            mReminderState.setText(getString(R.string.reminder_enabled));
        } else {
            mReminderState.setText(getString(R.string.reminder_disabled));
        }
    }

    public void saveTask(View view) {
        // Получаем заголовок задачи
        String title = mTaskTitle.getText().toString().trim();

        // Если заголовок есть, то делаем все остальное
        if (title.length() > 0) {
            // Создаем задачу
            Task task = new Task(title);

            // Получаем дату
            String date = mDateState.getText().toString();

            if (!date.equals(getString(R.string.date_without))) {
                task.setTargetDate(convertLongTextDateToLong(date));
            } else {
                task.setTargetDate(Long.MAX_VALUE);
            }

            // Получаем приоритет
            String priority = mPriorityState.getText().toString();

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
            String groupName = mGroupState.getText().toString();

            if (!groupName.equals(getString(R.string.group_without))) {
                Group group = mRealm.where(Group.class).equalTo(Group.NAME, groupName).findFirst();
                task.setGroup(group);
            } else {
                task.setGroup(null);
            }

            // Получаем повтор
            String repeat = mRepeatState.getText().toString();

            if (!repeat.equals(getString(R.string.repeat_without))) {
                if (repeat.equals(getString(R.string.repeat_day))) {
                    task.setRepeat("day");
                } else if (repeat.equals(getString(R.string.repeat_week))) {
                    task.setRepeat("week");
                } else if (repeat.equals(getString(R.string.repeat_month))) {
                    task.setRepeat("month");
                } else if (repeat.equals(getString(R.string.repeat_year))) {
                    task.setRepeat("year");
                }
            } else {
                task.setRepeat(null);
            }

            // Получаем статус напоминания
            String reminder = mReminderState.getText().toString();

            if (reminder.equals(getString(R.string.reminder_enabled))) {
                task.setReminder(true);
            } else {
                task.setReminder(false);
            }

            // Добавляем или обновляем задачу в базе
            mRealm.beginTransaction();

            if (mTask == null) {
                mRealm.insert(task);
            } else {
                mTask.setTitle(title);
                mTask.setTargetDate(task.getTargetDate());
                mTask.setPriority(task.getPriority());
                mTask.setGroup(task.getGroup());
                mTask.setRepeat(task.getRepeat());
                mTask.setReminder(task.getReminder());
            }

            mRealm.commitTransaction();
            onBackPressed();
        }
    }
}

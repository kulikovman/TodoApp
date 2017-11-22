package ru.kulikovman.todoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import io.realm.Realm;

import ru.kulikovman.todoapp.dialogs.DateDialog;
import ru.kulikovman.todoapp.dialogs.GroupDialog;
import ru.kulikovman.todoapp.dialogs.PriorityDialog;
import ru.kulikovman.todoapp.dialogs.ReminderDialog;
import ru.kulikovman.todoapp.dialogs.RepeatDialog;
import ru.kulikovman.todoapp.messages.CanNotSetReminder;
import ru.kulikovman.todoapp.messages.FirstSetTaskDate;
import ru.kulikovman.todoapp.models.Group;
import ru.kulikovman.todoapp.models.Task;

import static ru.kulikovman.todoapp.Helper.convertLongTextDateToLong;
import static ru.kulikovman.todoapp.Helper.convertLongToLongTextDate;
import static ru.kulikovman.todoapp.Helper.convertLongToShortTextDate;
import static ru.kulikovman.todoapp.Helper.convertTextDateToCalendar;
import static ru.kulikovman.todoapp.Helper.getTodayRoundCalendar;

public class TaskEditActivity extends AppCompatActivity {
    private Task mTask;
    private Realm mRealm;

    private EditText mTaskTitle;
    private TextView mDateState, mPriorityState, mGroupState, mRepeatState, mReminderState;
    //private LinearLayout mContainerLayout;

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
        //mContainerLayout = (LinearLayout) findViewById(R.id.container_layout);

        // Подключаем базу данных и читаем id из интента
        mRealm = Realm.getDefaultInstance();
        String id = (String) getIntent().getSerializableExtra("task_id");

        // Если uuid не пустой, то получаем задачу и обновляем поля
        if (id != null) {
            mTask = mRealm.where(Task.class).equalTo(Task.ID, id).findFirst();
            loadTask();
        }

        // Слушатель касаний для макета
        //mContainerLayout.setOnTouchListener(this);
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
                    // Показываем диалог выбора даты напоминания
                    DialogFragment reminderDialog = new ReminderDialog();
                    reminderDialog.show(getSupportFragmentManager(), "reminderDialog");
                }
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
        String repeat = mTask.getRepeatDate();

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
        String reminder = mTask.getReminderDate();

        if (reminder == null) {
            mReminderState.setText(getString(R.string.reminder_without));
        } else if (reminder.equals("on_the_day")) {
            mReminderState.setText(getString(R.string.reminder_on_the_day));
        } else if (reminder.equals("day_before")) {
            mReminderState.setText(getString(R.string.reminder_day_before));
        } else if (reminder.equals("two_day_before")) {
            mReminderState.setText(getString(R.string.reminder_two_day_before));
        } else if (reminder.equals("week_before")) {
            mReminderState.setText(getString(R.string.reminder_week_before));
        } else if (reminder.equals("month_before")) {
            mReminderState.setText(getString(R.string.reminder_month_before));
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
            String date = mDateState.getText().toString().trim();

            if (!date.equals(getString(R.string.date_without))) {
                task.setTargetDate(convertLongTextDateToLong(date));
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
            String groupName = mGroupState.getText().toString().trim();

            if (!groupName.equals(getString(R.string.group_without))) {
                Group group = mRealm.where(Group.class).equalTo(Group.NAME, groupName).findFirst();
                task.setGroup(group);
            }

            // Получаем повтор
            String repeat = mRepeatState.getText().toString().trim();

            if (!repeat.equals(getString(R.string.repeat_without))) {
                if (repeat.equals(getString(R.string.repeat_day))) {
                    task.setRepeatDate("day");
                } else if (repeat.equals(getString(R.string.repeat_week))) {
                    task.setRepeatDate("week");
                } else if (repeat.equals(getString(R.string.repeat_month))) {
                    task.setRepeatDate("month");
                } else if (repeat.equals(getString(R.string.repeat_year))) {
                    task.setRepeatDate("year");
                }
            }

            // Получаем дату напоминания
            String reminder = mReminderState.getText().toString().trim();

            if (!reminder.equals(getString(R.string.reminder_without))) {
                if (reminder.equals(getString(R.string.reminder_on_the_day))) {
                    task.setReminderDate("on_the_day");
                } else if (reminder.equals(getString(R.string.reminder_day_before))) {
                    task.setReminderDate("day_before");
                } else if (reminder.equals(getString(R.string.reminder_two_day_before))) {
                    task.setReminderDate("two_day_before");
                } else if (reminder.equals(getString(R.string.reminder_week_before))) {
                    task.setReminderDate("week_before");
                } else if (reminder.equals(getString(R.string.reminder_month_before))) {
                    task.setReminderDate("month_before");
                }
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
                mTask.setRepeatDate(task.getRepeatDate());
                mTask.setReminderDate(task.getReminderDate());
            }

            mRealm.commitTransaction();
            closeActivity();
        }
    }

    private void closeActivity() {
        // Удаляем текущий активити из стека и возвращаемся в список групп
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /*@Override
    public boolean onTouch(View v, MotionEvent event) {
        // Прячем клавиатуру
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        return true;
    }*/
}

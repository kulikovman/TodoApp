package ru.kulikovman.todoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ru.kulikovman.todoapp.database.TodoBaseHelper;
import ru.kulikovman.todoapp.models.Task;
import ru.kulikovman.todoapp.models.TaskComparator;

public class TodoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TaskAdapter.OnItemClickListener {

    private SharedPreferences mSharedPref;

    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private TodoBaseHelper mDbHelper;

    private View mItemView;
    private Task mTask;
    private int mPosition;

    private List<Task> mAllTasks;
    private List<Task> mCurrentTasks;
    private String mTypeTaskList;

    private FloatingActionButton mDeleteButton, mEditButton, mDoneButton;
    private TextView mNumberOfTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        Log.d("myTag", "Запущен onCreate");

        // Судя по всему, это код запуска бокового меню и сопутствующих элементов
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Инициализируем поле в хедере для показа количества задач
        View header = navigationView.getHeaderView(0);
        mNumberOfTasks = (TextView) header.findViewById(R.id.number_of_tasks);

        // Инициализируем необходимые вью элементы
        mDeleteButton = (FloatingActionButton) findViewById(R.id.fab_delete_task);
        mEditButton = (FloatingActionButton) findViewById(R.id.fab_edit_task);
        mDoneButton = (FloatingActionButton) findViewById(R.id.fab_done_task);
        mRecyclerView = (RecyclerView) findViewById(R.id.todo_recycler_view);

        // Создаем базу и обновляем общий список задач
        mDbHelper = new TodoBaseHelper(this);
        mAllTasks = mDbHelper.getAllTasks();

        // Получаем SharedPreferences
        mSharedPref = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);

        // Восстанавливаем из mSharedPref тип списка задач
        mTypeTaskList = mSharedPref.getString(getString(R.string.type_list), getString(R.string.list_unfinished));

        // Устанавливаем параметры для RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Создаем и устанавливаем список
        updateTaskList();

        // Слушатель для адаптера списка
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("myTag", "Запущен onPause");

        // Сохраняем тип текущего списка задач
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(getString(R.string.type_list), mTypeTaskList);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        // Если открыто боковое меню, то сначала оно скроется
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.здесь_вписать_имя_меню, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Присваиваем списку тип выбранный в меню
        if (id == R.id.nav_task_today) {
            mTypeTaskList = getString(R.string.list_today);
            updateTaskList();
        } else if (id == R.id.nav_task_month) {
            mTypeTaskList = getString(R.string.list_month);
            updateTaskList();
        } else if (id == R.id.nav_task_without_date) {
            mTypeTaskList = getString(R.string.list_without_date);
            updateTaskList();
        } else if (id == R.id.nav_task_unfinished) {
            mTypeTaskList = getString(R.string.list_unfinished);
            updateTaskList();
        } else if (id == R.id.nav_task_finished) {
            mTypeTaskList = getString(R.string.list_finished);
            updateTaskList();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(View itemView, int itemPosition, Task task, int selectedPosition) {
        // Выделяем или снимаем выделение выбранного элемента списка
        // Этот листенер проброшен с адаптера
        /*if (mItemView == null) {
            itemView.setBackgroundColor(Color.LTGRAY);
            mItemView = itemView;
            showActionButton();
        } else {
            if (mItemView != itemView) {
                mItemView.setBackgroundColor(Color.TRANSPARENT);
                itemView.setBackgroundColor(Color.LTGRAY);
                mItemView = itemView;
                showActionButton();
            } else {
                int itemColor = ((ColorDrawable) itemView.getBackground()).getColor();

                if (itemColor == Color.TRANSPARENT) {
                    itemView.setBackgroundColor(Color.LTGRAY);
                    showActionButton();
                } else {
                    itemView.setBackgroundColor(Color.TRANSPARENT);
                    hideActionButton();
                }
            }
        }*/

        if (selectedPosition != RecyclerView.NO_POSITION) {
            showActionButton();
        } else {
            hideActionButton();
        }

        // Запоминаем последний выбранный элемент
        mTask = task;
        mPosition = itemPosition;
    }

    private void updateTaskList() {
        // Создаем нужный тип списка задач
        if (mTypeTaskList != null) {
            if (mTypeTaskList.equals(getString(R.string.list_today))) {
                mCurrentTasks = createTodayTaskList();
            } else if (mTypeTaskList.equals(getString(R.string.list_month))) {
                mCurrentTasks = createMonthTaskList();
            } else if (mTypeTaskList.equals(getString(R.string.list_without_date))) {
                mCurrentTasks = createWithoutDateTaskList();
            } else if (mTypeTaskList.equals(getString(R.string.list_unfinished))) {
                mCurrentTasks = createUnfinishedTaskList();
            } else if (mTypeTaskList.equals(getString(R.string.list_finished))) {
                mCurrentTasks = createFinishedTaskList();
            }
        } else {
            mCurrentTasks = createUnfinishedTaskList();
        }

        // Сортируем через кампоратор
        Collections.sort(mCurrentTasks, new TaskComparator());

        // Назначаем адаптеру новый список
        if (mAdapter == null) {
            mAdapter = new TaskAdapter(this, mCurrentTasks);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.resetSelection();
            mAdapter.setTasks(mCurrentTasks);
            mAdapter.notifyDataSetChanged();
        }

        finishAction();
    }

    private void setNumberOfTasks() {
        // Метод для установки количества задач в заголовке меню
        int taskCounter = 0;
        for (Task task : mAllTasks) {
            if (!task.isDone()) {
                taskCounter++;
            }
        }

        String numberOfTasks = "Всего задач: " + taskCounter;
        mNumberOfTasks.setText(numberOfTasks);
    }

    private List<Task> createTodayTaskList() {
        List<Task> tasks = new ArrayList<>();

        Date date = new Date();
        long targetDate = date.getTime();

        for (Task task : mAllTasks) {
            if (!task.getDate().equals("Не установлена") && !task.isDone()) {
                long taskDate = Long.parseLong(task.getDate());
                if (taskDate <= targetDate) {
                    tasks.add(task);
                }
            }
        }
        return tasks;
    }

    private List<Task> createMonthTaskList() {
        List<Task> tasks = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        long targetDate = calendar.getTimeInMillis();

        for (Task task : mAllTasks) {
            if (!task.getDate().equals("Не установлена") && !task.isDone()) {
                long taskDate = Long.parseLong(task.getDate());
                if (taskDate <= targetDate) {
                    tasks.add(task);
                }
            }
        }
        return tasks;
    }

    private List<Task> createUnfinishedTaskList() {
        List<Task> tasks = new ArrayList<>();

        for (Task task : mAllTasks) {
            if (!task.isDone()) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    private List<Task> createFinishedTaskList() {
        List<Task> tasks = new ArrayList<>();

        for (Task task : mAllTasks) {
            if (task.isDone()) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    private List<Task> createWithoutDateTaskList() {
        List<Task> tasks = new ArrayList<>();

        for (Task task : mAllTasks) {
            if (task.getDate().equals("Не установлена") && !task.isDone()) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    public void fabDoneTask(View view) {
        Task task = new Task(mTask.getTitle(),
                mTask.getDate(), mTask.getPriority(), mTask.getColor(), mTask.getRepeat());

        mTask.setDone(!mTask.isDone());
        mDbHelper.updateTask(mTask);
        mAdapter.deleteItem(mPosition);

        // Если у задачи был повтор, то создаем аналогичную задачу на новую дату
        if (!task.getRepeat().equals("Без повтора") && !task.isDone()) {
            String taskDate = task.getDate();
            long longDate = Long.parseLong(taskDate);

            Date date = new Date(longDate);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);

            switch (task.getRepeat()) {
                case "Ежедневно":
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    break;
                case "Каждую неделю":
                    calendar.add(Calendar.DAY_OF_YEAR, 7);
                    break;
                case "Раз в месяц":
                    calendar.add(Calendar.MONTH, 1);
                    break;
                case "Через год":
                    calendar.add(Calendar.YEAR, 1);
                    break;
            }

            date = calendar.getTime();
            longDate = date.getTime();
            taskDate = String.valueOf(longDate);
            task.setDate(taskDate);

            mDbHelper.addTask(task);
            mCurrentTasks.add(task);
            Collections.sort(mCurrentTasks, new TaskComparator());

            for (int i = 0; i < mCurrentTasks.size(); i++) {
                String uuidFirst = mCurrentTasks.get(i).getId().toString();
                String uuidSecond = task.getId().toString();

                if (uuidFirst.equals(uuidSecond)) {
                    mCurrentTasks.remove(i);
                    mAdapter.addItem(i, task);
                    break;
                }
            }
        }

        mAllTasks = mDbHelper.getAllTasks();
        finishAction();
    }

    public void fabEditTask(View view) {
        // Открываем активити редактирования задачи и передаем uuid задачи
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("task_id", mTask.getId());
        startActivity(intent);
    }

    public void fabDeleteTask(View view) {
        mDbHelper.deleteTask(mTask);
        mAdapter.deleteItem(mPosition);

        mAllTasks = mDbHelper.getAllTasks();
        finishAction();
    }

    public void fabAddTask(View view) {
        // Просто открываем активити
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
    }

    private void finishAction() {
        // Убираем выделение и скрываем кнопки действий
        if (mItemView != null) {
            mItemView.setBackgroundColor(Color.TRANSPARENT);
        }
        hideActionButton();
        setNumberOfTasks();
    }

    private void hideActionButton() {
        mDeleteButton.setVisibility(View.INVISIBLE);
        mEditButton.setVisibility(View.INVISIBLE);
        mDoneButton.setVisibility(View.INVISIBLE);
    }

    private void showActionButton() {
        mDeleteButton.setVisibility(View.VISIBLE);
        mEditButton.setVisibility(View.VISIBLE);
        mDoneButton.setVisibility(View.VISIBLE);
    }
}

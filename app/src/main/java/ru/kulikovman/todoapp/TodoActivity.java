package ru.kulikovman.todoapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
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

    public static final String LIST_TODAY = "today";
    public static final String LIST_MONTH = "month";
    public static final String LIST_WITHOUT_DATE = "without_date";
    public static final String LIST_UNFINISHED = "unfinished";
    public static final String LIST_FINISHED = "finished";

    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private TodoBaseHelper mDbHelper;

    private View mItemView;
    private Task mTask;
    private int mPosition;
    private List<Task> mTasks;
    private String mTypeOfUploadedData = LIST_UNFINISHED;

    private FloatingActionButton mDeleteButton, mEditButton, mDoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDeleteButton = (FloatingActionButton) findViewById(R.id.fab_delete_task);
        mEditButton = (FloatingActionButton) findViewById(R.id.fab_edit_task);
        mDoneButton = (FloatingActionButton) findViewById(R.id.fab_done_task);

        mDbHelper = new TodoBaseHelper(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.todo_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateUI();

        mAdapter.setOnItemClickListener(this);
        Log.d("myLog", "Программа запущена");
    }

    @Override
    public void onBackPressed() {
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

        if (id == R.id.nav_task_today) {

        } else if (id == R.id.nav_task_month) {

        } else if (id == R.id.nav_task_without_date) {

        } else if (id == R.id.nav_task_unfinished) {
            mTypeOfUploadedData = LIST_UNFINISHED;
            updateUI();
        } else if (id == R.id.nav_task_finished) {
            mTypeOfUploadedData = LIST_FINISHED;
            updateUI();
        } else if (id == R.id.nav_task_rate) {

        } else if (id == R.id.nav_task_info) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(View itemView, int position, Task task) {
        if (mItemView == null) {
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
        }

        mTask = task;
        mPosition = position;
    }

    private void updateUI() {
        switch (mTypeOfUploadedData) {
            case LIST_TODAY:
                mTasks = mDbHelper.getUnfinishedTasks();
                break;
            case LIST_MONTH:
                mTasks = mDbHelper.getUnfinishedTasks();
                break;
            case LIST_WITHOUT_DATE:
                mTasks = mDbHelper.getUnfinishedTasks();
                break;
            case LIST_UNFINISHED:
                mTasks = mDbHelper.getUnfinishedTasks();
                break;
            case LIST_FINISHED:
                mTasks = mDbHelper.getFinishedTasks();
                break;
        }

        Collections.sort(mTasks, new TaskComparator());

        if (mAdapter == null) {
            mAdapter = new TaskAdapter(mTasks);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTasks(mTasks);
            mAdapter.notifyDataSetChanged();
            finishAction();
        }
    }

    public void fabDoneTask(View view) {
        Task task = new Task(mTask.getTitle(),
                mTask.getDate(), mTask.getPriority(), mTask.getColor(), mTask.getRepeat());

        mTask.setDone(!mTask.isDone());
        mDbHelper.updateTask(mTask);
        mAdapter.deleteItem(mPosition);

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
            mTasks.add(task);
            Collections.sort(mTasks, new TaskComparator());

            for (int i = 0; i < mTasks.size(); i++) {
                String uuidFirst = mTasks.get(i).getId().toString();
                String uuidSecond = task.getId().toString();

                if (uuidFirst.equals(uuidSecond)) {
                    mTasks.remove(i);
                    mAdapter.addItem(i, task);
                    break;
                }
            }
        }

        finishAction();
    }

    public void fabEditTask(View view) {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("task_id", mTask.getId());
        startActivity(intent);
    }

    public void fabDeleteTask(View view) {
        mDbHelper.deleteTask(mTask);
        mAdapter.deleteItem(mPosition);

        finishAction();
    }

    public void fabAddTask(View view) {
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
    }

    private void finishAction() {
        // Убираем выделение и скрываем кнопки действий
        if (mItemView != null) {
            mItemView.setBackgroundColor(Color.TRANSPARENT);
        }

        hideActionButton();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mTypeOfUploadedData", mTypeOfUploadedData);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTypeOfUploadedData = savedInstanceState.getString("mTypeOfUploadedData");
    }
}

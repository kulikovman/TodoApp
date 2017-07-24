package ru.kulikovman.todoapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

public class TodoListActivity extends AppCompatActivity implements TaskAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private TodoBaseHelper mDbHelper;

    private View mItemView;
    private Task mTask;
    private int mPosition;

    private FloatingActionButton mDeleteButton, mEditButton, mDoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_list, menu);
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

    public void fabDoneTask(View view) {
        String taskRepeat = mTask.getRepeat();

        if (!taskRepeat.equals("Без повтора") && !mTask.isDone()) {
            Task task = new Task(mTask.getTitle(),
                    mTask.getDate(), mTask.getPriority(), mTask.getColor(), mTask.getRepeat());

            String taskDate = task.getDate();
            long longDate = Long.parseLong(taskDate);

            Date date = new Date(longDate);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);

            switch (taskRepeat) {
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
        }

        mTask.setDone(!mTask.isDone());
        mDbHelper.updateTask(mTask);
        mAdapter.notifyItemRemoved(mPosition);

        updateUI();
    }

    public void fabEditTask(View view) {
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("task_id", mTask.getId());
        startActivity(intent);
    }

    public void fabDeleteTask(View view) {
        mDbHelper.deleteTask(mTask);
        mAdapter.notifyItemRemoved(mPosition);

        updateUI();
    }

    public void fabAddTask(View view) {
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
    }

    private void updateUI() {
        List<Task> tasks = mDbHelper.getTaskList();
        Collections.sort(tasks, new TaskComparator());

        if (mAdapter == null) {
            mAdapter = new TaskAdapter(tasks);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mItemView.setBackgroundColor(Color.TRANSPARENT);
            mAdapter.setTasks(tasks);
            mAdapter.notifyDataSetChanged();
            hideActionButton();
        }
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

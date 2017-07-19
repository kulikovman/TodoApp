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

import java.util.List;

import ru.kulikovman.todoapp.database.TodoBaseHelper;
import ru.kulikovman.todoapp.models.Task;

public class TodoListActivity extends AppCompatActivity implements TaskAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private TodoBaseHelper mDbHelper;

    private View mItemView;
    private Task mTask;

    private FloatingActionButton mDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDeleteButton = (FloatingActionButton) findViewById(R.id.fab_delete_task);

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

    public void fabAddTask(View view) {
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
    }

    public void fabDeleteTask(View view) {
        mDbHelper.deleteTask(mTask);
        mItemView.setBackgroundColor(Color.TRANSPARENT);

        updateUI();
    }

    private void updateUI() {
        List<Task> tasks = mDbHelper.getTaskList();

        if (mAdapter == null) {
            mAdapter = new TaskAdapter(tasks);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTasks(tasks);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View itemView, int position, Task task) {
        if (mItemView == null) {
            itemView.setBackgroundColor(Color.LTGRAY);
            mItemView = itemView;
            mDeleteButton.setVisibility(View.VISIBLE);
        } else {
            if (mItemView != itemView) {
                mItemView.setBackgroundColor(Color.TRANSPARENT);
                itemView.setBackgroundColor(Color.LTGRAY);
                mDeleteButton.setVisibility(View.VISIBLE);
                mItemView = itemView;
            } else {
                int itemColor = ((ColorDrawable) itemView.getBackground()).getColor();

                if (itemColor == Color.TRANSPARENT) {
                    itemView.setBackgroundColor(Color.LTGRAY);
                    mDeleteButton.setVisibility(View.VISIBLE);
                } else {
                    itemView.setBackgroundColor(Color.TRANSPARENT);
                    mDeleteButton.setVisibility(View.INVISIBLE);
                }
            }
        }

        mTask = task;
    }
}

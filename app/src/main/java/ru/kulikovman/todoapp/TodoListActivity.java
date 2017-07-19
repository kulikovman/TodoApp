package ru.kulikovman.todoapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

public class TodoListActivity extends AppCompatActivity {
    private RecyclerView mTodoRecyclerView;
    private TaskAdapter mTaskAdapter;

    private TodoBaseHelper mTodoBaseHelper;
    private List<Task> mTasks;
    private View mItemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTodoBaseHelper = new TodoBaseHelper(this);
        mTasks = mTodoBaseHelper.getTaskList();

        mTodoRecyclerView = (RecyclerView) findViewById(R.id.todo_recycler_view);
        mTodoRecyclerView.setHasFixedSize(true);
        mTodoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTaskAdapter = new TaskAdapter(mTasks);
        mTodoRecyclerView.setAdapter(mTaskAdapter);

        mTaskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position, Task task) {
                if (mItemView == null) {
                    itemView.setBackgroundColor(Color.LTGRAY);
                    mItemView = itemView;
                } else {
                    if (mItemView != itemView) {
                        mItemView.setBackgroundColor(Color.TRANSPARENT);
                        itemView.setBackgroundColor(Color.LTGRAY);
                        mItemView = itemView;
                    } else {
                        int itemColor = ((ColorDrawable) itemView.getBackground()).getColor();

                        if (itemColor == Color.TRANSPARENT) {
                            itemView.setBackgroundColor(Color.LTGRAY);
                        } else {
                            itemView.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                }




            }
        });
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



}

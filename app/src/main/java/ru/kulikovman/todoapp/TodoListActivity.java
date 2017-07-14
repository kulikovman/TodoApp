package ru.kulikovman.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.kulikovman.todoapp.database.TodoBaseHelper;
import ru.kulikovman.todoapp.models.Task;

public class TodoListActivity extends AppCompatActivity {
    private RecyclerView mTodoRecyclerView;
    private TaskAdapter mTaskAdapter;

    TodoBaseHelper mTodoBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTodoBaseHelper = new TodoBaseHelper(this);

        mTodoRecyclerView = (RecyclerView) findViewById(R.id.todo_recycler_view);
        mTodoRecyclerView.setHasFixedSize(true);
        mTodoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //mTaskAdapter = new TaskAdapter(mTodoBaseHelper.getTaskList());
        mTaskAdapter = new TaskAdapter(getTestTasks());
        mTodoRecyclerView.setAdapter(mTaskAdapter);

        mTaskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                /*Intent intent = new Intent(getParent(), TaskActivity.class);
                startActivity(intent);*/

                Toast.makeText(TodoListActivity.this, "Что-то получается...", Toast.LENGTH_SHORT)
                        .show();
            }
        });
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

    public List<Task> getTestTasks() {
        List<Task> testTasks = new ArrayList<>();

        testTasks.add(new Task("Тестовый таск", new Date(), 0, "Желтый", "Ежедневно"));
        testTasks.add(new Task("Еще один", new Date(), 2, "Зеленый", "Каждую неделю"));
        testTasks.add(new Task("И еще один", new Date(), 4, "Желтый", "Без повтора"));
        testTasks.add(new Task("Просто так", new Date(), 3, "Синий", "Каждую неделю"));
        testTasks.add(new Task("Парам-пам-пам", new Date(), 1, "Фиолетовый", "Без повтора"));
        testTasks.add(new Task("Декоданс древнегреческий", new Date(), 3, "Красный", "Без повтора"));
        testTasks.add(new Task("Свинтус полноводный", new Date(), 3, "Желтый", "Через год"));
        testTasks.add(new Task("Лисья норка", new Date(), 1, "Оранжевый", "Без повтора"));

        return testTasks;
    }

}

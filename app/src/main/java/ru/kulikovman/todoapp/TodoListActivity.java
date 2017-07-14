package ru.kulikovman.todoapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import ru.kulikovman.todoapp.database.TodoBaseHelper;
import ru.kulikovman.todoapp.models.Task;

public class TodoListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private RecyclerView mTodoListRecyclerView;
    private RecyclerView.Adapter mTaskAdapter;

    TodoBaseHelper mTodoBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTodoBaseHelper = new TodoBaseHelper(this);

        mTodoListRecyclerView = (RecyclerView) findViewById(R.id.todo_list_recycler_view);
        mTodoListRecyclerView.setHasFixedSize(true);
        mTodoListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTaskAdapter = new TaskAdapter(getTasks());
        mTodoListRecyclerView.setAdapter(mTaskAdapter);
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

    public List<Task> getTasks() {
        List<Task> testTasks = new ArrayList<>();

        testTasks.add(new Task("Gjbdsf sdfgsdf dfgsdf dfg"));
        testTasks.add(new Task("Swvfe ert dv erhtr nt"));
        testTasks.add(new Task("L vrgviebvkjbdk ekbrihewoivn sdvjnkdsbfv vksjbvik kjsdv"));
        testTasks.add(new Task("Ufnh kwjoihfvsbn kjsbdiuvh"));
        testTasks.add(new Task("Ekjnkuh kjbfeuvbxkdj kjbdfiubvd kjdfilvub kdjblfvbxd kjfdbvldxl"));

        return testTasks;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.setSelected(true);
    }
}

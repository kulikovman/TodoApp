package ru.kulikovman.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ru.kulikovman.todoapp.database.DbHelper;
import ru.kulikovman.todoapp.models.Group;

public class GroupListActivity extends AppCompatActivity implements GroupAdapter.OnItemClickListener {
    private DbHelper mDbHelper;
    private GroupAdapter mGroupAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Инициализируем необходимые вью элементы
        mRecyclerView = (RecyclerView) findViewById(R.id.group_list_recycler_view);

        // Подключаем базу данных
        mDbHelper = new DbHelper(this);

        // Устанавливаем параметры для RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Создаем адаптер и подключаем к списку
        mGroupAdapter = new GroupAdapter(this, mDbHelper.getAllGroups());
        mRecyclerView.setAdapter(mGroupAdapter);


    }

    public void fabAddGroup(View view) {
        // Открываем активити редактирования группы
        Intent intent = new Intent(this, EditGroupActivity.class);
        startActivity(intent);
    }

    public void fabDeleteGroup(View view) {
    }

    public void fabEditGroup(View view) {
    }

    @Override
    public void onItemClick(View itemView, int itemPosition, Group group, int selectedPosition) {

    }
}

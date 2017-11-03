package ru.kulikovman.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import ru.kulikovman.todoapp.database.DbHelper;

public class GroupListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView mGroupListView;
    private DbHelper mDbHelper;
    private GroupAdapterOld mGroupAdapterOld;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Инициализируем необходимые вью элементы
        mGroupListView = (ListView) findViewById(R.id.group_list_view);

        // Подключаем базу данных
        mDbHelper = new DbHelper(this);

        // Создаем адаптер и подключаем к списку
        mGroupAdapterOld = new GroupAdapterOld(this, mDbHelper.getAllGroups());
        mGroupListView.setAdapter(mGroupAdapterOld);

        // Подключаем слушатель нажатий элементов
        mGroupListView.setOnItemClickListener(this);

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Позиция: " + position, Toast.LENGTH_SHORT).show();
        view.setPressed(true);
    }
}

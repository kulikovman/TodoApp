package ru.kulikovman.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import ru.kulikovman.todoapp.database.DbHelper;
import ru.kulikovman.todoapp.models.Group;

public class GroupListActivity extends AppCompatActivity implements GroupAdapter.OnItemClickListener {
    private GroupAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private DbHelper mDbHelper;

    private Group mGroup;
    private int mPosition;

    private FloatingActionButton mEditButton, mDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Инициализируем необходимые вью элементы
        mRecyclerView = (RecyclerView) findViewById(R.id.group_list_recycler_view);
        mEditButton = (FloatingActionButton) findViewById(R.id.fab_edit_group);
        mDeleteButton = (FloatingActionButton) findViewById(R.id.fab_delete_group);

        // Подключаем базу данных
        mDbHelper = new DbHelper(this);

        // Устанавливаем параметры для RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateGroupList();

        // Слушатель для адаптера списка
        mAdapter.setOnItemClickListener(this);

        Log.d("myLog", "Успешно запущен onCreate в GroupListActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateGroupList();
    }

    private void updateGroupList() {
        if (mAdapter == null) {
            mAdapter = new GroupAdapter(this, mDbHelper.getAllGroups());
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.resetSelection();
            mAdapter.setGroups(mDbHelper.getAllGroups());
            mAdapter.notifyDataSetChanged();
        }
    }

    public void fabAddGroup(View view) {
        Intent intent = new Intent(this, EditGroupActivity.class);
        startActivity(intent);
    }

    public void fabDeleteGroup(View view) {
        mDbHelper.deleteGroup(mGroup);
        mAdapter.deleteItem(mPosition);
        hideFabButtons();
    }

    private void hideFabButtons() {
        mEditButton.setVisibility(View.INVISIBLE);
        mDeleteButton.setVisibility(View.INVISIBLE);
    }

    public void fabEditGroup(View view) {
    }

    @Override
    public void onItemClick(View itemView, int itemPosition, Group group, int selectedPosition) {
        // Показываем или скрываем кнопки в зависимости от выделения элементов списка
        if (selectedPosition != RecyclerView.NO_POSITION) {
            mEditButton.setVisibility(View.VISIBLE);
            mDeleteButton.setVisibility(View.VISIBLE);
        } else {
            hideFabButtons();
        }

        // Запоминаем последний выбранный элемент
        mGroup = group;
        mPosition = itemPosition;
    }
}

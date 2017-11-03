package ru.kulikovman.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ru.kulikovman.todoapp.models.Group;

public class GroupListActivity extends AppCompatActivity implements GroupAdapter.OnItemClickListener {
    private GroupAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TaskLab mTaskLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Инициализируем необходимые вью элементы
        mRecyclerView = (RecyclerView) findViewById(R.id.group_list_recycler_view);

        // Организуем доступ к группам и задачам
        mTaskLab = TaskLab.get(this);

        // Устанавливаем параметры для RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateGroupList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateGroupList();
    }

    private void updateGroupList() {
        if (mAdapter == null) {
            mAdapter = new GroupAdapter(this, mTaskLab.getGroups());
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.resetSelection();
            mAdapter.setGroups(mTaskLab.getGroups());
            mAdapter.notifyDataSetChanged();
        }
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

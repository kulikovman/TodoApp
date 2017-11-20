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

import io.realm.Realm;
import io.realm.RealmResults;
import ru.kulikovman.todoapp.adapters.GroupAdapter;
import ru.kulikovman.todoapp.models.Group;

public class GroupListActivity extends AppCompatActivity implements GroupAdapter.OnItemClickListener {
    private GroupAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Realm mRealm;

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
        mRealm = Realm.getDefaultInstance();

        // Устанавливаем параметры для RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Создаем или обновляем адаптер
        updateGroupList();

        // Слушатель для адаптера списка
        mAdapter.setOnItemClickListener(this);

        Log.d("log", "Успешно завершен onCreate в GroupListActivity");
    }

    private RealmResults<Group> loadAllGroups() {
        return mRealm.where(Group.class).findAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateGroupList();
    }

    private void updateGroupList() {
        if (mAdapter == null) {
            mAdapter = new GroupAdapter(this, loadAllGroups());
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.resetSelection();
            mAdapter.setGroups(loadAllGroups());
            mAdapter.notifyDataSetChanged();
        }
    }

    public void fabAddGroup(View view) {
        Intent intent = new Intent(this, GroupEditActivity.class);
        startActivity(intent);
    }

    public void fabDeleteGroup(View view) {
        final RealmResults<Group> results = mRealm.where(Group.class).equalTo(Group.NAME, mGroup.getName()).findAll();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });

        mAdapter.notifyItemRemoved(mPosition);
        mAdapter.resetSelection();
        hideFabButtons();
    }

    private void hideFabButtons() {
        mEditButton.setVisibility(View.INVISIBLE);
        mDeleteButton.setVisibility(View.INVISIBLE);
    }

    public void fabEditGroup(View view) {
        // Передаем с интентом имя группы
        Intent intent = new Intent(this, GroupEditActivity.class);
        intent.putExtra("group_name", mGroup.getName());
        startActivity(intent);
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

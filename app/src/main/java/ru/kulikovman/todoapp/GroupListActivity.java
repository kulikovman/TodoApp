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

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.Sort;
import ru.kulikovman.todoapp.adapters.GroupRealmAdapter;
import ru.kulikovman.todoapp.models.Group;

public class GroupListActivity extends AppCompatActivity implements GroupRealmAdapter.OnItemClickListener {
    private GroupRealmAdapter mAdapter;
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

        // Подключаем базу данных
        mRealm = Realm.getDefaultInstance();

        // Инициализируем необходимые вью элементы
        mRecyclerView = (RecyclerView) findViewById(R.id.group_list_recycler_view);
        mEditButton = (FloatingActionButton) findViewById(R.id.fab_edit_group);
        mDeleteButton = (FloatingActionButton) findViewById(R.id.fab_delete_group);

        // Создаем и запускаем список
        setUpRecyclerView();

        Log.d("log", "Завершен onCreate в GroupListActivity");
    }

    private void setUpRecyclerView() {
        mAdapter = new GroupRealmAdapter(this, loadGroupList());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        // Слушатель для адаптера списка
        mAdapter.setOnItemClickListener(this);
    }

    private OrderedRealmCollection<Group> loadGroupList() {
        return mRealm.where(Group.class)
                .findAll()
                .sort(Group.NAME, Sort.ASCENDING);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    /*private void updateGroupList() {
        if (mAdapter == null) {
            mAdapter = new GroupRealmAdapter(this, loadAllGroups());
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.resetSelection();
            mAdapter.setGroups(loadAllGroups());
            mAdapter.notifyDataSetChanged();
        }
    }*/

    public void fabAddGroup(View view) {
        Intent intent = new Intent(this, GroupEditActivity.class);
        startActivity(intent);
    }

    public void fabEditGroup(View view) {
        // Передаем с интентом имя группы
        Intent intent = new Intent(this, GroupEditActivity.class);
        intent.putExtra("group_id", mGroup.getId());
        startActivity(intent);
    }

    public void fabDeleteGroup(View view) {
        mRealm.beginTransaction();
        mGroup.deleteFromRealm();
        mRealm.commitTransaction();

        // Сопутствующие операции
        mAdapter.notifyItemRemoved(mPosition);
        mAdapter.resetSelection();
        hideActionButtons();
    }

    @Override
    public void onItemClick(View itemView, int itemPosition, Group group, int selectedPosition) {
        // Показываем или скрываем кнопки в зависимости от выделения элементов списка
        if (selectedPosition != RecyclerView.NO_POSITION) {
            mEditButton.setVisibility(View.VISIBLE);
            mDeleteButton.setVisibility(View.VISIBLE);
        } else {
            hideActionButtons();
        }

        // Запоминаем последний выбранный элемент
        mGroup = group;
        mPosition = itemPosition;
    }

    private void hideActionButtons() {
        mEditButton.setVisibility(View.INVISIBLE);
        mDeleteButton.setVisibility(View.INVISIBLE);
    }
}

package ru.kulikovman.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.kulikovman.todoapp.dialogs.ColorDialog;
import ru.kulikovman.todoapp.dialogs.DescriptionDialog;
import ru.kulikovman.todoapp.messages.GroupIsExist;
import ru.kulikovman.todoapp.models.Group;
import ru.kulikovman.todoapp.models.Task;

public class GroupEditActivity extends AppCompatActivity {
    private Group mGroup;
    private Realm mRealm;

    private EditText mGroupName;
    private TextView mDescriptionState, mColorState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Инициализируем необходимые вью элементы
        mGroupName = (EditText) findViewById(R.id.group_name);
        mDescriptionState = (TextView) findViewById(R.id.description_state);
        mColorState = (TextView) findViewById(R.id.color_state);

        // Подключаем базу данных и читаем id из интента
        mRealm = Realm.getDefaultInstance();

        // Получаем группу если она есть
        if (getIntent().getSerializableExtra("group_id") != null) {
            long groupId = (long) getIntent().getSerializableExtra("group_id");
            mGroup = mRealm.where(Group.class).equalTo(Group.ID, groupId).findFirst();
            loadGroup();
        }

        Log.d("log", "Завершен onCreate в GroupEditActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    public void groupOptions(View view) {
        // Обработка нажатий на опции группы
        switch (view.getId()) {
            case R.id.description_layout:
                DialogFragment descriptionFragment = new DescriptionDialog();
                descriptionFragment.show(getSupportFragmentManager(), "descriptionFragment");
                break;
            case R.id.group_layout:
                DialogFragment colorFragment = new ColorDialog();
                colorFragment.show(getSupportFragmentManager(), "colorFragment");
                break;
        }
    }

    private void loadGroup() {
        // Загружаем название
        mGroupName.setText(mGroup.getName());

        // Загружаем описание
        String description = mGroup.getDescription();

        if (description == null) {
            mDescriptionState.setText(getString(R.string.without_description));
        } else {
            mDescriptionState.setText(description);
        }

        // Загружаем цвет
        String color = mGroup.getColor();

        if (color == null) {
            mColorState.setText(getString(R.string.color_without));
        } else if (color.equals("red")) {
            mColorState.setText(getString(R.string.color_1_red));
        } else if (color.equals("orange")) {
            mColorState.setText(getString(R.string.color_2_orange));
        } else if (color.equals("yellow")) {
            mColorState.setText(getString(R.string.color_3_yellow));
        } else if (color.equals("green")) {
            mColorState.setText(getString(R.string.color_4_green));
        } else if (color.equals("blue")) {
            mColorState.setText(getString(R.string.color_5_blue));
        } else if (color.equals("violet")) {
            mColorState.setText(getString(R.string.color_6_violet));
        } else if (color.equals("pink")) {
            mColorState.setText(getString(R.string.color_7_pink));
        }
    }

    public void saveGroup(View view) {
        // Получаем название группы
        String name = mGroupName.getText().toString().trim();

        // Если название заполнено, то делаем все остальное
        if (name.length() > 0) {
            // Создаем группу
            Group group = new Group(name);

            // Если есть описание, то добавляем его в группу
            String description = mDescriptionState.getText().toString().trim();

            if (!description.equals(getString(R.string.without_description))) {
                group.setDescription(description);
            } else {
                group.setDescription(null);
            }

            // Если указан цвет, то добавляем его в группу
            String color = mColorState.getText().toString();

            if (!color.equals(getString(R.string.color_without))) {
                if (color.equals(getString(R.string.color_1_red))) {
                    group.setColor("red");
                } else if (color.equals(getString(R.string.color_2_orange))) {
                    group.setColor("orange");
                } else if (color.equals(getString(R.string.color_3_yellow))) {
                    group.setColor("yellow");
                } else if (color.equals(getString(R.string.color_4_green))) {
                    group.setColor("green");
                } else if (color.equals(getString(R.string.color_5_blue))) {
                    group.setColor("blue");
                } else if (color.equals(getString(R.string.color_6_violet))) {
                    group.setColor("violet");
                } else if (color.equals(getString(R.string.color_7_pink))) {
                    group.setColor("pink");
                }
            } else {
                group.setColor(null);
            }

            // Добавляем или обновляем группу в базе
            mRealm.beginTransaction();

            if (mGroup == null) {
                // Добавляем новую группу
                if (!isGroupExist(name)) {
                    mRealm.insert(group);
                    closeActivity();
                } else {
                    showErrorMessage();
                }
            } else {
                // Или обновляем существующую
                mGroup.setDescription(group.getDescription());
                mGroup.setColor(group.getColor());

                if (mGroup.getName().equals(name)) {
                    closeActivity();
                } else {
                    if (!isGroupExist(name)) {
                        mGroup.setName(name);
                        closeActivity();
                    } else {
                        showErrorMessage();
                    }
                }
            }
        }
    }

    private boolean isGroupExist(String name) {
        RealmResults<Group> groups = mRealm.where(Group.class).equalTo(Group.NAME, name).findAll();
        return groups.size() > 0;
    }

    private void closeActivity() {
        // Закрываем открытую транзакцию и возращаемся назад
        mRealm.commitTransaction();
        onBackPressed();
    }

    private void showErrorMessage() {
        // Отменяем открытую транзакцию
        mRealm.cancelTransaction();

        // Показываем сообщение об ошибке
        DialogFragment groupExistDialog = new GroupIsExist();
        groupExistDialog.show(getSupportFragmentManager(), "groupExistDialog");
    }
}
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

import ru.kulikovman.todoapp.database.DbHelper;
import ru.kulikovman.todoapp.dialogs.ColorDialog;
import ru.kulikovman.todoapp.dialogs.DescriptionDialog;
import ru.kulikovman.todoapp.dialogs.GroupExistDialog;
import ru.kulikovman.todoapp.models.Group;

public class EditGroupActivity extends AppCompatActivity {
    private DbHelper mDbHelper;
    private Group mGroup;

    private EditText mGroupName;
    private TextView mDescriptionState, mColorState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Инициализируем необходимые вью элементы
        mGroupName = (EditText) findViewById(R.id.group_name);
        mDescriptionState = (TextView) findViewById(R.id.description_state);
        mColorState = (TextView) findViewById(R.id.color_state);

        // Подключаем базу данных
        mDbHelper = new DbHelper(this);

        // Читаем имя группы из интента
        String groupName = (String) getIntent().getSerializableExtra("group_name");

        // Если имя есть, то получаем группу и обновляем поля активности
        if (groupName != null) {
            mGroup = mDbHelper.getGroupByName(groupName);
            loadGroup();
        }

        Log.d("myLog", "Успешно запущен EditGroupActivity - onCreate");
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
            }

            Log.d("myLog", "Создана группа: " + group.getName() + " | " + group.getDescription() + " | " + group.getColor());

            // Добавляем или обновляем группу в базе
            if (mGroup == null) {
                // Добавляем новую группу
                if (!mDbHelper.isGroupExist(name)) {
                    mDbHelper.addGroup(group);
                    closeActivity();
                } else {
                    showErrorMessage();
                }
            } else {
                // Обновляем существующую
                if (mGroup.getName().equals(name)) {
                    mDbHelper.updateGroup(group);
                    closeActivity();
                } else {
                    if (!mDbHelper.isGroupExist(name)) {
                        mDbHelper.updateGroupByName(mGroup.getName(), group);
                        closeActivity();
                    } else {
                        showErrorMessage();
                    }
                }
            }
        }
    }

    private void closeActivity() {
        // Удаляем текущий активити из стека и возвращаемся в список групп
        Intent intent = new Intent(this, GroupListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void showErrorMessage() {
        // TODO: 08.11.2017 Удалить фрагмент и сделать обычное сообщение в виде местного диалога
        DialogFragment groupExistMessageFragment = new GroupExistDialog();
        groupExistMessageFragment.show(getSupportFragmentManager(), "groupExistMessageFragment");
    }
}
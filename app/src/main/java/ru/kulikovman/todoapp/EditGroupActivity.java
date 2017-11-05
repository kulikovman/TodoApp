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


import java.util.UUID;

import ru.kulikovman.todoapp.database.DbHelper;
import ru.kulikovman.todoapp.dialogs.ColorFragment;
import ru.kulikovman.todoapp.dialogs.DescriptionFragment;
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

        // Читаем uuid группы из интента
        UUID uuid = (java.util.UUID) getIntent().getSerializableExtra("group_name");

        // Если uuid не пустой, то получаем соответствующую группу и обновляем поля
        if (uuid != null) {
            mGroup = mDbHelper.getGroupByUUID(uuid);
            loadGroup();
        }

        Log.d("myLog", "Успешно запущен EditGroupActivity - onCreate");
    }

    public void groupOptions(View view) {
        // Обработка нажатий на опции группы
        switch (view.getId()) {
            case R.id.description_layout:
                DialogFragment descriptionFragment = new DescriptionFragment();
                descriptionFragment.show(getSupportFragmentManager(), "descriptionFragment");
                break;
            case R.id.color_layout:
                DialogFragment colorFragment = new ColorFragment();
                colorFragment.show(getSupportFragmentManager(), "colorFragment");
                break;
        }
    }

    private void loadGroup() {
        // Загружаем название и описание
        mGroupName.setText(mGroup.getName());
        mDescriptionState.setText(mGroup.getDescription());

        // Загружаем цвет
        String color = mGroup.getColor();

        if (color == null) {
            mColorState.setText(getString(R.string.color_not_set));
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

        // Если название есть, то делаем все остальное
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

            if (!color.equals(getString(R.string.color_not_set))) {
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

            // Добавляем группу в базу
            if (mGroup == null) {
                mDbHelper.addGroup(group);
            } else {
                mGroup = group;
                mDbHelper.updateGroup(mGroup);
            }


            Log.d("myLog", "Группа добавлена в базу");

            // Удаляем текущий активити из стека и возвращаемся в список групп
            Intent intent = new Intent(this, GroupListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}

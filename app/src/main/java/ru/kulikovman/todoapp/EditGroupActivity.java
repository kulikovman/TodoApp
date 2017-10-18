package ru.kulikovman.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;

import ru.kulikovman.todoapp.database.DbHelper;
import ru.kulikovman.todoapp.dialogs.ColorFragment;
import ru.kulikovman.todoapp.dialogs.DateFragment;
import ru.kulikovman.todoapp.dialogs.DescriptionFragment;
import ru.kulikovman.todoapp.dialogs.PriorityFragment;
import ru.kulikovman.todoapp.dialogs.RepeatFragment;
import ru.kulikovman.todoapp.models.Group;
import ru.kulikovman.todoapp.models.Task;

public class EditGroupActivity extends AppCompatActivity {
    private DbHelper mDbHelper;
    //private Group mGroup;

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

    public void fabSaveGroup(View view) {
        // Получаем название группы
        String name = mGroupName.getText().toString().trim();

        // Если название есть, то делаем все остальное
        if (name.length() > 0) {
            // Получаем описание
            String description = mDescriptionState.getText().toString().trim();
            String color = mColorState.getText().toString();

            // Серый цвет по умолчанию
            int colorId = R.color.gray_2;

            // Если выбран цвет, то сохраняем его
            if (!color.equals(getString(R.string.color_not_set))) {
                if (color.equals(getString(R.string.color_1_red))) {
                    colorId = R.color.red;
                } else if (color.equals(getString(R.string.color_2_orange))) {
                    colorId = R.color.orange;
                } else if (color.equals(getString(R.string.color_3_yellow))) {
                    colorId = R.color.yellow;
                } else if (color.equals(getString(R.string.color_4_green))) {
                    colorId = R.color.green;
                } else if (color.equals(getString(R.string.color_5_blue))) {
                    colorId = R.color.blue;
                } else if (color.equals(getString(R.string.color_6_violet))) {
                    colorId = R.color.violet;
                } else if (color.equals(getString(R.string.color_7_pink))) {
                    colorId = R.color.pink;
                }
            }

            // Создаем группу
            Group group = new Group(name, colorId);

            // Если есть описание, то добавляем его в группу
            if (!description.equals(getString(R.string.without_description))) {
                group.setDescription(description);
            }

            // Добавляем группу в базу
            mDbHelper.addGroup(group);

            // Удаляем текущий активити из стека и возвращаемся в список групп
            Intent intent = new Intent(this, GroupListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}

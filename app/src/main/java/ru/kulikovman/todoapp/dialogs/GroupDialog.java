package ru.kulikovman.todoapp.dialogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import ru.kulikovman.todoapp.R;
import ru.kulikovman.todoapp.database.DbHelper;
import ru.kulikovman.todoapp.models.Group;

public class GroupDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Подключаемся к базе и получаем список групп
        DbHelper dbHelper = new DbHelper(getContext());
        List<Group> groups = dbHelper.getAllGroups();

        // Создаем массив строк и заполняем именами групп

        if (groups == null) {
            // Если список групп пуст, то показываем сообщение
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.group_title)
                    .setMessage(R.string.group_message)
                    .setPositiveButton(R.string.group_button_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Запускаем диалог создания группы
                            DialogFragment createGroupDialog = new CreateGroupDialog();
                            createGroupDialog.show(getActivity().getSupportFragmentManager(), "createGroupDialog");
                        }
                    })
                    .setNegativeButton(R.string.group_button_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            return builder.create();



        } else {
            // Если есть существующие группы, то готовим список
            final String names[] = new String[groups.size() + 1];
            for (int i = 0; i < groups.size(); i++) {
                names[i] = groups.get(i).getName();
            }

            // Добавляем элемент "Без группы"
            names[names.length - 1] = getString(R.string.group_not);

            // Показываем список
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.group_title)
                    .setItems(names, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("myLog", String.valueOf(which));

                            // Находим нужное вью в макете
                            TextView groupState = (TextView) getActivity().findViewById(R.id.group_state);

                            // Задаем этому вью название группы
                            if (which == names.length - 1) {
                                groupState.setText(R.string.group_without);
                            } else {
                                groupState.setText(names[which]);
                            }
                        }
                    })
                    .setPositiveButton(R.string.group_button_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton(R.string.group_button_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            return builder.create();
        }
    }
}

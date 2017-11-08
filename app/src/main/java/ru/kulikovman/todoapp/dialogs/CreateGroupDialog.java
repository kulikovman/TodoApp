package ru.kulikovman.todoapp.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ru.kulikovman.todoapp.R;
import ru.kulikovman.todoapp.database.DbHelper;
import ru.kulikovman.todoapp.models.Group;


public class CreateGroupDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Это нужно для привязки к диалогу вью из макета
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogDescription = inflater.inflate(R.layout.dialog_input_text, null);

        // Инициализируем вью элементы
        final EditText dialogInputText = (EditText) dialogDescription.findViewById(R.id.dialog_input_text);

        // Формируем диалог при помощи конструктора
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.create_group_title)
                .setView(dialogDescription)
                .setPositiveButton(R.string.create_group_button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Получаем введенный текст
                        String groupName = dialogInputText.getText().toString().trim();

                        // Если название введено
                        if (groupName.length() > 0) {
                            // Добавляем группу в базу
                            DbHelper dbHelper = new DbHelper(getActivity());
                            dbHelper.addGroup(new Group(groupName));

                            // Снова вызываем диалог со списком групп
                            // TODO: 08.11.2017 Пока попробую без явного вызова, посмотрим что получится...
                            //DialogFragment groupDialog = new GroupDialog();
                            //groupDialog.show(getActivity().getSupportFragmentManager(), "groupDialog");
                        }
                    }
                })
                .setNegativeButton(R.string.create_group_button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }
}

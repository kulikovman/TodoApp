package ru.kulikovman.todoapp.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import ru.kulikovman.todoapp.R;

public class RepeatFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String priorityList[] = {"Ежедневно", "Каждую неделю", "Раз в месяц", "Через год", "Не повторять"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Повторение")
                .setItems(priorityList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("myLog", String.valueOf(which));

                        TextView repeatField = (TextView) getActivity().findViewById(R.id.repeat_field);

                        switch (which) {
                            case 0:
                                repeatField.setText("Ежедневно");
                                break;
                            case 1:
                                repeatField.setText("Каждую неделю");
                                break;
                            case 2:
                                repeatField.setText("Раз в месяц");
                                break;
                            case 3:
                                repeatField.setText("Через год");
                                break;
                            case 4:
                                repeatField.setText("Без повтора");
                                break;
                        }
                    }
                });

        return builder.create();
    }
}

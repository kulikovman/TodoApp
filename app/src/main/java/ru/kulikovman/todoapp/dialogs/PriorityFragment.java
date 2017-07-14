package ru.kulikovman.todoapp.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import ru.kulikovman.todoapp.R;

public class PriorityFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String priorityList[] = {"Чрезвычайный", "Высокий", "Обычный", "Низкий", "Самый низкий"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Приоритет")
                .setItems(priorityList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("myLog", String.valueOf(which));

                        TextView priorityField = (TextView) getActivity().findViewById(R.id.priority_field);

                        switch (which) {
                            case 0:
                                priorityField.setText("Чрезвычайный");
                                break;
                            case 1:
                                priorityField.setText("Высокий");
                                break;
                            case 2:
                                priorityField.setText("Обычный");
                                break;
                            case 3:
                                priorityField.setText("Низкий");
                                break;
                            case 4:
                                priorityField.setText("Самый низкий");
                                break;
                        }
                    }
                });

        return builder.create();
    }
}

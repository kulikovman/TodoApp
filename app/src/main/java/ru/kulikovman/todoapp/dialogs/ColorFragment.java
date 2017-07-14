package ru.kulikovman.todoapp.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import ru.kulikovman.todoapp.R;

public class ColorFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String priorityList[] = {"Красный", "Оранжевый", "Желтый", "Зеленый", "Синий", "Фиолетовый", "Розовый", "Без цвета"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Цвет группы")
                .setItems(priorityList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("myLog", String.valueOf(which));

                        TextView colorField = (TextView) getActivity().findViewById(R.id.color_field);

                        switch (which) {
                            case 0:
                                colorField.setText("Красный");
                                break;
                            case 1:
                                colorField.setText("Оранжевый");
                                break;
                            case 2:
                                colorField.setText("Желтый");
                                break;
                            case 3:
                                colorField.setText("Зеленый");
                                break;
                            case 4:
                                colorField.setText("Синий");
                                break;
                            case 5:
                                colorField.setText("Фиолетовый");
                                break;
                            case 6:
                                colorField.setText("Розовый");
                                break;
                            case 7:
                                colorField.setText("Не выбран");
                                break;
                        }
                    }
                });

        return builder.create();
    }
}

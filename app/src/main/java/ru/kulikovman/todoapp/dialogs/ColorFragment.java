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
        final String color[] = {"Красный", "Оранжевый", "Желтый", "Зеленый", "Синий", "Фиолетовый", "Розовый", "Без цвета"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.color_dialog_title)
                .setItems(color, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("myLog", String.valueOf(which));

                        TextView colorField = (TextView) getActivity().findViewById(R.id.color_field);

                        switch (which) {
                            case 0:
                                colorField.setText(R.string.color_red);
                                break;
                            case 1:
                                colorField.setText(R.string.color_orange);
                                break;
                            case 2:
                                colorField.setText(R.string.color_yellow);
                                break;
                            case 3:
                                colorField.setText(R.string.color_green);
                                break;
                            case 4:
                                colorField.setText(R.string.color_blue);
                                break;
                            case 5:
                                colorField.setText(R.string.color_violet);
                                break;
                            case 6:
                                colorField.setText(R.string.color_pink);
                                break;
                            case 7:
                                colorField.setText(R.string.color_not_set);
                                break;
                        }
                    }
                });

        return builder.create();
    }
}

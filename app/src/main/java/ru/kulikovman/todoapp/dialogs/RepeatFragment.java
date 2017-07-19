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
        final String priority[] = {"Ежедневно", "Каждую неделю", "Раз в месяц", "Через год", "Не повторять"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.repeat_title)
                .setItems(priority, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("myLog", String.valueOf(which));

                        TextView repeatField = (TextView) getActivity().findViewById(R.id.repeat_field);
                        TextView dateField = (TextView) getActivity().findViewById(R.id.date_field);

                        String date = dateField.getText().toString();

                        if (date.equals("Не установлена")) {
                            repeatField.setText("Без повтора");
                        } else {
                            switch (which) {
                                case 0:
                                    repeatField.setText(priority[0]);
                                    break;
                                case 1:
                                    repeatField.setText(priority[1]);
                                    break;
                                case 2:
                                    repeatField.setText(priority[2]);
                                    break;
                                case 3:
                                    repeatField.setText(priority[3]);
                                    break;
                                case 4:
                                    repeatField.setText(priority[4]);
                                    break;
                            }
                        }
                    }
                });

        return builder.create();
    }
}

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
        final String repeat[] = {"Ежедневно", "Каждую неделю", "Раз в месяц", "Через год", "Не повторять"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.repeat_title)
                .setItems(repeat, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("myLog", String.valueOf(which));

                        TextView repeatField = (TextView) getActivity().findViewById(R.id.repeat_field);
                        TextView dateField = (TextView) getActivity().findViewById(R.id.date_field);

                        String date = dateField.getText().toString();

                        if (date.equals("Не установлена")) {
                            repeatField.setText(R.string.repeat_not_set);
                        } else {
                            switch (which) {
                                case 0:
                                    repeatField.setText(R.string.repeat_daily);
                                    break;
                                case 1:
                                    repeatField.setText(R.string.repeat_every_week);
                                    break;
                                case 2:
                                    repeatField.setText(R.string.repeat_once_a_month);
                                    break;
                                case 3:
                                    repeatField.setText(R.string.repeat_in_a_year);
                                    break;
                                case 4:
                                    repeatField.setText(R.string.repeat_do_not);
                                    break;
                            }
                        }
                    }
                });

        return builder.create();
    }
}

package ru.kulikovman.todoapp.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import ru.kulikovman.todoapp.R;

public class RepeatDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String day = getString(R.string.repeat_day);
        final String week = getString(R.string.repeat_week);
        final String month = getString(R.string.repeat_month);
        final String year = getString(R.string.repeat_year);
        final String not = getString(R.string.repeat_not);

        final String repeat[] = {day, week, month, year, not};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.repeat_title)
                .setItems(repeat, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("myLog", String.valueOf(which));

                        TextView repeatField = (TextView) getActivity().findViewById(R.id.repeat_state);
                        TextView dateField = (TextView) getActivity().findViewById(R.id.date_state);

                        String date = dateField.getText().toString();

                        if (date.equals("Не установлена")) {
                            repeatField.setText(R.string.repeat_without);
                        } else {
                            switch (which) {
                                case 0:
                                    repeatField.setText(day);
                                    break;
                                case 1:
                                    repeatField.setText(week);
                                    break;
                                case 2:
                                    repeatField.setText(month);
                                    break;
                                case 3:
                                    repeatField.setText(year);
                                    break;
                                case 4:
                                    repeatField.setText(not);
                                    break;
                            }
                        }
                    }
                });

        return builder.create();
    }
}

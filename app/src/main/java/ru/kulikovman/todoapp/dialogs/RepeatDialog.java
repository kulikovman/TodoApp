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

                        TextView repeatState = (TextView) getActivity().findViewById(R.id.repeat_state);
                        TextView reminderState = (TextView) getActivity().findViewById(R.id.reminder_state);

                        if (which == repeat.length - 1) {
                            repeatState.setText(R.string.repeat_without);
                        } else {
                            repeatState.setText(repeat[which]);
                        }

                        String reminder = reminderState.getText().toString();

                        switch (which) {
                            case 0:
                                repeatState.setText(day);
                                if (reminder.equals(getString(R.string.reminder_month_before)) ||
                                        reminder.equals(getString(R.string.reminder_week_before)) ||
                                        reminder.equals(getString(R.string.reminder_two_day_before)) ||
                                        reminder.equals(getString(R.string.reminder_day_before))) {
                                    reminderState.setText(R.string.reminder_without);
                                }
                                break;
                            case 1:
                                repeatState.setText(week);
                                if (reminder.equals(getString(R.string.reminder_month_before)) ||
                                        reminder.equals(getString(R.string.reminder_week_before))) {
                                    reminderState.setText(R.string.reminder_without);
                                }
                                break;
                            case 2:
                                repeatState.setText(month);
                                if (reminder.equals(getString(R.string.reminder_month_before))) {
                                    reminderState.setText(R.string.reminder_without);
                                }
                                break;
                            case 3:
                                repeatState.setText(year);
                                break;
                            case 4:
                                repeatState.setText(R.string.repeat_without);
                                break;
                        }
                    }
                });

        return builder.create();
    }
}

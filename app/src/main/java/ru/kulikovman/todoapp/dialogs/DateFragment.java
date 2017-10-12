package ru.kulikovman.todoapp.dialogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ru.kulikovman.todoapp.R;

public class DateFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String today = getString(R.string.date_today);
        final String tomorrow = getString(R.string.date_tomorrow);
        final String pickDate = getString(R.string.date_pick_date);
        final String withoutDate = getString(R.string.date_without_date);

        final String date[] = {today, tomorrow, pickDate, withoutDate};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.date_title)
                .setItems(date, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("myLog", String.valueOf(which));

                        TextView dateField = (TextView) getActivity().findViewById(R.id.date_state);
                        TextView repeatField = (TextView) getActivity().findViewById(R.id.repeat_state);

                        Calendar c = Calendar.getInstance();
                        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

                        switch (which) {
                            case 0:
                                String dateToday = dateFormat.format(c.getTime());
                                dateField.setText(dateToday);
                                break;
                            case 1:
                                c.add(Calendar.DATE, 1);
                                String dateTomorrow = dateFormat.format(c.getTime());
                                dateField.setText(dateTomorrow);
                                break;
                            case 2:
                                DialogFragment datePickerFragment = new DatePickerFragment();
                                datePickerFragment.show(getFragmentManager(), "datePicker");
                                break;
                            case 3:
                                dateField.setText(R.string.date_not_set);
                                repeatField.setText(R.string.repeat_not_set);
                                break;
                        }
                    }
                });

        return builder.create();
    }
}

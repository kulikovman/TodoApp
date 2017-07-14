package ru.kulikovman.todoapp.dialogs;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import ru.kulikovman.todoapp.R;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = new GregorianCalendar(view.getYear(), view.getMonth(), view.getDayOfMonth());
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        String date = dateFormat.format(calendar.getTime());

        TextView dateField = (TextView) getActivity().findViewById(R.id.date_field);
        dateField.setText(date);
    }
}

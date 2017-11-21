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

import ru.kulikovman.todoapp.Helper;
import ru.kulikovman.todoapp.R;

public class TaskDatePickerDialog extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Получаем сегодняшнюю дату
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Создаем и возвращаем новый DatePickerDialog
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, year, month, day);
        datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());

        return datePicker;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Получаем выбранную дату
        Calendar calendar = new GregorianCalendar(view.getYear(), view.getMonth(), view.getDayOfMonth());

        // Инициализируем вью и записываем в него дату
        TextView dateState = (TextView) getActivity().findViewById(R.id.date_state);
        dateState.setText(Helper.convertCalendarToTextDate(calendar));
    }
}

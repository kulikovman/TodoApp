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
        String priorityList[] = {"Сегодня", "Завтра", "Выбрать дату", "Без даты"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Дата выполнения")
                .setItems(priorityList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("myLog", String.valueOf(which));

                        TextView dateField = (TextView) getActivity().findViewById(R.id.date_field);

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
                                dateField.setText("Не установлена");
                                break;
                        }
                    }
                });

        return builder.create();
    }
}

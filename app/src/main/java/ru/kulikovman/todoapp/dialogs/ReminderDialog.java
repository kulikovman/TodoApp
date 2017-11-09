package ru.kulikovman.todoapp.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;

import ru.kulikovman.todoapp.Helper;
import ru.kulikovman.todoapp.R;


public class ReminderDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String onTheDay = getString(R.string.reminder_on_the_day);
        String dayBefore = getString(R.string.reminder_day_before);
        String weekBefore = getString(R.string.reminder_week_before);
        String monthBefore = getString(R.string.reminder_month_before);
        String pickDate = getString(R.string.reminder_pick_date);
        String not = getString(R.string.reminder_not);

        final String reminder[] = {onTheDay, dayBefore, weekBefore, monthBefore, pickDate, not};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.reminder_title)
                .setItems(reminder, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("myLog", String.valueOf(which));

                        // Инициализируем нужные вью
                        TextView dateState = (TextView) getActivity().findViewById(R.id.date_state);
                        TextView reminderState = (TextView) getActivity().findViewById(R.id.reminder_state);

                        // Получаем дату задачи
                        String selectedDate = dateState.getText().toString();

                        // Записываем выбранную дату в поле
                        if (!selectedDate.equals(getString(R.string.date_without))) {
                            // Если у задачи нет даты, то показываем сообщение
                            // ...
                        } else {
                            // Если дата есть, то преобразуем ее из текста в календарь
                            Calendar calendar = Helper.convertTextDateToCalendar(selectedDate);

                            // Записываем в поле выбранную дату
                            switch (which) {
                                case 0:
                                    reminderState.setText(selectedDate);
                                    break;
                                case 1:
                                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                                    reminderState.setText(Helper.convertCalendarToText(calendar));
                                    break;
                                case 2:
                                    calendar.add(Calendar.WEEK_OF_YEAR, -1);
                                    reminderState.setText(Helper.convertCalendarToText(calendar));
                                    break;
                                case 3:
                                    calendar.add(Calendar.MONTH, -1);
                                    reminderState.setText(Helper.convertCalendarToText(calendar));
                                    break;
                                case 4:
                                    // Запуск диалога выбора даты напоминания
                                    DialogFragment datePickerFragment = new DatePickerDialog();
                                    datePickerFragment.show(getFragmentManager(), "datePicker");
                                    break;
                                case 5:
                                    reminderState.setText(R.string.reminder_not);
                                    break;
                            }
                        }
                    }
                });

        return builder.create();
    }
}

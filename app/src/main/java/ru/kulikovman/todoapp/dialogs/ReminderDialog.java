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
import ru.kulikovman.todoapp.messages.CanNotSetReminder;

public class ReminderDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Строки для формирования списка
        String onTheDay = getString(R.string.reminder_on_the_day);
        String dayBefore = getString(R.string.reminder_day_before);
        String weekBefore = getString(R.string.reminder_week_before);
        String monthBefore = getString(R.string.reminder_month_before);
        String pickDate = getString(R.string.reminder_pick_date);
        String not = getString(R.string.reminder_not);

        // Инициализируем нужные вью
        final TextView dateState = (TextView) getActivity().findViewById(R.id.date_state);
        final TextView reminderState = (TextView) getActivity().findViewById(R.id.reminder_state);

        // Получаем дату задачи и сегодняшнюю дату
        final String date = dateState.getText().toString();
        final Calendar taskDate = Helper.convertTextDateToCalendar(date);
        Calendar todayDate = Helper.getTodayRoundCalendar();

        // Вычисляем количество дней с сегодня - до даты задачи
        int daysBeforeTarget = (int) ((taskDate.getTimeInMillis() - todayDate.getTimeInMillis()) / 1000 / 60 / 60 / 24);
        Log.d("log", "Дней до даты выполнения задачи: " + daysBeforeTarget);

        // Формируем диалог с корректным списком
        if (daysBeforeTarget == 0) { // У задачи сегодняшняя дата
            final String reminder[] = {onTheDay, not};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.reminder_title)
                    .setItems(reminder, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("myLog", String.valueOf(which));

                            // Записываем в поле выбранную дату
                            switch (which) {
                                case 0: // В день выполнения
                                    reminderState.setText(date);
                                    break;
                                case 1: // Без напоминания
                                    reminderState.setText(R.string.reminder_not);
                                    break;
                            }
                        }
                    });
            return builder.create();

        } else if (daysBeforeTarget == 1) { // Задача на завтра
            final String reminder[] = {onTheDay, dayBefore, not};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.reminder_title)
                    .setItems(reminder, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("myLog", String.valueOf(which));

                            // Записываем в поле выбранную дату
                            switch (which) {
                                case 0: // В день выполнения
                                    reminderState.setText(date);
                                    break;
                                case 1: // За день до выполнения
                                    taskDate.add(Calendar.DAY_OF_YEAR, -1);
                                    reminderState.setText(Helper.convertCalendarToLongTextDate(taskDate));
                                    break;
                                case 2: // Без напоминания
                                    reminderState.setText(R.string.reminder_not);
                                    break;
                            }
                        }
                    });
            return builder.create();

        } else if (daysBeforeTarget <= 7) { // Меньше недели
            final String reminder[] = {onTheDay, dayBefore, pickDate, not};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.reminder_title)
                    .setItems(reminder, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("myLog", String.valueOf(which));

                            // Записываем в поле выбранную дату
                            switch (which) {
                                case 0: // В день выполнения
                                    reminderState.setText(date);
                                    break;
                                case 1: // За день до выполнения
                                    taskDate.add(Calendar.DAY_OF_YEAR, -1);
                                    reminderState.setText(Helper.convertCalendarToLongTextDate(taskDate));
                                    break;
                                case 2: // Выбор даты
                                    DialogFragment reminderDatePicker = new ReminderDatePickerDialog();
                                    reminderDatePicker.show(getActivity().getSupportFragmentManager(), "reminderDatePicker");
                                case 3: // Без напоминания
                                    reminderState.setText(R.string.reminder_not);
                                    break;
                            }
                        }
                    });
            return builder.create();

        } else if (daysBeforeTarget <= 31) { // Меньше месяца
            final String reminder[] = {onTheDay, dayBefore, weekBefore, pickDate, not};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.reminder_title)
                    .setItems(reminder, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("myLog", String.valueOf(which));

                            // Записываем в поле выбранную дату
                            switch (which) {
                                case 0: // В день выполнения
                                    reminderState.setText(date);
                                    break;
                                case 1: // За день
                                    taskDate.add(Calendar.DAY_OF_YEAR, -1);
                                    reminderState.setText(Helper.convertCalendarToLongTextDate(taskDate));
                                    break;
                                case 2: // За неделю
                                    taskDate.add(Calendar.WEEK_OF_YEAR, -1);
                                    reminderState.setText(Helper.convertCalendarToLongTextDate(taskDate));
                                    break;
                                case 3: // Выбор даты
                                    DialogFragment reminderDatePickerDialog = new ReminderDatePickerDialog();
                                    reminderDatePickerDialog.show(getActivity().getSupportFragmentManager(), "reminderDatePickerDialog");
                                case 4: // Без напоминания
                                    reminderState.setText(R.string.reminder_not);
                                    break;
                            }
                        }
                    });
            return builder.create();
        }

        // Если ни одно из условий не сработало, то формируем полный список
        final String reminder[] = {onTheDay, dayBefore, weekBefore, monthBefore, pickDate, not};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.reminder_title)
                .setItems(reminder, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("myLog", String.valueOf(which));

                        // Записываем в поле выбранную дату
                        switch (which) {
                            case 0: // В день выполнения
                                reminderState.setText(date);
                                break;
                            case 1: // За день
                                taskDate.add(Calendar.DAY_OF_YEAR, -1);
                                reminderState.setText(Helper.convertCalendarToLongTextDate(taskDate));
                                break;
                            case 2: // За неделю
                                taskDate.add(Calendar.WEEK_OF_YEAR, -1);
                                reminderState.setText(Helper.convertCalendarToLongTextDate(taskDate));
                                break;
                            case 3: // За месяц
                                taskDate.add(Calendar.MONTH, -1);
                                reminderState.setText(Helper.convertCalendarToLongTextDate(taskDate));
                                break;
                            case 4: // Выбор даты
                                DialogFragment reminderDatePickerDialog = new ReminderDatePickerDialog();
                                reminderDatePickerDialog.show(getActivity().getSupportFragmentManager(), "reminderDatePickerDialog");
                            case 5: // Без напоминания
                                reminderState.setText(R.string.reminder_not);
                                break;
                        }
                    }
                });

        return builder.create();
    }
}

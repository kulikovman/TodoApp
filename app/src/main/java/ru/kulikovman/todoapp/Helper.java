package ru.kulikovman.todoapp;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Helper {
    private static DateFormat mDateFormat;

    static {
        mDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    }

    public static Calendar convertTextDateToCalendar(String sourceDate) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(mDateFormat.parse(sourceDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    public static String convertCalendarToText(Calendar calendar) {
        return mDateFormat.format(calendar.getTime());
    }
}

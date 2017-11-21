package ru.kulikovman.todoapp;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Helper {
    private static DateFormat mLongDateFormat;
    private static DateFormat mShortDateFormat;

    static {
        mLongDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        mShortDateFormat = new SimpleDateFormat("dd MMMM", Locale.getDefault());
    }

    public static Calendar convertTextDateToCalendar(String textDate) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(mLongDateFormat.parse(textDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    public static long convertLongTextDateToLong(String longTextDate) {
        long date = 0;
        try {
            date = mLongDateFormat.parse(longTextDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String convertCalendarToLongTextDate(Calendar calendar) {
        return mLongDateFormat.format(calendar.getTime());
    }

    public static String convertLongToLongTextDate(long longDate) {
        return mLongDateFormat.format(new Date(longDate));
    }

    public static String convertLongToShortTextDate(long longDate) {
        return mShortDateFormat.format(new Date(longDate));
    }

    public static Calendar getTodayRoundCalendar() {
        String date = convertLongToLongTextDate(Calendar.getInstance().getTimeInMillis());
        return convertTextDateToCalendar(date);
    }

}

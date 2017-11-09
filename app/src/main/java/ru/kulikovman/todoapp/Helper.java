package ru.kulikovman.todoapp;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Helper {
    public static Calendar convertTextDateToCalendar(String sourceDate) {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        try {
            calendar.setTime(dateFormat.parse(sourceDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }
}

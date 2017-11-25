package ru.kulikovman.todoapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class TimeNotification extends BroadcastReceiver {
    private NotificationManager mNotificationManager;
    private final int NOTIFICATION_ID = 127;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("log", "Запущен onReceive в TimeNotification");

        // Инициализация уведомления
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Создание уведомления
        Notification.Builder builder = new Notification.Builder(context);

        Intent finishIntent = new Intent(context, TaskListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, finishIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_event_24dp)
                .setTicker("Есть задачи")
                .setAutoCancel(true)
                .setContentTitle("Задачи на сегодня")
                .setContentText("- Помыть кота\n- Погладить кота");

        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_ALL;

        // Запуск уведомления
        mNotificationManager.notify(NOTIFICATION_ID, notification);



        /*Notification notification = new Notification(R.drawable.ic_event_available_24dp, "Test", System.currentTimeMillis());
        notification.notify();*/

        /*PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);


        //Интент для активити, которую мы хотим запускать при нажатии на уведомление
        Intent intentTL = new Intent(context, TaskListActivity.class);
        notification.setLatestEventInfo(context, "Test", "Do something!",
                PendingIntent.getActivity(context, 0, intentTL, PendingIntent.FLAG_CANCEL_CURRENT));

        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(1, notification);*/

        // Установим следующее напоминание.
        /*AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmManager.INTERVAL_DAY, pendingIntent);*/
    }

    public void showNotification(View view) {

    }
}

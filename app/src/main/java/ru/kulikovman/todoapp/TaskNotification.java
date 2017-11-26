package ru.kulikovman.todoapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.kulikovman.todoapp.models.Task;

public class TaskNotification extends BroadcastReceiver {
    private NotificationManager mNotificationManager;
    private final int NOTIFICATION_ID = 125;
    private Realm mRealm;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("log", "Запущен onReceive в TaskNotification");

        // Получаем список задач на сегодня с напоминаниями
        Calendar calendar = Calendar.getInstance();

        mRealm = Realm.getDefaultInstance();
        RealmResults<Task> tasks = mRealm.where(Task.class)
                .equalTo(Task.REMINDER, true)
                .lessThan(Task.TARGET_DATE, calendar.getTimeInMillis())
                .findAll();

        // Если список не пустой
        if (tasks.size() != 0) {
            // Формируем заголовок и сообщение
            String title, message;

            if (tasks.size() == 1) {
                title = "Напоминание о задаче";
                message = tasks.get(0).getTitle();
            } else {
                title = "Напоминание о задачах - " + tasks.size() + " шт.";
                message = "Нажмите, для просмотра";
            }

            // Инициализация уведомления
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Создание уведомления
            Notification.Builder builder = new Notification.Builder(context);

            Intent finishIntent = new Intent(context, TaskListActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, finishIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_event_24dp)
                    .setTicker("Задача на сегодня")
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(message);

            Notification notification = builder.build();
            notification.defaults = Notification.DEFAULT_ALL;

            // Запуск уведомления
            mNotificationManager.notify(NOTIFICATION_ID, notification);
        }

        // Ставим следующее напоминание
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
}

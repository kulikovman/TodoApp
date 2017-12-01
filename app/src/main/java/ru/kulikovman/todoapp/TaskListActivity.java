package ru.kulikovman.todoapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.Sort;
import ru.kulikovman.todoapp.adapters.TaskRealmAdapter;
import ru.kulikovman.todoapp.models.Task;

public class TaskListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TaskRealmAdapter.OnItemClickListener {

    private SharedPreferences mSharedPref;

    private RecyclerView mRecyclerView;
    private TaskRealmAdapter mAdapter;
    private Realm mRealm;

    private AlarmManager mAlarmManager;
    private Calendar mNotifyTime;

    private Task mTask;
    private String mTaskId;
    private int mPosition;
    private View mItemView;

    private List<Task> mAllTasks;
    private List<Task> mCurrentTasks;
    private String mTypeList;

    private FloatingActionButton mDeleteButton, mEditButton, mDoneButton;
    private TextView mNumberOfTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        Realm.init(this);

        // Судя по всему, это код запуска бокового меню и сопутствующих элементов
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Подключаемся к базе данных
        mRealm = Realm.getDefaultInstance();

        /*// Инициализируем поле в хедере для показа количества задач
        View header = navigationView.getHeaderView(0);
        mNumberOfTasks = (TextView) header.findViewById(R.id.number_of_tasks);*/

        // Инициализируем необходимые вью элементы
        mDeleteButton = (FloatingActionButton) findViewById(R.id.fab_delete_task);
        mEditButton = (FloatingActionButton) findViewById(R.id.fab_edit_task);
        mDoneButton = (FloatingActionButton) findViewById(R.id.fab_done_task);
        mRecyclerView = (RecyclerView) findViewById(R.id.task_list_recycler_view);

        // Получаем SharedPreferences и восстанавливаем тип списка задач
        mSharedPref = getSharedPreferences(getString(R.string.pref_key), Context.MODE_PRIVATE);
        mTypeList = mSharedPref.getString(getString(R.string.type_list), getString(R.string.list_unfinished));

        // Создаем и запускаем список
        setUpRecyclerView();

        // Запускаем уведомления
        //setNotifyTime();
        //restartNotify();

        Log.d("log", "Завершен onCreate в TaskListActivity");
    }

    private void setUpRecyclerView() {
        mAdapter = new TaskRealmAdapter(this, loadUnfinishedTasks());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        // Слушатель для адаптера списка
        mAdapter.setOnItemClickListener(this);
    }

    private OrderedRealmCollection<Task> loadUnfinishedTasks() {
        return mRealm.where(Task.class)
                .equalTo(Task.DONE, false)
                .findAll()
                .sort(new String[]{Task.TARGET_DATE, Task.PRIORITY, Task.TITLE},
                        new Sort[] {Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING});
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("log", "Запущен onPause в TaskListActivity");

        /*// Сохраняем тип текущего списка задач
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(getString(R.string.type_list), mTypeList);
        editor.apply();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecyclerView.setAdapter(null);
        mRealm.close();
    }

    @Override
    public void onBackPressed() {
        // Если открыто боковое меню, то сначала оно скроется
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.здесь_вписать_имя_меню, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Если выбран пункт редактирования групп
        if (id == R.id.nav_edit_group) {
            Intent intent = new Intent(this, GroupListActivity.class);
            startActivity(intent);
        }

        // Если выбран вид списка, то присваиваем списку тип
        if (id == R.id.nav_task_today) {
            mTypeList = getString(R.string.list_today);
        } else if (id == R.id.nav_task_month) {
            mTypeList = getString(R.string.list_month);
        } else if (id == R.id.nav_task_without_date) {
            mTypeList = getString(R.string.list_without_date);
        } else if (id == R.id.nav_task_unfinished) {
            mTypeList = getString(R.string.list_unfinished);
        } else if (id == R.id.nav_task_finished) {
            mTypeList = getString(R.string.list_finished);
        }

        //updateTaskList();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(View itemView, int itemPosition, Task task, int selectedPosition) {
        // Показываем или скрываем кнопки в зависимости от выделения элементов списка
        if (selectedPosition != RecyclerView.NO_POSITION) {
            showActionButton();
        } else {
            hideActionButtons();
        }

        // Запоминаем последний выбранный элемент
        mTask = task;
        mPosition = itemPosition;
    }

    /*private void updateTaskList() {
        // Создаем нужный тип списка задач
        mCurrentTasks = createList(mTypeList);

        // Сортируем список через компаратор
        Collections.sort(mCurrentTasks, new TaskComparator());

        // Назначаем адаптеру новый список
        if (mAdapter == null) {
            mAdapter = new TaskAdapter(this, mCurrentTasks);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.resetSelection();
            mAdapter.setTasks(mCurrentTasks);
            mAdapter.notifyDataSetChanged();
        }

        // TODO: 29.09.2017  Эту часть кода нужно проверить и переделать
        // раньше она снимала выделение и скрывала кнопки
        // сейчас выделение элементов работает по другому
        finishAction();
    }*/

    /*private void setNumberOfTasks() {
        // Метод для установки количества задач в заголовке меню
        int taskCounter = 0;
        for (Task task : mAllTasks) {
            if (!task.isDone()) {
                taskCounter++;
            }
        }

        String numberOfTasks = "Всего задач: " + taskCounter;
        mNumberOfTasks.setText(numberOfTasks);
    }*/

    public void fabDoneTask(View view) {
        String repeat = mTask.getRepeat();

        // Если есть повтор, создаем новую задачу
        if (repeat != null){
            Task task = new Task(mTask.getTitle());
            task.setPriority(mTask.getPriority());
            task.setGroup(mTask.getGroup());
            task.setRepeat(mTask.getRepeat());
            task.setReminder(mTask.getReminder());

            Calendar calendar = Helper.convertLongToCalendar(mTask.getTargetDate());

            if (repeat.equals("day")) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            } else if (repeat.equals("week")) {
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            } else if (repeat.equals("month")) {
                calendar.add(Calendar.MONTH, 1);
            } else if (repeat.equals("year")) {
                calendar.add(Calendar.YEAR, 1);
            }

            task.setTargetDate(calendar.getTimeInMillis());

            mRealm.beginTransaction();
            mRealm.insert(task);
            mRealm.commitTransaction();
        }

        // Завершаем выбранную задачу
        mRealm.beginTransaction();
        mTask.setCompletionDate(System.currentTimeMillis());
        mTask.setDone(true);
        mRealm.commitTransaction();

        // Сопутствующие операции
        mAdapter.notifyItemRemoved(mPosition);
        mAdapter.resetSelection();
        hideActionButtons();
    }

    public void fabEditTask(View view) {
        // Открываем активити редактирования задачи и передаем id задачи
        Intent intent = new Intent(this, TaskEditActivity.class);
        intent.putExtra("task_id", mTask.getId());
        Log.d("log", "Передаваемый id: " + mTask.getId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void fabDeleteTask(View view) {
        mRealm.beginTransaction();
        mTask.deleteFromRealm();
        mRealm.commitTransaction();

        // Сопутствующие операции
        mAdapter.notifyItemRemoved(mPosition);
        mAdapter.resetSelection();
        hideActionButtons();
    }

    public void fabAddTask(View view) {
        // Просто открываем активити
        Intent intent = new Intent(this, TaskEditActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    /*private void finishAction() {
        // Убираем выделение и скрываем кнопки действий
        if (mItemView != null) {
            mItemView.setBackgroundColor(Color.TRANSPARENT);
        }
        hideActionButtons();
        setNumberOfTasks();
    }*/

    private void hideActionButtons() {
        mDeleteButton.setVisibility(View.INVISIBLE);
        mEditButton.setVisibility(View.INVISIBLE);
        mDoneButton.setVisibility(View.INVISIBLE);
    }

    private void showActionButton() {
        mDeleteButton.setVisibility(View.VISIBLE);
        mEditButton.setVisibility(View.VISIBLE);
        mDoneButton.setVisibility(View.VISIBLE);
    }

    private void setNotifyTime() {
        mNotifyTime = Calendar.getInstance();
        int hour = mNotifyTime.get(Calendar.HOUR_OF_DAY);

        if (hour >= 11) {
            mNotifyTime.add(Calendar.DAY_OF_YEAR, 1);
        }

        mNotifyTime.set(Calendar.HOUR_OF_DAY, 11);
        mNotifyTime.set(Calendar.MINUTE, 0);
        mNotifyTime.set(Calendar.SECOND, 0);
    }

    private void restartNotify() {
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, TaskNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Если ранее запускали активити, а потом поменяли время, откажемся от уведомления
        mAlarmManager.cancel(pendingIntent);

        // Устанавливаем разовое напоминание
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, mNotifyTime.getTimeInMillis(), pendingIntent);

        Log.d("log", "Завершен restartNotify в TaskListActivity");
        Log.d("log", "Время уведомления: " + mNotifyTime.getTime());
    }
}

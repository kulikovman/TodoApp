package ru.kulikovman.todoapp.models;

import java.util.Comparator;

import ru.kulikovman.todoapp.models.Task;


public class TaskComparator implements Comparator<Task> {

    @Override
    public int compare(Task task1, Task task2) {
        // Первый этап сортировки - дата
        int result = task1.getDate().compareTo(task2.getDate());
        if (result != 0) {
            return result / Math.abs(result);
        }

        // Второй этап сортировки - приоритет
        result = task1.getPriority().compareTo(task2.getPriority());
        if (result != 0) {
            return result / Math.abs(result);
        }

        // Третий этап сортировки - цвет
        result = task1.getColor().compareTo(task2.getColor());
        if (result != 0) {
            return result / Math.abs(result);
        }

        // Завершающий этап сортировки - заголовок
        result = task1.getTitle().compareTo(task2.getTitle());

        return result != 0 ? result / Math.abs(result) : 0;
    }
}

package ru.kulikovman.todoapp;

import java.util.Comparator;

import ru.kulikovman.todoapp.models.Task;

public class TaskComparator implements Comparator<Task> {

    @Override
    public int compare(Task t1, Task t2) {
        // Сортировка по дате
        int result = t1.getTargetDate() < t2.getTargetDate() ? -1 : t1.getTargetDate() == t1.getTargetDate() ? 0 : 1;
        if (result != 0) {
            return result / Math.abs(result);
        }

        // Сортировка по приоритету
        result = t1.getPriority() < t2.getPriority() ? -1 : t1.getPriority() == t1.getPriority() ? 0 : 1;
        if (result != 0) {
            return result / Math.abs(result);
        }

        /*// Сортировка по группе
        result = t1.getGroup().getName().compareTo(t2.getGroup().getName());
        if (result != 0) {
            return result / Math.abs(result);
        }*/

        // Сортировка по алфавиту
        result = t1.getTitle().compareTo(t2.getTitle());

        return result != 0 ? result / Math.abs(result) : 0;
    }
}

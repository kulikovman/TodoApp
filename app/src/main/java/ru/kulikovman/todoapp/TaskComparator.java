package ru.kulikovman.todoapp;

import java.util.Comparator;

import ru.kulikovman.todoapp.models.Task;


public class TaskComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Task task1 = (Task) o1;
        Task task2 = (Task) o2;

        int result = task1.getDate().compareTo(task2.getDate());
        if (result != 0) {
            return result / Math.abs(result);
        }

        result = task1.getPriority().compareTo(task2.getPriority());
        if (result != 0) {
            return result / Math.abs(result);
        }

        result = task1.getTitle().compareTo(task2.getTitle());

        return result != 0 ? result / Math.abs(result) : 0;
    }
}

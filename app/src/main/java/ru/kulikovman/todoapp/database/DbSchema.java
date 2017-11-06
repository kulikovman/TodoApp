package ru.kulikovman.todoapp.database;


public class DbSchema {
    public static final class TaskTable {
        public static final String NAME = "tasks";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String PRIORITY = "priority";
            public static final String DONE = "done";
            public static final String CREATE_DATE = "create_date";
            public static final String TARGET_DATE = "target_date";
            public static final String COMPLETION_DATE = "completion_date";
            public static final String REPEAT_DATE = "repeat_date";
            public static final String REMINDER_DATE = "reminder_date";
            public static final String GROUP = "group";
        }
    }

    public static final class GroupTable {
        public static final String NAME = "groups";

        public static final class Cols {
            public static final String NAME = "title";
            public static final String COLOR = "color";
            public static final String DESCRIPTION = "description";
        }
    }
}

package ru.kulikovman.todoapp.models;


import io.realm.RealmObject;

public class Group extends RealmObject {
    private String mName;
    private String mDescription;
    private String mColor;

    public static final String NAME = "mName";
    public static final String DESCRIPTION = "mDescription";
    public static final String COLOR = "mColor";

    public Group(String name, String description, String color) {
        mName = name;
        mDescription = description;
        mColor = color;
    }

    public Group(String name, String color) {
        mName = name;
        mColor = color;
    }

    public Group(String name) {
        mName = name;
    }

    public Group() {
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}

package ru.kulikovman.todoapp.models;


import java.util.UUID;

public class Group {
    private UUID mId;
    private String mName;
    private String mDescription;
    private String mColor;

    public Group(UUID id, String name, String description, String color) {
        mId = id;
        mName = name;
        mDescription = description;
        mColor = color;
    }

    public Group(String name, String color) {
        mName = name;
        mColor = color;
    }

    public Group(String name) {
        mId = UUID.randomUUID();
        mName = name;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
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

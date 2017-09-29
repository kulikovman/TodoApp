package ru.kulikovman.todoapp.models;


public class Group {
    private String mName;
    private int mColorId;

    public Group(String name, int colorId) {
        mName = name;
        mColorId = colorId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getColorId() {
        return mColorId;
    }

    public void setColorId(int colorId) {
        mColorId = colorId;
    }
}

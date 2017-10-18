package ru.kulikovman.todoapp.models;


import ru.kulikovman.todoapp.R;

public class Group {
    private String mName;
    private String mDescription;
    private int mColorId;

    public Group(String name, String description, int colorId) {
        mName = name;
        mDescription = description;
        mColorId = colorId;
    }

    public Group(String name, int colorId) {
        mName = name;
        mColorId = colorId;
    }

    public Group(String name) {
        mName = name;
        mColorId = R.color.gray_2;
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}

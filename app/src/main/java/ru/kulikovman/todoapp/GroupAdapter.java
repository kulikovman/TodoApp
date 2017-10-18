package ru.kulikovman.todoapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import ru.kulikovman.todoapp.models.Group;


public class GroupAdapter extends BaseAdapter {
    private Context mContext;
    private List<Group> mGroups;
    private LayoutInflater mLayoutInflater;

    public GroupAdapter(Context context, List<Group> groups) {
        mContext = context;
        mGroups = groups;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mGroups.size();
    }

    @Override
    public Object getItem(int position) {
        return mGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_group, parent, false);
        }

        TextView name = (TextView) view.findViewById(R.id.item_group_name);
        TextView description = (TextView) view.findViewById(R.id.item_group_description);
        ImageButton color = (ImageButton) view.findViewById(R.id.item_group_color);

        Group group = (Group) getItem(position);

        name.setText(group.getName());
        description.setText(group.getDescription());
        color.setBackgroundResource(group.getColorId());

        return view;
    }
}

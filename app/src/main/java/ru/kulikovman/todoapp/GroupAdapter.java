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
    private List<Group> mGroups;
    private LayoutInflater mLayoutInflater;

    public GroupAdapter(Context context, List<Group> groups) {
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
        ViewHolder holder;

        if (convertView == null) {
            // Привязываем к вью макет элемента
            convertView = mLayoutInflater.inflate(R.layout.item_group, parent, false);

            holder = new ViewHolder();

            // Инициализируем необходимые вью элементы
            holder.name = (TextView) convertView.findViewById(R.id.item_group_name);
            holder.description = (TextView) convertView.findViewById(R.id.item_group_description);
            holder.color = (ImageButton) convertView.findViewById(R.id.item_group_color);

            // Сохраняем ссылку на ViewHolder
            convertView.setTag(holder);
        }
        else {
            // Получаем ссылку на ViewHolder
            holder = (ViewHolder) convertView.getTag();
        }

        // Получаем нужную группу
        Group group = (Group) getItem(position);

        // Устанавливаем значения во вью элементы
        holder.name.setText(group.getName());
        holder.description.setText(group.getDescription());
        holder.color.setBackgroundResource(group.getColorId());

        return convertView;

    }

    // Здесь храним ссылки для используемых вью элементов
    private static class ViewHolder {
        private TextView name;
        private TextView description;
        private ImageButton color;
    }
}

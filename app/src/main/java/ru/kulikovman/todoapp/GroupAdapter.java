package ru.kulikovman.todoapp;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import ru.kulikovman.todoapp.models.Group;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupHolder> {
    private static GroupAdapter.OnItemClickListener mListener;
    private Context mContext;
    private List<Group> mGroups;

    private int mPosition = RecyclerView.NO_POSITION;

    public class GroupHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mGroupName, mGroupDescription;
        private ImageButton mGroupColor;
        private Group mGroup;

        public GroupHolder(View view) {
            super(view);

            // Слушатель нажатий по элементу
            view.setOnClickListener(this);

            // Инициализируем вью элемента списка
            mGroupName = (TextView) view.findViewById(R.id.item_group_name);
            mGroupDescription = (TextView) view.findViewById(R.id.item_group_description);
            mGroupColor = (ImageButton) view.findViewById(R.id.item_group_color);
        }

        @Override
        public void onClick(View v) {
            // Обновляем айтем нажатый ранее
            notifyItemChanged(mPosition);

            // Сохраняем старую и получаем новую позицию
            int oldPosition = mPosition;
            mPosition = getLayoutPosition();

            // Если старая и новая позиции совпадают, то удаляем позицию
            if (mPosition == oldPosition) {
                resetSelection();
            }

            // Обновляем айтем нажатый сейчас
            notifyItemChanged(mPosition);

            // Код для проброса слушателя
            if (GroupAdapter.mListener != null) {
                GroupAdapter.mListener.onItemClick(v, getLayoutPosition(), mGroup, mPosition);
            }
        }

        public void bindGroup(Group group) {
            mGroup = group;

            // Устанавливаем значения полей
            mGroupName.setText(mGroup.getName());
            mGroupDescription.setText(mGroup.getDescription());

            // Устанавливаем цвет ярлычка
            if (mGroup.getColor() == null) {
                mGroupColor.setBackgroundResource(R.color.gray_2);
            } else if (mGroup.getColor().equals("red")) {
                mGroupColor.setBackgroundResource(R.color.red);
            } else if (mGroup.getColor().equals("orange")) {
                mGroupColor.setBackgroundResource(R.color.orange);
            } else if (mGroup.getColor().equals("yellow")) {
                mGroupColor.setBackgroundResource(R.color.yellow);
            } else if (mGroup.getColor().equals("green")) {
                mGroupColor.setBackgroundResource(R.color.green);
            } else if (mGroup.getColor().equals("blue")) {
                mGroupColor.setBackgroundResource(R.color.blue);
            } else if (mGroup.getColor().equals("violet")) {
                mGroupColor.setBackgroundResource(R.color.violet);
            } else if (mGroup.getColor().equals("pink")) {
                mGroupColor.setBackgroundResource(R.color.pink);
            }
        }
    }

    public GroupAdapter(Context context, List<Group> groups) {
        mGroups = groups;
        mContext = context;
    }

    @Override
    public GroupAdapter.GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new GroupAdapter.GroupHolder(v);
    }

    @Override
    public void onBindViewHolder(final GroupAdapter.GroupHolder holder, int position) {
        Group group = mGroups.get(position);
        holder.bindGroup(group);

        // Если установленная позиция равна текущей, то выделяем элемент
        holder.itemView.setSelected(mPosition == position);
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    public void setGroups(List<Group> groups) {
        mGroups = groups;
    }

    // Интерфейс для проброса слушателя наружу
    public interface OnItemClickListener {
        void onItemClick(View itemView, int itemPosition, Group group, int selectedPosition);
    }

    public void setOnItemClickListener(GroupAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public void addItem(int position, Group group) {
        mGroups.add(position, group);
        super.notifyItemInserted(position);
    }

    public void deleteItem(int position) {
        mGroups.remove(position);
        super.notifyItemRemoved(position);
        resetSelection();
    }

    public void resetSelection() {
        mPosition = RecyclerView.NO_POSITION;   //NO_POSITION == -1
    }
}

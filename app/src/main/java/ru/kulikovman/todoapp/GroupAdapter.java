package ru.kulikovman.todoapp;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

        private LinearLayout mItemLayout;

        public GroupHolder(View view) {
            super(view);

            view.setClickable(true);

            // Слушатель нажатий по элменту
            view.setOnClickListener(this);

            // Инициализируем вью элемента списка
            mGroupName = (TextView) view.findViewById(R.id.item_group_name);
            mGroupDescription = (TextView) view.findViewById(R.id.item_group_description);
            mGroupColor = (ImageButton) view.findViewById(R.id.item_group_color);
            mItemLayout = (LinearLayout) view.findViewById(R.id.item_group_layout);
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

            // Устанавливаем значения полей и цвет ярлычка
            mGroupName.setText(mGroup.getName());
            mGroupDescription.setText(mGroup.getDescription());
            mGroupColor.setBackgroundResource(mGroup.getColorId());
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

        // Если установленная позиция равна текущей, то делаем элемент "нажатым"
        holder.itemView.setPressed(mPosition == position);

        // Запасной вариант - выделение выбранным цветом, но без риппл эффекта
        //holder.itemView.setBackgroundColor(mPosition == position ? Color.GREEN : Color.TRANSPARENT);

        // Есть еще вариант с setSelected(), который является более подходящим
        // Только нужно разабраться почему веделение визуально не выделяется
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
    }

    public void resetSelection() {
        mPosition = RecyclerView.NO_POSITION;   //NO_POSITION == -1
    }
}
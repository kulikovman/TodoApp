package ru.kulikovman.todoapp.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import ru.kulikovman.todoapp.R;
import ru.kulikovman.todoapp.models.Group;

public class GroupRealmAdapter extends RealmRecyclerViewAdapter<Group, GroupRealmAdapter.GroupHolder> {
    private static OnItemClickListener mListener;
    private Context mContext;
    private OrderedRealmCollection<Group> mGroups;

    private int mPosition = RecyclerView.NO_POSITION;

    public GroupRealmAdapter(Context context, OrderedRealmCollection<Group> results) {
        super(results, true);
        // Only set this if the model class has a primary key that is also a integer or long.
        // In that case, {@code getItemId(int)} must also be overridden to return the key.
        setHasStableIds(true);

        mGroups = results;
        mContext = context;
    }

    @Override
    public long getItemId(int index) {
        //noinspection ConstantConditions
        return getItem(index).getId();
    }

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
            if (GroupRealmAdapter.mListener != null) {
                GroupRealmAdapter.mListener.onItemClick(v, getLayoutPosition(), mGroup, mPosition);
            }
        }

        public void bindGroup(Group group) {
            mGroup = group;

            // Устанавливаем значения полей
            mGroupName.setText(group.getName());
            mGroupDescription.setText(group.getDescription());

            // Получаем название цвета и закрашиваем ярлычок
            String color = group.getColor();

            if (color != null) {
                // Получаем id цвета из его названия
                int colorId = mContext.getResources().getIdentifier(color, "color", mContext.getPackageName());
                mGroupColor.setBackgroundResource(colorId);
            }
        }
    }

    @Override
    public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new GroupHolder(item);
    }

    @Override
    public void onBindViewHolder(final GroupRealmAdapter.GroupHolder holder, int position) {
        Group group = mGroups.get(position);
        holder.bindGroup(group);

        // Если установленная позиция равна текущей, то выделяем элемент
        holder.itemView.setSelected(mPosition == position);
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    public void setGroups(OrderedRealmCollection<Group> results) {
        mGroups = results;
    }

    // Интерфейс для проброса слушателя наружу
    public interface OnItemClickListener {
        void onItemClick(View itemView, int itemPosition, Group group, int selectedPosition);
    }

    public void setOnItemClickListener(GroupRealmAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public void resetSelection() {
        mPosition = RecyclerView.NO_POSITION;   //NO_POSITION == -1
    }
}

package ru.kulikovman.todoapp;


import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        // Вычисление пикселей по DP. Здесь отступ будет *8dp*
        int margin = 50;
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin, view.getResources().getDisplayMetrics());

        if(parent.getChildAdapterPosition(view) == 0){
            outRect.top = space;
            outRect.bottom = 0;
        }
    }
}

package com.readboy.onlinecourseaides.adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author jll
 * @Date 2022/12/26
 */
public class SpacesAppItemDecoration extends RecyclerView.ItemDecoration{
    private int space;

    public SpacesAppItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
//        if (parent.getChildPosition(view) == 0){
//            outRect.left = 0;
//        }

    }
}

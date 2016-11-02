package com.insurancetelematics.team.projectl.android.core.cards;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildLayoutPosition(view);
        boolean first = pos == 0;
        boolean last = pos == parent.getAdapter().getItemCount() - 1;

        outRect.left = space;
        outRect.top = first ? space : (space / 2);
        outRect.right = space;
        outRect.bottom = last ? space : (space / 2);
    }
}
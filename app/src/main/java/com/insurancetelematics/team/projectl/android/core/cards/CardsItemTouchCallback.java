package com.insurancetelematics.team.projectl.android.core.cards;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class CardsItemTouchCallback extends ItemTouchHelper.SimpleCallback {
    private static final int NOT_MOVE_FLAG = 0;

    private final OnSwipeItemListener onSwipeItemListener;

    public CardsItemTouchCallback(OnSwipeItemListener onSwipeItemListener) {
        super(NOT_MOVE_FLAG, ItemTouchHelper.RIGHT);
        this.onSwipeItemListener = onSwipeItemListener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
            RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (onSwipeItemListener == null) return;

        int swipedPosition = viewHolder.getLayoutPosition();
        onSwipeItemListener.onSwipe(swipedPosition);
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        Card card = (Card) viewHolder.itemView.getTag();
        if (!card.isSwipeable()) return 0;

        return super.getSwipeDirs(recyclerView, viewHolder);
    }
}
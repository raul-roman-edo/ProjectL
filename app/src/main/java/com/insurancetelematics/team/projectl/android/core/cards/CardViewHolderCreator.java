package com.insurancetelematics.team.projectl.android.core.cards;

import android.view.ViewGroup;

public interface CardViewHolderCreator<T extends CardViewHolder> {
    T create(ViewGroup parent, int viewType, OnCardRemoved cardRemovedCallback);
}

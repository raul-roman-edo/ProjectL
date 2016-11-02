package com.insurancetelematics.team.projectl.android.core.cards;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class CardViewHolder<T> extends RecyclerView.ViewHolder {

    protected OnCardRemoved onCardRemoved = null;
    protected T defaultValue = null;

    public CardViewHolder(ViewGroup parent, int layoutId, OnCardRemoved onCardRemoved) {
        this(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false), onCardRemoved);
    }

    public CardViewHolder(View v, OnCardRemoved onCardRemoved) {
        super(v);
        this.onCardRemoved = onCardRemoved;
    }

    public abstract void fillElement(T element);

    public void recycle() {

    }

    public void setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
    }
}

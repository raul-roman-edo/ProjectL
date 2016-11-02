package com.insurancetelematics.team.projectl.android.core.cards;

public abstract class Card {
    public boolean isSwipeable() {
        return false;
    }

    public abstract int getType();
}

package com.insurancetelematics.team.projectl.comments;

import com.insurancetelematics.team.projectl.android.core.cards.Card;
import java.util.List;

public interface CommentsView {
    void updateEntries(List<Card> newCards);

    void showProgress();

    void hideProgress();

    void showMessage(String message);
}

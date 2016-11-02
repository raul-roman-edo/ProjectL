package com.insurancetelematics.team.projectl.images.gallery;

import com.insurancetelematics.team.projectl.android.core.cards.Card;
import java.util.List;

public interface GalleryView {
    void updateEntries(List<Card> newCards);

    void showProgress();

    void hideProgress();

    void showMessage(String message);
}

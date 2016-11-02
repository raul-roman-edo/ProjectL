package com.insurancetelematics.team.projectl.images.gallery.cards.photos;

import com.google.gson.annotations.SerializedName;
import com.insurancetelematics.team.projectl.android.core.cards.Card;
import java.util.Objects;

public class Photo extends Card {
    public static final String TAG = Photo.class.getSimpleName();
    private static final String EMPTY = "";
    @SerializedName("message")
    private String description = EMPTY;
    @SerializedName("url")
    private String imageUrl = EMPTY;

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public int getType() {
        return TAG.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return Objects.equals(imageUrl, photo.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUrl);
    }
}

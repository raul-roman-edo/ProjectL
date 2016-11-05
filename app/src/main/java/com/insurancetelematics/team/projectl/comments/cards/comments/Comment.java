package com.insurancetelematics.team.projectl.comments.cards.comments;

import com.google.gson.annotations.SerializedName;
import com.insurancetelematics.team.projectl.android.core.cards.Card;
import java.util.Objects;

public class Comment extends Card {
    public static final String TAG = Comment.class.getSimpleName();
    private static final String EMPTY = "";
    @SerializedName("name")
    private String name = EMPTY;
    @SerializedName("message")
    private String message = EMPTY;
    @SerializedName("avatarURL")
    private String avatarURL = EMPTY;

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    @Override
    public int getType() {
        return TAG.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(name, comment.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

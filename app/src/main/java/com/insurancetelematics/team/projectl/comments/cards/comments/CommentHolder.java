package com.insurancetelematics.team.projectl.comments.cards.comments;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.insurancetelematics.team.projectl.R;
import com.insurancetelematics.team.projectl.android.core.cards.CardViewHolder;
import com.squareup.picasso.Picasso;

public class CommentHolder extends CardViewHolder<Comment> {
    private ImageView avatar;
    private TextView name;
    private TextView message;

    public CommentHolder(ViewGroup parent) {
        super(parent, R.layout.comment_item, null);
        loadViews(itemView);
    }

    @Override
    public void fillElement(Comment comment) {
        itemView.setTag(comment);
        showComment(comment);
    }

    private void showComment(Comment comment) {
        name.setText(comment.getName());
        message.setText(comment.getMessage());
        showAvatar(comment);
    }

    private void loadViews(View itemView) {
        avatar = (ImageView) itemView.findViewById(R.id.avatar);
        name = (TextView) itemView.findViewById(R.id.name);
        message = (TextView) itemView.findViewById(R.id.message);
    }

    private void showAvatar(Comment comment) {
        boolean hasNoAvatar = comment.getAvatarURL().isEmpty();
        if (hasNoAvatar) {
            avatar.setImageResource(R.drawable.default_photo);
        } else {
            Picasso.with(itemView.getContext())
                    .load(comment.getAvatarURL())
                    .resizeDimen(R.dimen.comment_avatar_size, R.dimen.comment_avatar_size)
                    .centerCrop()
                    .placeholder(R.drawable.default_photo)
                    .error(R.drawable.default_photo)
                    .into(avatar);
        }
    }
}

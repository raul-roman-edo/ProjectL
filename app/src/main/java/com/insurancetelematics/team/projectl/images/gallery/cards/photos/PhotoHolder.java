package com.insurancetelematics.team.projectl.images.gallery.cards.photos;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.insurancetelematics.team.projectl.R;
import com.insurancetelematics.team.projectl.android.core.cards.CardViewHolder;
import com.squareup.picasso.Picasso;

public class PhotoHolder extends CardViewHolder<Photo> implements PhotoView {
    private PhotoPresenter presenter;
    private ImageView image;

    public PhotoHolder(ViewGroup parent) {
        super(parent, R.layout.item, null);
        loadViews(itemView);
        createPresenter();
    }

    @Override
    public void fillElement(Photo photo) {
        itemView.setTag(photo);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onPhotoPressed((Photo) itemView.getTag());
            }
        });
        presenter.fillWith(photo);
    }

    @Override
    public void showImage(String url) {
        Picasso.with(itemView.getContext())
                .load(url)
                .resizeDimen(R.dimen.grid_item_size, R.dimen.grid_item_size)
                .centerCrop()
                .placeholder(R.drawable.default_photo)
                .error(R.drawable.default_photo)
                .into(image);
    }

    private void loadViews(View itemView) {
        image = (ImageView) itemView.findViewById(R.id.image);
    }

    private void createPresenter() {
        presenter = new PhotoPresenter(this);
    }
}

package com.insurancetelematics.team.projectl.images.gallery.cards.photos;

public class PhotoPresenter {
    private final PhotoView view;

    public PhotoPresenter(PhotoView view) {
        this.view = view;
    }

    public void fillWith(Photo photo) {
        view.showImage(photo.getImageUrl());
    }

    public void onPhotoPressed(Photo photo) {
        // TODO: 2/11/16 launch details activity with animations
    }
}

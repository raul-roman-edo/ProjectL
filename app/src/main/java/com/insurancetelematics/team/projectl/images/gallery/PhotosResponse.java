package com.insurancetelematics.team.projectl.images.gallery;

import com.insurancetelematics.team.projectl.core.BaseApiResponse;
import com.insurancetelematics.team.projectl.images.gallery.cards.photos.Photo;
import java.util.ArrayList;
import java.util.List;

public class PhotosResponse extends BaseApiResponse {
    private List<Photo> photos = new ArrayList<>();

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}

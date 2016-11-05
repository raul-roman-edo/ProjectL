package com.insurancetelematics.team.projectl.images.gallery;

import com.insurancetelematics.team.projectl.core.AsyncAction;
import com.insurancetelematics.team.projectl.core.BaseApiResponse;
import com.insurancetelematics.team.projectl.core.ThreadExecutor;
import com.insurancetelematics.team.projectl.images.gallery.cards.photos.Photo;
import java.util.List;

public class PhotosLoadingUseCase extends AsyncAction<Void, BaseApiResponse<List<Photo>>> {
    private final GetRemotePhotosCommand loader;

    public PhotosLoadingUseCase(ThreadExecutor executor, GetRemotePhotosCommand loader) {
        super(executor);
        this.loader = loader;
    }

    @Override
    protected BaseApiResponse<List<Photo>> parallelExecution(Void ignore) {
        BaseApiResponse<List<Photo>> response = loader.request();
        return response;
    }
}

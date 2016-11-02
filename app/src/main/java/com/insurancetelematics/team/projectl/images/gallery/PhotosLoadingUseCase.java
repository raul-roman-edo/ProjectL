package com.insurancetelematics.team.projectl.images.gallery;

import com.insurancetelematics.team.projectl.core.AsyncAction;
import com.insurancetelematics.team.projectl.core.ThreadExecutor;

public class PhotosLoadingUseCase extends AsyncAction<Void, PhotosResponse> {
    private final GetRemotePhotosCommand loader;

    public PhotosLoadingUseCase(ThreadExecutor executor, GetRemotePhotosCommand loader) {
        super(executor);
        this.loader = loader;
    }

    @Override
    protected PhotosResponse parallelExecution(Void ignore) {
        PhotosResponse response = loader.request();
        return response;
    }
}

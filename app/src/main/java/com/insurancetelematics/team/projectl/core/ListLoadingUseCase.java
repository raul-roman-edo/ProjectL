package com.insurancetelematics.team.projectl.core;

import java.lang.reflect.Type;
import java.util.List;

public class ListLoadingUseCase<Item> extends AsyncAction<Void, BaseApiResponse<List<Item>>> {
    private final GetRemoteListCommand<Item> loader;

    public ListLoadingUseCase(ThreadExecutor executor, NetworkUtils network, String url, Type listType) {
        super(executor);
        this.loader = new GetRemoteListCommand<>(network, url, listType);
    }

    @Override
    protected BaseApiResponse<List<Item>> parallelExecution(Void ignore) {
        BaseApiResponse<List<Item>> response = loader.request();
        return response;
    }
}

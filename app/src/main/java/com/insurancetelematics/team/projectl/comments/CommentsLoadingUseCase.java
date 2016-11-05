package com.insurancetelematics.team.projectl.comments;

import com.insurancetelematics.team.projectl.comments.cards.comments.Comment;
import com.insurancetelematics.team.projectl.core.AsyncAction;
import com.insurancetelematics.team.projectl.core.BaseApiResponse;
import com.insurancetelematics.team.projectl.core.ThreadExecutor;
import java.util.List;

public class CommentsLoadingUseCase extends AsyncAction<Void, BaseApiResponse<List<Comment>>> {
    private final GetRemoteCommentsCommand loader;

    public CommentsLoadingUseCase(ThreadExecutor executor, GetRemoteCommentsCommand loader) {
        super(executor);
        this.loader = loader;
    }

    @Override
    protected BaseApiResponse<List<Comment>> parallelExecution(Void ignore) {
        BaseApiResponse<List<Comment>> response = loader.request();
        return response;
    }
}

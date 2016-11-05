package com.insurancetelematics.team.projectl.comments;

import com.insurancetelematics.team.projectl.android.core.cards.Card;
import com.insurancetelematics.team.projectl.android.core.cards.CardsPresenter;
import com.insurancetelematics.team.projectl.comments.cards.comments.Comment;
import com.insurancetelematics.team.projectl.core.AsyncAction;
import com.insurancetelematics.team.projectl.core.AsyncActionListener;
import com.insurancetelematics.team.projectl.core.BaseApiResponse;
import com.insurancetelematics.team.projectl.core.ListLoadingUseCase;
import com.insurancetelematics.team.projectl.core.NetworkCallbacksDispatcher;
import java.util.ArrayList;
import java.util.List;

public class CommentsPresenter implements CardsPresenter {
    private final CommentsView view;
    private final AsyncAction<Void, BaseApiResponse<List<Comment>>> loadingUseCase;
    private AsyncActionListener<BaseApiResponse<List<Comment>>> successListener =
            new AsyncActionListener<BaseApiResponse<List<Comment>>>() {
                @Override
                public void onFinished(BaseApiResponse<List<Comment>> result) {
                    view.hideProgress();
                    List<Card> cards = new ArrayList<>();
                    cards.addAll(result.getPayload());
                    view.updateEntries(cards);
                }
            };

    private AsyncActionListener<BaseApiResponse<List<Comment>>> errorListener =
            new AsyncActionListener<BaseApiResponse<List<Comment>>>() {
                @Override
                public void onFinished(BaseApiResponse<List<Comment>> result) {
                    view.hideProgress();
                    view.showMessage("Ha ocurrido un error");
                }
            };

    private AsyncActionListener<BaseApiResponse<List<Comment>>> connectivityListener =
            new AsyncActionListener<BaseApiResponse<List<Comment>>>() {
                @Override
                public void onFinished(BaseApiResponse<List<Comment>> result) {
                    view.hideProgress();
                    view.showMessage("Ha sido imposible conectar");
                }
            };

    public CommentsPresenter(CommentsView view, ListLoadingUseCase loadingUseCase) {
        this.view = view;
        this.loadingUseCase = loadingUseCase;
    }

    @Override
    public void loadCards() {
        view.showProgress();
        NetworkCallbacksDispatcher<BaseApiResponse<List<Comment>>> dispatcher =
                new NetworkCallbacksDispatcher<>(successListener, errorListener, connectivityListener);
        loadingUseCase.run(dispatcher);
    }
}

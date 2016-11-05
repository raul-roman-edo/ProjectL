package com.insurancetelematics.team.projectl.images.gallery;

import com.insurancetelematics.team.projectl.android.core.cards.Card;
import com.insurancetelematics.team.projectl.android.core.cards.CardsPresenter;
import com.insurancetelematics.team.projectl.core.AsyncAction;
import com.insurancetelematics.team.projectl.core.AsyncActionListener;
import com.insurancetelematics.team.projectl.core.BaseApiResponse;
import com.insurancetelematics.team.projectl.core.ListLoadingUseCase;
import com.insurancetelematics.team.projectl.core.NetworkCallbacksDispatcher;
import com.insurancetelematics.team.projectl.images.gallery.cards.photos.Photo;
import java.util.ArrayList;
import java.util.List;

public class GalleryPresenter implements CardsPresenter {
    private final GalleryView view;
    private final AsyncAction<Void, BaseApiResponse<List<Photo>>> loadingUseCase;
    private AsyncActionListener<BaseApiResponse<List<Photo>>> successListener =
            new AsyncActionListener<BaseApiResponse<List<Photo>>>() {
                @Override
                public void onFinished(BaseApiResponse<List<Photo>> result) {
                    view.hideProgress();
                    List<Card> cards = new ArrayList<>();
                    cards.addAll(result.getPayload());
                    view.updateEntries(cards);
                }
            };

    private AsyncActionListener<BaseApiResponse<List<Photo>>> errorListener =
            new AsyncActionListener<BaseApiResponse<List<Photo>>>() {
                @Override
                public void onFinished(BaseApiResponse<List<Photo>> result) {
                    view.hideProgress();
                    view.showMessage("Ha ocurrido un error");
                }
            };

    private AsyncActionListener<BaseApiResponse<List<Photo>>> connectivityListener =
            new AsyncActionListener<BaseApiResponse<List<Photo>>>() {
                @Override
                public void onFinished(BaseApiResponse<List<Photo>> result) {
                    view.hideProgress();
                    view.showMessage("Ha sido imposible conectar");
                }
            };

    public GalleryPresenter(GalleryView view, ListLoadingUseCase<Photo> loadingUseCase) {
        this.view = view;
        this.loadingUseCase = loadingUseCase;
    }

    @Override
    public void loadCards() {
        view.showProgress();
        NetworkCallbacksDispatcher<BaseApiResponse<List<Photo>>> dispatcher =
                new NetworkCallbacksDispatcher<>(successListener, errorListener, connectivityListener);
        loadingUseCase.run(dispatcher);
    }

    public void onPlayPressed() {
        // TODO: 2/11/16 launch animated presentation with music
    }
}

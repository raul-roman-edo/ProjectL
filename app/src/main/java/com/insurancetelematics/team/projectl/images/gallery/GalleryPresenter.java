package com.insurancetelematics.team.projectl.images.gallery;

import com.insurancetelematics.team.projectl.android.core.cards.Card;
import com.insurancetelematics.team.projectl.android.core.cards.CardsPresenter;
import com.insurancetelematics.team.projectl.core.AsyncAction;
import com.insurancetelematics.team.projectl.core.AsyncActionListener;
import com.insurancetelematics.team.projectl.core.NetworkCallbacksDispatcher;
import java.util.ArrayList;
import java.util.List;

public class GalleryPresenter implements CardsPresenter {
    private final GalleryView view;
    private final AsyncAction<Void, PhotosResponse> loadingUseCase;
    private AsyncActionListener<PhotosResponse> successListener = new AsyncActionListener<PhotosResponse>() {
        @Override
        public void onFinished(PhotosResponse result) {
            view.hideProgress();
            List<Card> cards = new ArrayList<>();
            cards.addAll(result.getPhotos());
            view.updateEntries(cards);
        }
    };

    private AsyncActionListener<PhotosResponse> errorListener = new AsyncActionListener<PhotosResponse>() {
        @Override
        public void onFinished(PhotosResponse result) {
            view.hideProgress();
            view.showMessage("Ha ocurrido un error");
        }
    };

    private AsyncActionListener<PhotosResponse> connectivityListener = new AsyncActionListener<PhotosResponse>() {
        @Override
        public void onFinished(PhotosResponse result) {
            view.hideProgress();
            view.showMessage("Ha sido imposible conectar");
        }
    };

    public GalleryPresenter(GalleryView view, PhotosLoadingUseCase loadingUseCase) {
        this.view = view;
        this.loadingUseCase = loadingUseCase;
    }

    @Override
    public void loadCards() {
        view.showProgress();
        NetworkCallbacksDispatcher<PhotosResponse> dispatcher =
                new NetworkCallbacksDispatcher<>(successListener, errorListener, connectivityListener);
        loadingUseCase.run(dispatcher);
    }

    public void onPlayPressed() {
        // TODO: 2/11/16 launch animated presentation with music
    }
}

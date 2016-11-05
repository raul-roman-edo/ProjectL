package com.insurancetelematics.team.projectl.images.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.insurancetelematics.team.projectl.R;
import com.insurancetelematics.team.projectl.android.core.cards.CardViewHolder;
import com.insurancetelematics.team.projectl.android.core.cards.CardViewHolderCreator;
import com.insurancetelematics.team.projectl.android.core.cards.CardsFragment;
import com.insurancetelematics.team.projectl.android.core.cards.OnCardRemoved;
import com.insurancetelematics.team.projectl.android.core.cards.RecyclerViewCardsAdapter;
import com.insurancetelematics.team.projectl.core.NetworkUtils;
import com.insurancetelematics.team.projectl.core.ThreadExecutor;
import com.insurancetelematics.team.projectl.images.gallery.cards.photos.Photo;
import com.insurancetelematics.team.projectl.images.gallery.cards.photos.PhotoHolder;
import java.util.HashMap;
import java.util.Map;

public class GalleryFragment extends CardsFragment<GalleryPresenter> implements GalleryView {

    private Map<Integer, CardViewHolderCreator> holdersMap;
    private CoordinatorLayout coordinator;
    private Toolbar toolbar;
    private FloatingActionButton play;
    private View progress;

    public static Fragment newInstance() {
        return new GalleryFragment();
    }

    public GalleryFragment() {
        holdersMap = new HashMap<>();
        holdersMap.put(Photo.TAG.hashCode(), new CardViewHolderCreator() {
            @Override
            public CardViewHolder create(ViewGroup parent, int viewType, OnCardRemoved cardRemovedCallback) {
                return new PhotoHolder(parent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        loadViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(coordinator, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected GalleryPresenter createPresenter() {
        GetRemotePhotosCommand loader = new GetRemotePhotosCommand(NetworkUtils.getInstance());
        PhotosLoadingUseCase loadingUseCase = new PhotosLoadingUseCase(ThreadExecutor.getInstance(), loader);
        GalleryPresenter presenter = new GalleryPresenter(this, loadingUseCase);

        return presenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_gallery;
    }

    @Override
    protected int obtainRecyclerViewId() {
        return R.id.recycler;
    }

    @Override
    protected RecyclerViewCardsAdapter createCardsAdapter(RecyclerView recyclerView) {
        RecyclerViewCardsAdapter adapter = new RecyclerViewCardsAdapter(recyclerView);
        adapter.setHoldersMap(holdersMap);

        return adapter;
    }

    @Override
    protected RecyclerView.LayoutManager obtainLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected RecyclerView.ItemDecoration obtainItemDecoration() {
        return null;
    }

    private void loadViews(View view) {
        coordinator = (CoordinatorLayout) view.findViewById(R.id.main_coordinator_layout);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        progress = view.findViewById(R.id.progressContainer);
        play = (FloatingActionButton) view.findViewById(R.id.fab);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onPlayPressed();
            }
        });
    }
}

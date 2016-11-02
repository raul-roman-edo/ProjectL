package com.insurancetelematics.team.projectl.android.core.cards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public abstract class CardsFragment<T extends CardsPresenter> extends Fragment {
    private static final int HALF_AN_HOUR_MILLIS = 30 * 60 * 1000;
    protected RecyclerView recyclerView;
    protected T presenter;
    private View mainView = null;
    private long sessionMillis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(getLayout(), container, false);
        }
        if (recyclerView == null) {
            recyclerView = (RecyclerView) mainView.findViewById(obtainRecyclerViewId());
        }

        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPresenter();
        boolean isNewInstance = recyclerView.getAdapter() == null;
        if (isNewInstance) {
            initRecyclerView();
        } else {
            restoreRecyclerView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        launchEntriesLoading();
    }

    public void updateEntries(List<Card> newCards) {
        RecyclerViewCardsAdapter adapter = (RecyclerViewCardsAdapter) recyclerView.getAdapter();
        boolean isNotEmpty = adapter.getItemCount() > 0;
        boolean haveSameElements = adapter.getItemCount() == newCards.size();
        adapter.update(newCards);
        if (isNotEmpty && haveSameElements) {
            adapter.notifyDataSetChanged();
        }
    }

    protected abstract T createPresenter();

    protected abstract int getLayout();

    protected abstract int obtainRecyclerViewId();

    protected abstract RecyclerViewCardsAdapter createCardsAdapter(RecyclerView recyclerView);

    protected abstract RecyclerView.LayoutManager obtainLayoutManager();

    protected abstract RecyclerView.ItemDecoration obtainItemDecoration();

    private void initPresenter() {
        presenter = createPresenter();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(obtainLayoutManager());
        RecyclerView.ItemDecoration decorator = obtainItemDecoration();
        if (decorator != null) {
            recyclerView.addItemDecoration(decorator);
        }
        recyclerView.setAdapter(createCardsAdapter(recyclerView));
        initSession();
    }

    private void restoreRecyclerView() {
        boolean expiredSession = isSessionExpired();
        if (expiredSession) {
            initSession();
        }
    }

    private void refreshAdapter() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void initSession() {
        sessionMillis = System.currentTimeMillis();
        RecyclerViewCardsAdapter adapter = (RecyclerViewCardsAdapter) recyclerView.getAdapter();
        adapter.clearManuallyRemoved();
    }

    private boolean isSessionExpired() {
        long currentTimeMillis = System.currentTimeMillis();
        long thresholdTimeMillis = sessionMillis + HALF_AN_HOUR_MILLIS;
        boolean isSessionExpired = currentTimeMillis > thresholdTimeMillis;

        return isSessionExpired;
    }

    private void launchEntriesLoading() {
        presenter.loadCards();
    }
}

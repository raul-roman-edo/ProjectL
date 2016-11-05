package com.insurancetelematics.team.projectl.comments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.insurancetelematics.team.projectl.comments.cards.comments.Comment;
import com.insurancetelematics.team.projectl.comments.cards.comments.CommentHolder;
import com.insurancetelematics.team.projectl.core.NetworkUtils;
import com.insurancetelematics.team.projectl.core.ThreadExecutor;
import java.util.HashMap;
import java.util.Map;

public class CommentsFragment extends CardsFragment<CommentsPresenter> implements CommentsView {

    private Map<Integer, CardViewHolderCreator> holdersMap;
    private CoordinatorLayout coordinator;
    private Toolbar toolbar;
    private View progress;

    public static Fragment newInstance() {
        return new CommentsFragment();
    }

    public CommentsFragment() {
        holdersMap = new HashMap<>();
        holdersMap.put(Comment.TAG.hashCode(), new CardViewHolderCreator() {
            @Override
            public CardViewHolder create(ViewGroup parent, int viewType, OnCardRemoved cardRemovedCallback) {
                return new CommentHolder(parent);
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
    protected CommentsPresenter createPresenter() {
        GetRemoteCommentsCommand loader = new GetRemoteCommentsCommand(NetworkUtils.getInstance());
        CommentsLoadingUseCase loadingUseCase = new CommentsLoadingUseCase(ThreadExecutor.getInstance(), loader);
        CommentsPresenter presenter = new CommentsPresenter(this, loadingUseCase);

        return presenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_comments;
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
        return new LinearLayoutManager(getActivity());
    }

    @Override
    protected RecyclerView.ItemDecoration obtainItemDecoration() {
        return null;
    }

    private void loadViews(View view) {
        coordinator = (CoordinatorLayout) view.findViewById(R.id.main_coordinator_layout);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        progress = view.findViewById(R.id.progressContainer);
    }
}

package com.insurancetelematics.team.projectl.android.core.cards;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecyclerViewCardsAdapter extends RecyclerView.Adapter<CardViewHolder> {

    public static final int PRELOAD_THRESHOLD = 7;
    private final List<Card> cards;
    private final List<Card> manuallyRemoved;
    private OnRecyclerEventListener onRecyclerEventListener = null;
    private boolean processingBottom = false;
    private Map<Integer, CardViewHolderCreator> holdersMap;
    private OnCardRemoved onCardRemoved = new OnCardRemoved() {
        @Override
        public void removeCard(Card toRemove) {
            remove(toRemove);
        }
    };

    public RecyclerViewCardsAdapter(final RecyclerView recyclerView) {
        this.cards = new ArrayList<>();
        this.manuallyRemoved = new ArrayList<>();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (processingBottom || onRecyclerEventListener == null) {
                    return;
                }

                if (!(recyclerView.getLayoutManager() instanceof LinearLayoutManager)) {
                    return;
                }
                int last = getItemCount() - 1;
                if (last < 0) {
                    return;
                }
                int lastVisible = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                if (last - lastVisible < PRELOAD_THRESHOLD) {
                    processingBottom = true;
                    onRecyclerEventListener.onNearBottom();
                }
            }
        });
    }

    public void setBottomProcessed() {
        processingBottom = false;
    }

    public void setHoldersMap(Map<Integer, CardViewHolderCreator> holdersMap) {
        this.holdersMap = holdersMap;
    }

    public void setOnRecyclerEventListener(OnRecyclerEventListener onRecyclerEventListener) {
        this.onRecyclerEventListener = onRecyclerEventListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (checkNotAbleToLoad()) {
            return 0;
        }

        Card card = cards.get(position);
        return card.getType();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (checkNotAbleToLoad()) {
            return null;
        }

        CardViewHolderCreator creator = holdersMap.get(viewType);
        if (creator == null) {
            return null;
        } else {
            return creator.create(parent, viewType, onCardRemoved);
        }
    }

    @Override
    public void onBindViewHolder(CardViewHolder rawHolder, int position) {
        rawHolder.fillElement(cards.get(position));
    }

    @Override
    public void onViewRecycled(CardViewHolder rawHolder) {
        rawHolder.recycle();
    }

    @Override
    public int getItemCount() {
        return cards == null ? 0 : cards.size();
    }

    public List<Card> getItems() {
        return cards;
    }

    public Card getItemByPosition(int position) {
        Card card = cards.get(position);

        return card;
    }

    public void addToBottom(Card card) {
        if (cards == null) {
            return;
        }
        int count = cards.size();
        cards.add(count, card);
        notifyItemRangeInserted(count, 1);
    }

    public void addToBottom(List<Card> entries) {
        if (this.cards == null || entries == null) {
            return;
        }
        int count = this.cards.size();
        this.cards.addAll(count, entries);
        notifyItemRangeInserted(count, entries.size());

        notifyItemChanged(this.cards.size());
    }

    public void update(List<Card> newEntries) {
        if (cards.isEmpty()) {
            addToBottom(newEntries);
        } else {
            addNew(newEntries);
            clean(newEntries);
        }
    }

    public void removeAll() {
        if (cards.size() > 0) {
            notifyItemRangeRemoved(0, cards.size());
            cards.clear();
        }
    }

    public void removeManually(Card card) {
        manuallyRemoved.add(card);
        remove(card);
    }

    public void remove(Card card) {
        if (cards.size() > 0) {
            int pos = cards.indexOf(card);
            cards.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    public void notifyItemChanged(Card card) {
        if (cards != null) {
            int position = cards.indexOf(card);
            notifyItemChanged(position);
        }
    }

    public void clearManuallyRemoved() {
        manuallyRemoved.clear();
    }

    private boolean checkNotAbleToLoad() {
        return cards == null || cards.isEmpty();
    }

    private void addNew(List<Card> newEntries) {
        int size = newEntries.size();
        Card newer;
        for (int i = 0; i < size; i++) {
            newer = newEntries.get(i);
            boolean mustNotAdd = cards.contains(newer) || manuallyRemoved.contains(newer);
            if (mustNotAdd) continue;

            int newPosition = obtainCurrentPosition(newEntries, i);
            addToPosition(newer, newPosition);
        }
    }

    private int obtainCurrentPosition(List<Card> newEntries, int i) {
        Card previous;
        int newPosition = -1;
        for (int j = i - 1; j >= 0; j--) {
            previous = newEntries.get(j);
            if (cards.contains(previous)) {
                newPosition = cards.lastIndexOf(previous);
                break;
            }
        }
        newPosition++;
        return newPosition;
    }

    private void addToPosition(Card newer, int newPosition) {
        cards.add(newPosition, newer);
        notifyItemInserted(newPosition);
    }

    private void clean(List<Card> newEntries) {
        List<Card> toRemove = new ArrayList<>();
        for (Card current : cards) {
            if (!newEntries.contains(current)) {
                toRemove.add(current);
            }
        }
        for (Card old : toRemove) {
            remove(old);
        }
    }

    public interface OnRecyclerEventListener {
        void onNearBottom();
    }
}
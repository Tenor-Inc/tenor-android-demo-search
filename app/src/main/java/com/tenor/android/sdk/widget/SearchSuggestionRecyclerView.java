package com.tenor.android.sdk.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.CallSuper;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.tenor.android.core.response.impl.SearchSuggestionResponse;
import com.tenor.android.core.util.AbstractListUtils;
import com.tenor.android.core.util.AbstractUIUtils;
import com.tenor.android.core.util.AbstractWeakReferenceUtils;
import com.tenor.android.core.weakref.IWeakRefObject;
import com.tenor.android.sdk.adapter.SearchSuggestionAdapter;
import com.tenor.android.sdk.holder.SearchSuggestionVH;
import com.tenor.android.sdk.presenter.impl.SearchSuggestionPresenter;
import com.tenor.android.sdk.rvitem.SearchSuggestionRVItem;
import com.tenor.android.sdk.util.ColorPalette;
import com.tenor.android.sdk.view.ISearchSuggestionView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * {@link SearchSuggestionRecyclerView} is a subclass of {@link RecyclerView} for search suggestions
 * <p>
 * "AppCompatTextView" under "com.android.support:appcompat-v7" is not required, yet highly recommended
 * to ensure this widget is working properly on API 20-
 */
public class SearchSuggestionRecyclerView extends RecyclerView implements IWeakRefObject<Context>,
        ISearchSuggestionView, SearchSuggestionVH.OnClickListener {

    private final WeakReference<Context> mWeakRef;
    private final SearchSuggestionAdapter<SearchSuggestionRecyclerView> mAdapter;
    private final SearchSuggestionPresenter mPresenter;
    private final ItemDecoration mDefaultItemDecoration;
    private SearchSuggestionVH.OnClickListener mOnSearchSuggestionClickListener;
    private Call<SearchSuggestionResponse> mSearchSuggestionCall;

    public SearchSuggestionRecyclerView(Context context) {
        this(context, null);
    }

    public SearchSuggestionRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchSuggestionRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mWeakRef = new WeakReference<>(context);
        mPresenter = new SearchSuggestionPresenter(this);
        mAdapter = new SearchSuggestionAdapter<>(this);
        mDefaultItemDecoration = new SearchSuggestionItemDecoration(mWeakRef, 4);

        setAdapter(mAdapter);
        setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL));
        addItemDecoration(mDefaultItemDecoration);
    }

    public void removeDefaultItemDecoration() {
        removeItemDecoration(mDefaultItemDecoration);
    }

    @Nullable
    @Override
    public Context getRef() {
        return mWeakRef.get();
    }

    @NonNull
    @Override
    public WeakReference<Context> getWeakRef() {
        return mWeakRef;
    }

    @Override
    public boolean hasRef() {
        return AbstractWeakReferenceUtils.isAlive(mWeakRef);
    }

    public void getSearchSuggestions(@Nullable String query) {

        if (mSearchSuggestionCall != null && !mSearchSuggestionCall.isCanceled()) {
            mSearchSuggestionCall.cancel();
        }

        if (TextUtils.isEmpty(query)) {
            return;
        }
        mSearchSuggestionCall = mPresenter.getSearchSuggestions(query);
    }

    @Override
    @CallSuper
    public void onReceiveSearchSuggestionsSucceeded(@NonNull String query, @NonNull List<String> suggestions) {
        final boolean expandable = hasRef() && !AbstractListUtils.isEmpty(suggestions);
        setVisibility(expandable ? VISIBLE : GONE);
        if (!expandable) {
            return;
        }
        scrollToPosition(0);

        final List<SearchSuggestionRVItem> list = new ArrayList<>();
        for (String suggestion : suggestions) {
            list.add(new SearchSuggestionRVItem(SearchSuggestionAdapter.TYPE_DID_YOU_MEAN_SUGGESTION,
                    query, ColorPalette.getRandomColorResId(list.size()), suggestion));
        }
        mAdapter.insert(list);
    }

    @Override
    @CallSuper
    public void onReceiveSearchSuggestionsFailed(@NonNull String query, @Nullable Exception error) {
        setVisibility(GONE);
    }

    public void setOnSearchSuggestionClickListener(@Nullable final SearchSuggestionVH.OnClickListener listener) {
        mOnSearchSuggestionClickListener = listener;
        mAdapter.setOnSearchSuggestionClickListener(this);
    }

    @Override
    @CallSuper
    public void onClick(int position, @NonNull String query, @NonNull String suggestion) {
        if (mOnSearchSuggestionClickListener != null) {
            mOnSearchSuggestionClickListener.onClick(position, query, suggestion);
        }
    }

    private static class SearchSuggestionItemDecoration extends ItemDecoration {

        private final WeakReference<Context> mWeakRef;

        // note that the item shadow on each side is 4dp
        private final int mSpaceOnBothEnds;
        private final int mSpaceBetweenItems;

        /**
         * @param horizontalGap horizontal gap in dp
         */
        private SearchSuggestionItemDecoration(@NonNull WeakReference<Context> weakRef,
                                               @IntRange(from = 4, to = Integer.MAX_VALUE) int horizontalGap) {
            mWeakRef = weakRef;
            // e.g. expected gap is 16dp, then 16 - 4 = mSpaceOnBothEnds
            mSpaceOnBothEnds = AbstractUIUtils.dpToPx(weakRef.get(), getOrZero(horizontalGap - 4));
            // e.g. expected gap is 16dp, then 16 - 2 * 4 = mSpaceBetweenItems
            mSpaceBetweenItems = AbstractUIUtils.dpToPx(weakRef.get(), getOrZero(horizontalGap - 8));
        }

        private static int getOrZero(int value) {
            return value > 0 ? value : 0;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if (!AbstractWeakReferenceUtils.isAlive(mWeakRef)) {
                return;
            }

            outRect.left = mSpaceBetweenItems / 2;
            outRect.top = 0;
            outRect.right = mSpaceBetweenItems / 2;
            outRect.bottom = 0;

            final int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
            final int total = parent.getAdapter().getItemCount();
            if (position == 0) {
                outRect.left = mSpaceOnBothEnds;
            } else if (position == total - 1) {
                outRect.right = mSpaceOnBothEnds;
            }
        }
    }
}


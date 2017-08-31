package com.tenor.android.demo.search.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.model.impl.Result;
import com.tenor.android.core.response.BaseError;
import com.tenor.android.core.response.impl.GifsResponse;
import com.tenor.android.core.util.AbstractLayoutManagerUtils;
import com.tenor.android.core.util.AbstractListUtils;
import com.tenor.android.core.util.AbstractUIUtils;
import com.tenor.android.core.weakref.WeakRefOnScrollListener;
import com.tenor.android.core.widget.adapter.AbstractRVItem;
import com.tenor.android.demo.search.R;
import com.tenor.android.demo.search.adapter.GifSearchAdapter;
import com.tenor.android.demo.search.adapter.decorations.GifSearchItemDecoration;
import com.tenor.android.demo.search.adapter.rvitem.GifRVItem;
import com.tenor.android.demo.search.adapter.view.IGifSearchView;
import com.tenor.android.demo.search.presenter.IGifSearchPresenter;
import com.tenor.android.demo.search.presenter.impl.GifSearchPresenter;
import com.tenor.android.demo.search.widget.TenorStaggeredGridLayoutManager;
import com.tenor.android.sdk.holder.SearchSuggestionVH;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchActivity will display GIFs in a "waterfall" vertical layout,
 * where the aspect ratio of the GIFs will be maintained.
 *
 * Related search terms will be displayed in a horizontal top bar.
 * Clicking one will open a new instance of SearchActivity.
 */
public class SearchActivity extends AppCompatActivity implements IGifSearchView {
    // Number of columns for the RecyclerView
    private static final int STAGGERED_GRID_LAYOUT_COLUMN_NUMBER = 2;

    // The number of GIFs pulled from each API request
    private static final int SEARCH_BATCH_SIZE = 18;

    // Extra parameter for the the intent to pass in the search query
    public static final String KEY_QUERY = "KEY_QUERY";

    // Display for the search term
    private TextView mTitleQuery;
    // RecyclerView to display the stream of GIFs
    private RecyclerView mRecyclerView;
    // Back button for returning to MainActivity
    private ImageView mBackButton;

    // Adapter containing the GIF items/view holders, as well as related suggestions view holder
    private GifSearchAdapter<SearchActivity> mSearchAdapter;
    private TenorStaggeredGridLayoutManager mStaggeredGridLayoutManager;

    // Api calls for SearchActivity performed here
    private IGifSearchPresenter mSearchPresenter;

    // Search term
    private String mQuery;

    // Index for the last GIF returned by the API, to pull additional GIFs after the last one
    private String mNextPageId = StringConstant.EMPTY;

    // Boolean to signify a search call has been made.  More calls should not perform while this is set to true
    private boolean mIsLoadingMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mTitleQuery = (TextView) findViewById(R.id.as_tv_query);
        mRecyclerView = (RecyclerView) findViewById(R.id.as_rv_recyclerview);
        mBackButton = (ImageView) findViewById(R.id.as_ib_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mQuery = getIntent().getStringExtra(KEY_QUERY).trim();

        if (!TextUtils.isEmpty(mQuery)) {
            mTitleQuery.setText(mQuery);

            mSearchPresenter = new GifSearchPresenter(this);

            mSearchAdapter = new GifSearchAdapter<>(this);

            // When a search suggestion is clicked, a new instance of SearchActivity will open
            mSearchAdapter.setOnSearchSuggestionClickListener(new SearchSuggestionVH.OnClickListener() {
                @Override
                public void onClick(int position, @NonNull String query, @NonNull String suggestion) {
                    Intent intent = new Intent(SearchActivity.this, SearchActivity.class);
                    intent.putExtra(KEY_QUERY, suggestion);
                    startActivity(intent);
                    finish();
                }
            });

            mSearchAdapter.setSearchQuery(mQuery);
            mStaggeredGridLayoutManager = new TenorStaggeredGridLayoutManager(
                    STAGGERED_GRID_LAYOUT_COLUMN_NUMBER, StaggeredGridLayoutManager.VERTICAL);

            mRecyclerView.addItemDecoration(new GifSearchItemDecoration(AbstractUIUtils.dpToPx(this, 4)));
            mRecyclerView.setAdapter(mSearchAdapter);
            mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
            mRecyclerView.addOnScrollListener(new WeakRefOnScrollListener<SearchActivity>(this) {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if (dy > 0) {
                        final int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                        final int lastVisibleItem = AbstractLayoutManagerUtils.findLastVisibleItemPosition(mStaggeredGridLayoutManager);
                        final int spanCount = AbstractLayoutManagerUtils.getSpanCount(recyclerView.getLayoutManager());

                        /*
                         *
                         * `3 * STAGGERED_GRID_LAYOUT_COLUMN_NUMBER` is a joint effort to avoid swapping,
                         * it kick-starts the load more action when it reaches about 3 rows away from
                         * the bottom of the existing list
                         */
                        if (!mIsLoadingMore && totalItemCount <= (lastVisibleItem + 3 * spanCount)) {
                            mIsLoadingMore = true;
                            performSearch(mQuery, true);
                        }
                    }
                }
            });

            performSearch(mQuery, false);
        }
    }

    @Override
    public Context getContext() {
        return getBaseContext();
    }

    private void performSearch(String query, boolean isAppend) {
        if (!isAppend) {
            mNextPageId = StringConstant.EMPTY;
            mSearchAdapter.clearList();
            mSearchAdapter.addPivotRV();
        }

        if (!TextUtils.isEmpty(query)) {
            mSearchPresenter.search(query, SEARCH_BATCH_SIZE, mNextPageId, isAppend);
        }
    }

    @Override
    public void onReceiveSearchResultsSucceed(@NonNull final GifsResponse response, boolean isAppend) {
        mNextPageId = response.getNext();
        mSearchAdapter.insert(castToRVItems(response.getResults()), isAppend);
        mIsLoadingMore = false;
    }

    @Override
    public void onReceiveSearchResultsFailed(BaseError error, boolean isAppend) {
        if (!isAppend) {
            mSearchAdapter.notifyListEmpty();
        }
    }

    /**
     * Places the Result object into a GifRVItem that can then be placed inside the mSearchAdapter
     * @param results - List of the Result objects from a successful search response
     */
    private static List<AbstractRVItem> castToRVItems(@Nullable final List<Result> results) {
        List<AbstractRVItem> list = new ArrayList<>();
        if (AbstractListUtils.isEmpty(results)) {
            return list;
        }

        for (int i = 0; i < results.size(); i++) {
            list.add(new GifRVItem<>(GifSearchAdapter.TYPE_GIF, results.get(i)).setRelativePosition(i));
        }
        return list;
    }
}

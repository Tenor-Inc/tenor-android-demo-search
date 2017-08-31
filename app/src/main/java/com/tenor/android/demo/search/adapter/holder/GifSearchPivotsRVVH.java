package com.tenor.android.demo.search.adapter.holder;

import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.tenor.android.core.util.AbstractUIUtils;
import com.tenor.android.core.widget.viewholder.StaggeredGridLayoutItemViewHolder;
import com.tenor.android.demo.search.R;
import com.tenor.android.demo.search.adapter.view.IGifSearchView;
import com.tenor.android.sdk.holder.SearchSuggestionVH;
import com.tenor.android.sdk.widget.SearchSuggestionRecyclerView;

public class GifSearchPivotsRVVH<CTX extends IGifSearchView> extends StaggeredGridLayoutItemViewHolder<CTX> {

    // Horizontal RecyclerView to display related search suggestions
    private final SearchSuggestionRecyclerView mPivotsRV;
    // ProgressBar to display while waiting for GIF background to load
    private final int mPivotMargin;

    public GifSearchPivotsRVVH(View itemView, CTX context) {
        super(itemView, context);

        mPivotsRV = (SearchSuggestionRecyclerView) itemView.findViewById(R.id.gspv_rv_pivotlist);

        if (!hasContext()) {
            mPivotMargin = 0;
            return;
        }

        mPivotMargin = AbstractUIUtils.dpToPx(getContext(), 1);
        mPivotsRV.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = mPivotMargin;
                outRect.right = mPivotMargin;
            }
        });
    }

    public void setOnSearchSuggestionClickListener(@Nullable final SearchSuggestionVH.OnClickListener listener) {
        mPivotsRV.setOnSearchSuggestionClickListener(listener);
    }

    public void setQuery(@Nullable final String query) {
        if (!TextUtils.isEmpty(query)) {
            mPivotsRV.getSearchSuggestions(query);
        }
    }
}

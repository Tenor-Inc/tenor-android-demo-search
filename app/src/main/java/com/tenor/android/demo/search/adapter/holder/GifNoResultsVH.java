package com.tenor.android.demo.search.adapter.holder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.widget.viewholder.StaggeredGridLayoutItemViewHolder;
import com.tenor.android.demo.search.R;
import com.tenor.android.demo.search.adapter.view.IGifSearchView;

public class GifNoResultsVH<CTX extends IGifSearchView> extends StaggeredGridLayoutItemViewHolder<CTX> {
    // Text view to tell the user no results were found
    private final TextView mNoResults;

    public GifNoResultsVH(View itemView, CTX context) {
        super(itemView, context);
        mNoResults = (TextView) itemView.findViewById(R.id.no_results);
    }

    public void setNoResultsMessage(@NonNull String message) {
        mNoResults.setText(StringConstant.getOrEmpty(message));
    }
}

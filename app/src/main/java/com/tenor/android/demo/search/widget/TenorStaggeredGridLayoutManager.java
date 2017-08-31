package com.tenor.android.demo.search.widget;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Modified StaggeredGridLayoutManager to disable pre-fetching
 */
public class TenorStaggeredGridLayoutManager extends StaggeredGridLayoutManager {
    public TenorStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setItemPrefetchEnabled(false);
    }

    public TenorStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
        setItemPrefetchEnabled(false);
    }
}

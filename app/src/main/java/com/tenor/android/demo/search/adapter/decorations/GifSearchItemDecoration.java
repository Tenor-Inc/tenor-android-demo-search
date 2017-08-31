package com.tenor.android.demo.search.adapter.decorations;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.tenor.android.demo.search.adapter.GifSearchAdapter;

public class GifSearchItemDecoration extends RecyclerView.ItemDecoration {
    private int mLeft;
    private int mTop;
    private int mRight;
    private int mBottom;

    public GifSearchItemDecoration(int space) {
        this(space, space);
    }

    public GifSearchItemDecoration(int horizontal, int vertical) {
        this(horizontal, vertical, horizontal, vertical);
    }

    public GifSearchItemDecoration(int left, int top, int right, int bottom) {
        mLeft = left;
        mTop = top;
        mRight = right;
        mBottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = 0;
        outRect.top = 0;
        outRect.right = 0;
        outRect.bottom = 0;

        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
        GifSearchAdapter adapter = (GifSearchAdapter) parent.getAdapter();
        final int type = adapter.getItemViewType(position);
        switch (type) {
            case GifSearchAdapter.TYPE_GIF:

                StaggeredGridLayoutManager.LayoutParams lp =
                        (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                final int spanIndex = lp.getSpanIndex();

                if (spanIndex == 0) {
                    // item on left
                    outRect.left = mLeft;
                    outRect.right = mRight / 2;
                } else {
                    // item on right
                    outRect.left = mRight - mRight / 2;
                    outRect.right = mRight;
                }

                if (position == 0) {
                    // first item
                    outRect.top = mTop;
                } else {

                }
                outRect.bottom = mBottom / 2;
                break;
            case GifSearchAdapter.TYPE_SEARCH_PIVOT:
                if (view.getLayoutParams() != null
                        && view.getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {

                    outRect.left = 0;
                    outRect.right = 0;
                    outRect.top = mTop;
                    outRect.bottom = mBottom / 2;
                }
                break;
            case GifSearchAdapter.TYPE_NO_RESULTS:
                outRect.top = mTop * 3;
                outRect.bottom = mBottom;
                break;
            default:
                outRect.left = mLeft;
                outRect.right = mRight;
                outRect.bottom = mBottom;
                break;
        }
    }
}

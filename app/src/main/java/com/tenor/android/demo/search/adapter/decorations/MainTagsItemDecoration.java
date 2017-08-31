package com.tenor.android.demo.search.adapter.decorations;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.tenor.android.demo.search.adapter.TagsAdapter;

public class MainTagsItemDecoration extends RecyclerView.ItemDecoration {
    private int mLeft;
    private int mTop;
    private int mRight;
    private int mBottom;

    public MainTagsItemDecoration(Context context, int space) {
        this(context, space, space);
    }

    public MainTagsItemDecoration(Context context, int horizontal, int vertical) {
        this(context, horizontal, vertical, horizontal, vertical);
    }

    public MainTagsItemDecoration(Context context, int left, int top, int right, int bottom) {
        mLeft = left;
        mTop = top;
        mRight = right;
        mBottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = 0;
        outRect.top = 0;
        outRect.right = 0;
        outRect.bottom = 0;

        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
        if (position < 0) {
            return;
        }
        final int type = parent.getAdapter().getItemViewType(position);

        switch (type) {
            case TagsAdapter.TYPE_REACTION_ITEM:
                final int spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams())
                        .getSpanIndex();

                if (spanIndex == 0) {
                    // item on left
                    outRect.left = mLeft;
                    outRect.right = mRight / 2;
                } else {
                    // item on right
                    outRect.left = mRight - mRight / 2;
                    outRect.right = mRight;
                }

                outRect.top = mTop / 2;
                outRect.bottom = mBottom / 2;
                break;
            default:
                outRect.left = mLeft;
                outRect.top = mTop / 2;
                outRect.right = mRight;
                outRect.bottom = mBottom / 2;
                break;
        }
    }

}

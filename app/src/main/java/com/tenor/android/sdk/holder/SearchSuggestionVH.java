package com.tenor.android.sdk.holder;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.graphics.drawable.TintAwareDrawable;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.TextView;

import com.tenor.android.core.view.IBaseView;
import com.tenor.android.core.widget.viewholder.StaggeredGridLayoutItemViewHolder;
import com.tenor.android.demo.search.R;
import com.tenor.android.sdk.rvitem.SearchSuggestionRVItem;
import com.tenor.android.sdk.util.AbstractColorUtils;
import com.tenor.android.sdk.util.AbstractDrawableUtils;

public class SearchSuggestionVH<CTX extends IBaseView> extends StaggeredGridLayoutItemViewHolder<CTX>
        implements View.OnClickListener {

    /**
     * Interface used to communicate info between adapter and view holder
     */
    public interface OnClickListener {
        void onClick(int position, @NonNull String query, @NonNull String suggestion);
    }

    private static final int[][] STATES = new int[][]{
            new int[]{-android.R.attr.state_pressed}, // pressed = false, default
            new int[]{android.R.attr.state_pressed},  // pressed = true
    };

    @NonNull
    private OnClickListener mListener = new OnClickListener() {
        @Override
        public void onClick(int position, @NonNull String query, @NonNull String suggestion) {
            // do nothing
        }
    };

    private SearchSuggestionRVItem mItem;
    private final TextView mSuggestionField;

    public SearchSuggestionVH(View itemView, CTX context) {
        super(itemView, context);
        mSuggestionField = itemView.findViewById(R.id.ips_tv_title);
        mSuggestionField.setOnClickListener(this);
    }

    public String getSuggestion() {
        return mItem.getSuggestion();
    }

    public String getQuery() {
        return mItem.getQuery();
    }

    public void render(@Nullable final SearchSuggestionRVItem item,
                       @Nullable final OnClickListener listener) {
        if (!hasContext() || item == null) {
            return;
        }

        mItem = item;
        if (listener != null) {
            mListener = listener;
        }

        mSuggestionField.setText(item.getSuggestion());

        if (mSuggestionField instanceof TintableBackgroundView) {
            // "AppCompatTextView" and "com.android.support:appcompat-v7" are used, tint all states
            ViewCompat.setBackgroundTintList(mSuggestionField,
                    new ColorStateList(STATES, new int[]{
                            AbstractColorUtils.getColor(getContext(), item.getPlaceholder()),
                            AbstractColorUtils.getColor(getContext(), R.color.tenor_sdk_primary_color)}));
            return;
        }

        // "com.android.support:appcompat-v7" is likely not being used, and thus "TextView" is used
        Drawable background = mSuggestionField.getBackground();
        if (background instanceof TintAwareDrawable) {
            // tint all states of the given drawable
            DrawableCompat.setTintList(background,
                    new ColorStateList(STATES, new int[]{
                            AbstractColorUtils.getColor(getContext(), item.getPlaceholder()),
                            AbstractColorUtils.getColor(getContext(), R.color.tenor_sdk_primary_color)}));
            return;
        }

        // last option, tint only the background individually
        AbstractDrawableUtils.setDrawableTint(getContext(), background, item.getPlaceholder());
    }

    @Override
    public final void onClick(View v) {
        /*
         * Android Studio requirements on final constant resource id
         *
         * http://tools.android.com/tips/non-constant-fields
         */
        if (v.getId() == R.id.ips_tv_title) {
            mListener.onClick(getAdapterPosition(), getQuery(), getSuggestion());
        }
    }
}


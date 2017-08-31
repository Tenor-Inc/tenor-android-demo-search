package com.tenor.android.sdk.rvitem;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.widget.adapter.AbstractRVItem;

public class SearchSuggestionRVItem extends AbstractRVItem {

    @ColorRes
    private final int mPlaceholder;

    @NonNull
    private final String mQuery;

    public SearchSuggestionRVItem(int type, @NonNull final String query,
                                  @ColorRes int placeholder, @NonNull final String suggestion) {
        super(type, suggestion);
        mQuery = query;
        mPlaceholder = placeholder;
    }

    @NonNull
    public String getQuery() {
        return StringConstant.getOrEmpty(mQuery);
    }

    @ColorRes
    public int getPlaceholder() {
        return mPlaceholder;
    }

    @NonNull
    public String getSuggestion() {
        return getId();
    }
}


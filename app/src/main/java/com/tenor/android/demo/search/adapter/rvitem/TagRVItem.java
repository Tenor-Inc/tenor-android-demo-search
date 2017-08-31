package com.tenor.android.demo.search.adapter.rvitem;

import android.support.annotation.NonNull;

import com.tenor.android.core.model.impl.Tag;
import com.tenor.android.core.widget.adapter.AbstractRVItem;

public class TagRVItem extends AbstractRVItem {
    private final Tag mTag;

    public TagRVItem(int type, @NonNull final Tag tag) {
        super(type, tag.getId());
        mTag = tag;
    }

    @NonNull
    public Tag getTag() {
        return mTag;
    }
}

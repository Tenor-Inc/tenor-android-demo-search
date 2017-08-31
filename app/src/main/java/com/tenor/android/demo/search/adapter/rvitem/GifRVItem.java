package com.tenor.android.demo.search.adapter.rvitem;

import android.support.annotation.NonNull;

import com.tenor.android.core.model.IGif;
import com.tenor.android.core.widget.adapter.AbstractRVItem;

public class GifRVItem<T extends IGif> extends AbstractRVItem {

    private T mGif;

    public GifRVItem(int type, @NonNull final T result) {
        super(type, result.getId());
        mGif = result;
    }

    @NonNull
    public T get() {
        return mGif;
    }
}

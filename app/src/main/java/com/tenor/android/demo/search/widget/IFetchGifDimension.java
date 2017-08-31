package com.tenor.android.demo.search.widget;

import android.support.annotation.NonNull;

public interface IFetchGifDimension {

    void onReceiveViewHolderDimension(@NonNull String id, int width, int height, int orientation);
}

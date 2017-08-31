package com.tenor.android.demo.search.adapter.view;

import com.tenor.android.core.response.BaseError;
import com.tenor.android.core.response.impl.GifsResponse;
import com.tenor.android.core.view.IBaseView;

public interface IGifSearchView extends IBaseView {
    void onReceiveSearchResultsSucceed(GifsResponse response, final boolean isAppend);

    void onReceiveSearchResultsFailed(BaseError error, final boolean isAppend);
}

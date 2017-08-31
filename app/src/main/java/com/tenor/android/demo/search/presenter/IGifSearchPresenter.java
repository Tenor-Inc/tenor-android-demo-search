package com.tenor.android.demo.search.presenter;

import com.tenor.android.core.presenter.IBasePresenter;
import com.tenor.android.core.response.impl.GifsResponse;
import com.tenor.android.demo.search.adapter.view.IGifSearchView;

import retrofit2.Call;

public interface IGifSearchPresenter extends IBasePresenter<IGifSearchView> {
    Call<GifsResponse> search(String query, int limit, String pos, final boolean isAppend);
}
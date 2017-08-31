package com.tenor.android.demo.search.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.network.ApiClient;
import com.tenor.android.core.presenter.impl.BasePresenter;
import com.tenor.android.core.response.BaseError;
import com.tenor.android.core.response.impl.GifsResponse;
import com.tenor.android.demo.search.adapter.view.IGifSearchView;
import com.tenor.android.demo.search.presenter.IGifSearchPresenter;

import retrofit2.Call;

public class GifSearchPresenter extends BasePresenter<IGifSearchView> implements IGifSearchPresenter {
    public GifSearchPresenter(IGifSearchView view) {
        super(view);
    }

    /**
     * Perform an API search call
     * @param query - Term to be searched
     * @param limit - Search batch size
     * @param pos - Last index of the last pulled term.  Empty String if first item
     * @param isAppend - Should the returned results be appeneded to existing results, or is it a new query
     */
    @Override
    public Call<GifsResponse> search(final String query, int limit, String pos, final boolean isAppend) {

        final String qry = !TextUtils.isEmpty(query) ? query : StringConstant.EMPTY;

        Call<GifsResponse> call = ApiClient.getInstance(getView().getContext())
                .search(ApiClient.getServiceIds(getView().getContext()), qry,
                        limit, pos);

        call.enqueue(new BaseWeakRefCallback<GifsResponse>(getWeakRef()) {
            @Override
            public void success(@NonNull IGifSearchView view, @Nullable GifsResponse response) {
                if (response == null) {
                    view.onReceiveSearchResultsFailed(new BaseError(), isAppend);
                    return;
                }

                view.onReceiveSearchResultsSucceed(response, isAppend);
            }

            @Override
            public void failure(@NonNull IGifSearchView view, @Nullable BaseError error) {
                view.onReceiveSearchResultsFailed(error, isAppend);
            }
        });
        return call;
    }
}

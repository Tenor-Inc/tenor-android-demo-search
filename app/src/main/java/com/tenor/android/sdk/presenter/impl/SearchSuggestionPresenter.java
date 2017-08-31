package com.tenor.android.sdk.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenor.android.core.network.ApiClient;
import com.tenor.android.core.network.CallStub;
import com.tenor.android.core.presenter.impl.BasePresenter;
import com.tenor.android.core.response.BaseError;
import com.tenor.android.core.response.impl.SearchSuggestionResponse;
import com.tenor.android.sdk.presenter.ISearchSuggestionPresenter;
import com.tenor.android.sdk.view.ISearchSuggestionView;

import retrofit2.Call;

public class SearchSuggestionPresenter extends BasePresenter<ISearchSuggestionView> implements ISearchSuggestionPresenter {


    public SearchSuggestionPresenter(ISearchSuggestionView view) {
        super(view);
    }

    @Override
    public Call<SearchSuggestionResponse> getSearchSuggestions(@NonNull final String query) {
        if (!hasView()) {
            return new CallStub<>();
        }

        final Call<SearchSuggestionResponse> call =
                ApiClient.getInstance(getWeakRef().get().getContext())
                        .getSearchSuggestions(ApiClient.getServiceIds(getWeakRef().get().getContext()), query, 10);

        call.enqueue(new BaseWeakRefCallback<SearchSuggestionResponse>(getWeakRef()) {

            @Override
            public void success(@NonNull ISearchSuggestionView view, @Nullable SearchSuggestionResponse response) {
                if (call.isCanceled()) {
                    return;
                }

                if (response == null) {
                    view.onReceiveSearchSuggestionsFailed(query, new NullPointerException());
                    return;
                }
                view.onReceiveSearchSuggestionsSucceeded(query, response.getResults());
            }

            @Override
            public void failure(@NonNull ISearchSuggestionView view, BaseError error) {
                if (call.isCanceled()) {
                    return;
                }

                view.onReceiveSearchSuggestionsFailed(query, error);
            }
        });
        return call;
    }
}


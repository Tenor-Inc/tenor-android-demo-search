package com.tenor.android.sdk.presenter;

import android.support.annotation.NonNull;

import com.tenor.android.core.presenter.IBasePresenter;
import com.tenor.android.core.response.impl.SearchSuggestionResponse;
import com.tenor.android.sdk.view.ISearchSuggestionView;

import retrofit2.Call;

public interface ISearchSuggestionPresenter extends IBasePresenter<ISearchSuggestionView> {
    Call<SearchSuggestionResponse> getSearchSuggestions(@NonNull String query);
}

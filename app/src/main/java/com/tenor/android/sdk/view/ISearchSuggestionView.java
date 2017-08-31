package com.tenor.android.sdk.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenor.android.core.view.IBaseView;

import java.util.List;

public interface ISearchSuggestionView extends IBaseView {

    void onReceiveSearchSuggestionsSucceeded(@NonNull String query, @NonNull List<String> suggestions);

    void onReceiveSearchSuggestionsFailed(@NonNull String query, @Nullable Exception error);
}


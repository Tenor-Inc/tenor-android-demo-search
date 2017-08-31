package com.tenor.android.demo.search.presenter.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.network.ApiClient;
import com.tenor.android.core.presenter.impl.BasePresenter;
import com.tenor.android.core.response.BaseError;
import com.tenor.android.core.response.impl.TagsResponse;
import com.tenor.android.core.util.AbstractListUtils;
import com.tenor.android.core.util.AbstractLocaleUtils;
import com.tenor.android.demo.search.adapter.view.IMainView;
import com.tenor.android.demo.search.presenter.IMainPresenter;

import java.util.List;

import retrofit2.Call;

public class MainPresenter extends BasePresenter<IMainView> implements IMainPresenter {
    public MainPresenter(IMainView view) {
        super(view);
    }

    /**
     * Api call to fetch a list of Tag items
     * @param context
     * @param categories - optional field to pull specific types of tags.  Null if pulling from top of the results
     */
    @Override
    public Call<TagsResponse> getTags(@NonNull Context context, @Nullable List<String> categories) {
        final String c = !AbstractListUtils.isEmpty(categories) ? TextUtils.join(StringConstant.COMMA, categories)
                : StringConstant.EMPTY;

        Call<TagsResponse> call = ApiClient.getInstance(getView().getContext())
                .getTags(ApiClient.getServiceIds(getView().getContext()), c, AbstractLocaleUtils.getUtcOffset(context));

        call.enqueue(new BaseWeakRefCallback<TagsResponse>(getWeakRef()) {
            @Override
            public void success(@NonNull IMainView view, @Nullable TagsResponse response) {
                if (response == null || AbstractListUtils.isEmpty(response.getTags())) {
                    view.onReceiveReactionsFailed(new BaseError());
                    return;
                }
                view.onReceiveReactionsSucceeded(response.getTags());
            }

            @Override
            public void failure(@NonNull IMainView view, @Nullable BaseError error) {
                view.onReceiveReactionsFailed(error);
            }
        });
        return call;
    }
}

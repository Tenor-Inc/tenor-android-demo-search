package com.tenor.android.demo.search.presenter;

import android.content.Context;

import com.tenor.android.core.presenter.IBasePresenter;
import com.tenor.android.core.response.impl.TagsResponse;
import com.tenor.android.demo.search.adapter.view.IMainView;

import java.util.List;

import retrofit2.Call;

public interface IMainPresenter extends IBasePresenter<IMainView> {
    Call<TagsResponse> getTags(Context context, List<String> categories);
}

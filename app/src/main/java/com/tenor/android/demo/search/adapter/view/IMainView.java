package com.tenor.android.demo.search.adapter.view;

import com.tenor.android.core.model.impl.Tag;
import com.tenor.android.core.response.BaseError;
import com.tenor.android.core.view.IBaseView;

import java.util.List;

public interface IMainView extends IBaseView {
    void onReceiveReactionsSucceeded(List<Tag> tags);
    void onReceiveReactionsFailed(BaseError error);
}

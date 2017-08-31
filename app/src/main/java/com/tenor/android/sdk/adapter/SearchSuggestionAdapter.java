package com.tenor.android.sdk.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tenor.android.core.util.AbstractListUtils;
import com.tenor.android.core.view.IBaseView;
import com.tenor.android.core.widget.adapter.ListRVAdapter;
import com.tenor.android.core.widget.viewholder.StaggeredGridLayoutItemViewHolder;
import com.tenor.android.demo.search.R;
import com.tenor.android.sdk.holder.SearchSuggestionVH;
import com.tenor.android.sdk.rvitem.SearchSuggestionRVItem;

import java.util.List;

public class SearchSuggestionAdapter<CTX extends IBaseView> extends ListRVAdapter<CTX, SearchSuggestionRVItem, StaggeredGridLayoutItemViewHolder<CTX>> {

    private SearchSuggestionVH.OnClickListener mOnSearchSuggestionClickListener;

    public final static int TYPE_DID_YOU_MEAN_SUGGESTION = 2;

    public SearchSuggestionAdapter(@NonNull final CTX context) {
        super(context);
    }

    public void setOnSearchSuggestionClickListener(@Nullable final SearchSuggestionVH.OnClickListener onSearchSuggestionClickListener) {
        // use the given correction listener
        mOnSearchSuggestionClickListener = onSearchSuggestionClickListener;
    }

    /**
     * Should only be used for telescoping suggestions
     * isAppend should always be false
     *
     * @param list     the list
     * @param isAppend should be always false for {@link SearchSuggestionAdapter}
     */
    @Override
    public void insert(@Nullable final List<SearchSuggestionRVItem> list, boolean isAppend) {

        if (AbstractListUtils.isEmpty(list)) {
            notifyDataSetChanged();
            return;
        }

        // intentional ignore append case in here
        getList().clear();
        getList().addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public StaggeredGridLayoutItemViewHolder<CTX> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_DID_YOU_MEAN_SUGGESTION:
            default:
                return new SearchSuggestionVH<>(
                        inflater.inflate(R.layout.item_search_suggestion, parent, false),
                        getRef());
        }
    }

    @Override
    public void onBindViewHolder(StaggeredGridLayoutItemViewHolder<CTX> viewHolder, int position) {
        if (viewHolder instanceof SearchSuggestionVH) {
            final SearchSuggestionVH holder = (SearchSuggestionVH) viewHolder;
            holder.render(getList().get(position), mOnSearchSuggestionClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return getList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return getList().get(position).getType();
    }
}

package com.tenor.android.demo.search.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tenor.android.core.model.impl.Tag;
import com.tenor.android.core.util.AbstractListUtils;
import com.tenor.android.core.widget.adapter.ListRVAdapter;
import com.tenor.android.demo.search.R;
import com.tenor.android.demo.search.activity.MainActivity;
import com.tenor.android.demo.search.adapter.holder.TagItemVH;
import com.tenor.android.demo.search.adapter.rvitem.TagRVItem;

import java.util.List;

/**
 * Adapter to display Tags as a list, with the option either multiple columns or multiple rows
 * depending on orientation given by the LayoutManager.
 */
public class TagsAdapter<CTX extends MainActivity>
        extends ListRVAdapter<CTX, TagRVItem, TagItemVH<CTX>> {

    public static final int TYPE_REACTION_ITEM = 0;

    public TagsAdapter(@Nullable CTX ctx) {
        super(ctx);
    }

    @Override
    public void insert(@Nullable List<TagRVItem> list, boolean isAppend) {
        if (!isAppend) {
            getList().clear();
            if (AbstractListUtils.isEmpty(list)) {
                notifyDataSetChanged();
                return;
            }
        }

        if (AbstractListUtils.isEmpty(list)) {
            return;
        }

        final int start = getItemCount();
        final int size = list.size();
        getList().addAll(list);

        if (!isAppend) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeChanged(start, size);
        }
    }

    @Override
    public TagItemVH<CTX> onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create Tag view holder
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new TagItemVH<>(
                layoutInflater.inflate(R.layout.item_tag, parent, false),
                getRef());
    }

    @Override
    public void onBindViewHolder(TagItemVH<CTX> holder, int position) {
        final Tag reactionTag = getList().get(position).getTag();
        holder.render(reactionTag);
    }

    @Override
    public int getItemCount() {
        return getList().size();
    }
}

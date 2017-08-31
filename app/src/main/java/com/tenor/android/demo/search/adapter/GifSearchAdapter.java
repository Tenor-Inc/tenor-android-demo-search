package com.tenor.android.demo.search.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tenor.android.core.model.impl.Result;
import com.tenor.android.core.util.AbstractListUtils;
import com.tenor.android.core.widget.adapter.AbstractRVItem;
import com.tenor.android.core.widget.adapter.ListRVAdapter;
import com.tenor.android.core.widget.viewholder.StaggeredGridLayoutItemViewHolder;
import com.tenor.android.demo.search.R;
import com.tenor.android.demo.search.adapter.holder.GifNoResultsVH;
import com.tenor.android.demo.search.adapter.holder.GifSearchItemVH;
import com.tenor.android.demo.search.adapter.holder.GifSearchPivotsRVVH;
import com.tenor.android.demo.search.adapter.rvitem.GifRVItem;
import com.tenor.android.demo.search.adapter.view.IGifSearchView;
import com.tenor.android.demo.search.widget.IFetchGifDimension;
import com.tenor.android.sdk.holder.SearchSuggestionVH;

import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Adapter to display GIFs as a list, with the option either multiple columns or multiple rows
 * depending on orientation given by the LayoutManager,
 *
 * Terms with related search terms will display terms as the first list item.
 *
 * If no Gifs are found for the search term, a "No GIFs were found" TextView will display as a single list item.
 */
public class GifSearchAdapter<CTX extends IGifSearchView>
        extends ListRVAdapter<CTX, AbstractRVItem, StaggeredGridLayoutItemViewHolder<CTX>> implements IFetchGifDimension {

    private String mQuery;

    // Used to set aspect ratio of GIFs, so as to not cause position shifting in the waterfall layout
    private final Map<String, Integer> mHeights;

    public final static int TYPE_NO_RESULTS = 0;
    public final static int TYPE_SEARCH_PIVOT = 1;
    public final static int TYPE_GIF = 2;

    private static final String ID_ITEM_NO_RESULT = "ID_ITEM_NO_RESULT";
    private static final String ID_ITEM_SEARCH_PIVOT = "ID_ITEM_SEARCH_PIVOT";

    // Item to display a "No GIFs were found" TextView
    private static final AbstractRVItem NO_RESULT_ITEM = new AbstractRVItem(TYPE_NO_RESULTS,
            ID_ITEM_NO_RESULT) {
    };

    // Item to show related search terms in a horizontal RecyclerView
    private static final AbstractRVItem SEARCH_PIVOT_RV_ITEM = new AbstractRVItem(TYPE_SEARCH_PIVOT,
            ID_ITEM_SEARCH_PIVOT) {
    };

    // Listener for when a related suggestion is clicked
    private SearchSuggestionVH.OnClickListener mOnSearchSuggestionClickListener;

    public GifSearchAdapter(@NonNull CTX context) {
        super(context);
        mHeights = new ArrayMap<>();
    }

    @Override
    public StaggeredGridLayoutItemViewHolder<CTX> onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            // View holder for related search terms RecyclerView
            case TYPE_SEARCH_PIVOT:
                GifSearchPivotsRVVH<CTX> holder = new GifSearchPivotsRVVH<>(
                        inflater.inflate(R.layout.gif_search_pivots_view, parent, false),
                        getRef());
                holder.setFullSpan(true);
                holder.setOnSearchSuggestionClickListener(mOnSearchSuggestionClickListener);
                holder.setQuery(mQuery);
                return holder;
            // View holder for "no results" TextView
            case TYPE_NO_RESULTS:
                return new GifNoResultsVH<>(
                        inflater.inflate(R.layout.gif_search_no_results, parent, false),
                        getRef());
            // View holder for GIF results
            case TYPE_GIF:
            default:
                return new GifSearchItemVH<>(inflater.inflate(R.layout.gif_base, parent, false), getRef());
        }
    }

    @Override
    public void onBindViewHolder(StaggeredGridLayoutItemViewHolder<CTX> viewHolder, int position) {
        if (viewHolder instanceof GifSearchPivotsRVVH) {
            final GifSearchPivotsRVVH holder = (GifSearchPivotsRVVH) viewHolder;
            holder.setFullWidthWithHeight();
            return;
        }

        if (viewHolder instanceof GifNoResultsVH) {
            final GifNoResultsVH holder = (GifNoResultsVH) viewHolder;
            holder.setFullWidthWithHeight();
            return;
        }

        if (viewHolder instanceof GifSearchItemVH) {
            final GifSearchItemVH holder = (GifSearchItemVH) viewHolder;

            if (!(getList().get(position) instanceof GifRVItem)) {
                return;
            }
            final GifRVItem item = (GifRVItem) getList().get(position);

            if (!(item.get() instanceof Result)) {
                return;
            }

            if (mHeights.containsKey(item.getId())) {
                holder.setHeightInPixel(mHeights.get(item.getId()));
            } else {
                holder.setFetchGifHeightListener(this);
                holder.setupViewHolder((Result) item.get(), OrientationHelper.VERTICAL);
            }
            holder.renderGif((Result) item.get());
            return;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getList().get(position).getType();
    }

    @Override
    public int getItemCount() {
        return getList().size();
    }

    public void setSearchQuery(@Nullable final String query) {
        // allow empty string
        if (query != null) {
            mQuery = query;
        }
    }

    // Add related search RecyclerView, if terms exist
    public void addPivotRV() {
        if (AbstractListUtils.isEmpty(getList()) ||
                getList().get(0).getType() != TYPE_SEARCH_PIVOT) {
            getList().add(0, SEARCH_PIVOT_RV_ITEM);
            notifyItemInserted(0);
        }
    }

    public void setOnSearchSuggestionClickListener(@Nullable final SearchSuggestionVH.OnClickListener listener) {
        mOnSearchSuggestionClickListener = listener;
    }

    @Override
    public void insert(@Nullable final List<AbstractRVItem> list, boolean isAppend) {
        if (AbstractListUtils.isEmpty(list) && !isAppend) {
            notifyListEmpty();
            return;
        }

        if (!isAppend) {
            // If GIFs are refreshed, we want to keep the same related suggestions, so long as the term has not changed
            threadSafeRemove(new IThreadSafeConditions<AbstractRVItem>() {
                @Override
                public boolean removeIf(AbstractRVItem item) {
                    return item.getType() != TYPE_SEARCH_PIVOT;
                }

                @Override
                public void onItemsRemoved(Stack<Integer> positions) {
                    // do nothing
                }
            });
            mHeights.clear();
        }

        if (!AbstractListUtils.isEmpty(list) && list.get(0) instanceof GifRVItem) {
            getList().addAll(list);

            int start = getItemCount();
            if (!isAppend) {
                notifyDataSetChanged();
            } else {
                notifyItemRangeInserted(start, list.size());
            }
        }
    }

    // Display "No GIFs were found" TextView view holder if no results are found
    public void notifyListEmpty() {
        clearList();
        mHeights.clear();
        getList().add(NO_RESULT_ITEM);
        notifyDataSetChanged();
    }

    @Override
    public void onReceiveViewHolderDimension(@NonNull String id, int width, int height, int orientation) {
        if (orientation == OrientationHelper.VERTICAL) {
            mHeights.put(id, height);
        }
    }
}

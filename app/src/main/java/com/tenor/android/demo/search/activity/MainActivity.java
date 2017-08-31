package com.tenor.android.demo.search.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.model.impl.Tag;
import com.tenor.android.core.response.BaseError;
import com.tenor.android.core.util.AbstractUIUtils;
import com.tenor.android.core.widget.adapter.AbstractRVItem;
import com.tenor.android.demo.search.R;
import com.tenor.android.demo.search.adapter.TagsAdapter;
import com.tenor.android.demo.search.adapter.decorations.MainTagsItemDecoration;
import com.tenor.android.demo.search.adapter.rvitem.TagRVItem;
import com.tenor.android.demo.search.adapter.view.IMainView;
import com.tenor.android.demo.search.presenter.IMainPresenter;
import com.tenor.android.demo.search.presenter.impl.MainPresenter;
import com.tenor.android.demo.search.widget.TenorStaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * For the MainActivity, we will display a search bar followed by a stream of Tags pulled from the Tenor API.
 * Either by clicking on a tag or entering a search, SearchActivity will open.
 */
public class MainActivity extends AppCompatActivity implements IMainView{
    // Number of columns for the RecyclerView
    private static final int STAGGERED_GRID_LAYOUT_COLUMN_NUMBER = 2;
    // Minimum length a search term can be
    private static final int TEXT_QUERY_MIN_LENGTH = 2;

    // A search box for entering a search term
    public EditText mEditText;
    // RecyclerView to display the stream of Tags
    public RecyclerView mRecyclerView;

    // Api calls for MainActivity performed here
    private IMainPresenter mPresenter;
    // Adapter containing the tag items/view holders
    private TagsAdapter mTagsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.am_et_search);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                final String query = textView.getText().toString().trim();

                if (query.length() < TEXT_QUERY_MIN_LENGTH) {
                    Toast.makeText(MainActivity.this, getString(R.string.search_error), Toast.LENGTH_LONG).show();
                    return true;
                }

                // The keyboard enter will perform the search
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startSearch(query);
                    return true;
                }
                return false;
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.am_rv_tags);
        mRecyclerView.addItemDecoration(new MainTagsItemDecoration(getContext(), AbstractUIUtils.dpToPx(this, 2)));

        // Two column, vertical display
        final TenorStaggeredGridLayoutManager layoutManager = new TenorStaggeredGridLayoutManager(STAGGERED_GRID_LAYOUT_COLUMN_NUMBER,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mTagsAdapter = new TagsAdapter<>(this);
        mRecyclerView.setAdapter(mTagsAdapter);

        mPresenter = new MainPresenter(this);
        mPresenter.getTags(getContext(), null);

    }

    private void startSearch(@Nullable final CharSequence text) {
        final String query = !TextUtils.isEmpty(text) ? text.toString().trim() : StringConstant.EMPTY;
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(SearchActivity.KEY_QUERY, query);
        startActivity(intent);
    }

    @Override
    public Context getContext() {
        return getBaseContext();
    }

    @Override
    public void onReceiveReactionsSucceeded(List<Tag> tags) {

        // Map the tags into a list of TagRVItem for the mTagsAdapter
        List<AbstractRVItem> list = new ArrayList<>();
        for (Tag tag : tags) {
            list.add(new TagRVItem(TagsAdapter.TYPE_REACTION_ITEM, tag));
        }
        mTagsAdapter.insert(list, false);
    }

    @Override
    public void onReceiveReactionsFailed(BaseError error) {
        // For now, we will just display nothing if the tags fail to return
    }
}

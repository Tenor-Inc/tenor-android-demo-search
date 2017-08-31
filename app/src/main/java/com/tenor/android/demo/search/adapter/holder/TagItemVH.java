package com.tenor.android.demo.search.adapter.holder;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tenor.android.core.loader.GlideTaskParams;
import com.tenor.android.core.loader.WeakRefContentLoaderTaskListener;
import com.tenor.android.core.loader.gif.GifLoader;
import com.tenor.android.core.model.impl.Tag;
import com.tenor.android.core.widget.viewholder.StaggeredGridLayoutItemViewHolder;
import com.tenor.android.demo.search.R;
import com.tenor.android.demo.search.activity.SearchActivity;
import com.tenor.android.demo.search.adapter.view.IMainView;

public class TagItemVH<CTX extends IMainView> extends StaggeredGridLayoutItemViewHolder<CTX> {

    // Preview GIF image shown in the background
    private final ImageView mImage;
    // Display name of the Tag
    private final TextView mName;
    private final ProgressBar mLoadingProgress;

    private Tag mTag;

    public TagItemVH(@NonNull View itemView, CTX context) {
        super(itemView, context);

        mImage = itemView.findViewById(R.id.it_iv_image);
        mName = itemView.findViewById(R.id.it_tv_name);
        mLoadingProgress = itemView.findViewById(R.id.it_pb_loading);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClicked();
            }
        });
    }

    public void render(@Nullable final Tag tag) {
        if (tag == null) {
            return;
        }
        mTag = tag;
        this.setText(tag.getName()).setImage(tag.getImage());
    }

    private TagItemVH setText(@Nullable final CharSequence text) {
        // empty string allowed
        if (text == null) {
            return this;
        }
        mName.setText(text);
        return this;
    }

    private TagItemVH<CTX> setImage(@Nullable final String image) {
        if (TextUtils.isEmpty(image)) {
            return this;
        }

        // normal load to display
        mLoadingProgress.setVisibility(View.VISIBLE);

        // Load GIF preview background into mImage
        GlideTaskParams<ImageView> params = new GlideTaskParams<>(mImage, image);
        params.setListener(new WeakRefContentLoaderTaskListener<CTX, ImageView>(getRef()) {
            @Override
            public void success(@NonNull CTX ctx, @NonNull ImageView imageView, @Nullable Drawable drawable) {
                mLoadingProgress.setVisibility(View.GONE);
            }

            @Override
            public void failure(@NonNull CTX ctx, @NonNull ImageView imageView, @Nullable Drawable drawable) {
                mLoadingProgress.setVisibility(View.GONE);
            }
        });

        GifLoader.loadGif(getContext(), params);
        return this;
    }

    public Tag getTag() {
        return mTag;
    }

    private void onClicked() {
        if (!hasContext()) {
            return;
        }

        // Start a new search
        Intent intent = new Intent(getContext(), SearchActivity.class);
        intent.putExtra(SearchActivity.KEY_QUERY, getTag().getSearchTerm());
        getContext().startActivity(intent);
    }
}

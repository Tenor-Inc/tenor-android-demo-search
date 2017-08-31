package com.tenor.android.demo.search.adapter.holder;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tenor.android.core.constant.MediaCollectionFormats;
import com.tenor.android.core.loader.GlideTaskParams;
import com.tenor.android.core.loader.WeakRefContentLoaderTaskListener;
import com.tenor.android.core.loader.gif.GifLoader;
import com.tenor.android.core.model.impl.Result;
import com.tenor.android.core.network.ApiClient;
import com.tenor.android.core.util.AbstractListUtils;
import com.tenor.android.core.widget.viewholder.StaggeredGridLayoutItemViewHolder;
import com.tenor.android.demo.search.R;
import com.tenor.android.demo.search.adapter.view.IGifSearchView;
import com.tenor.android.demo.search.widget.IFetchGifDimension;
import com.tenor.android.sdk.concurrency.WeakRefOnPreDrawListener;

public class GifSearchItemVH<CTX extends IGifSearchView> extends StaggeredGridLayoutItemViewHolder<CTX> {

    // ImageView to contain and display the GIF
    private final ImageView mImageView;
    // ProgressBar to display while waiting for GIF to load
    private final ProgressBar mProgressBar;
    // Icon to signify the GIF is an mp4 with sound
    private final View mAudio;

    // Model with the necessary GIF fields, including urls
    private Result mResult;

    // Waits for holder's view to be pre-drawn, and returns the height and width values based on the GIFs aspectRatio
    private IFetchGifDimension mFetchGifDimensionListener;

    public GifSearchItemVH(View itemView, CTX ctx) {
        super(itemView, ctx);

        mImageView = (ImageView) itemView.findViewById(R.id.gdi_iv_image);
        mProgressBar = (ProgressBar) itemView.findViewById(R.id.gdi_pb_loading);
        mAudio = itemView.findViewById(R.id.gdi_v_audio);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClicked();
            }
        });

    }

    public void renderGif(@Nullable final Result result) {
        if (result == null || !hasContext()) {
            return;
        }
        mResult = result;

        // Only show mAudio for Results with sound
        if (result.isHasAudio()) {
            mAudio.setVisibility(View.VISIBLE);
        } else {
            mAudio.setVisibility(View.GONE);
        }

        if (AbstractListUtils.isEmpty(result.getMedias())) {
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);

        final String url = result.getMedias().get(0).get(MediaCollectionFormats.GIF_TINY).getUrl();
        GlideTaskParams<ImageView> params = new GlideTaskParams<>(mImageView, url);
        params.setPlaceholder(result.getPlaceholderColorHex());
        params.setListener(new WeakRefContentLoaderTaskListener<CTX, ImageView>(getRef()) {
            @Override
            public void success(@NonNull CTX ctx, @NonNull ImageView imageView, @Nullable Drawable drawable) {
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void failure(@NonNull CTX ctx, @NonNull ImageView imageView, @Nullable Drawable drawable) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
        // Load GIF into mImageView
        GifLoader.loadGif(getContext(), params);
    }

    public void setFetchGifHeightListener(IFetchGifDimension fetchGifDimensionListener) {
        mFetchGifDimensionListener = fetchGifDimensionListener;
    }

    public boolean setupViewHolder(@Nullable final Result result, int orientation) {
        if (result == null || !hasContext()) {
            return false;
        }
        postChangeGifViewDimension(itemView, result, orientation);
        return true;
    }

    private void postChangeGifViewDimension(@NonNull View view, final @NonNull Result result, final int orientation) {

        final float aspectRatio = result.getMedias().get(0).get("GIF_TINY").getAspectRatio();

        /*
         * Re-adjust itemView size on items without exterior ad badge.
         * The MATCH_PARENT on the image view is two levels too low,
         * and thus got dominated by it parent views
         */
        view.getViewTreeObserver().addOnPreDrawListener(new WeakRefOnPreDrawListener<View>(view) {
            @Override
            public boolean onPreDraw(@NonNull View view) {
                view.getViewTreeObserver().removeOnPreDrawListener(this);

                ViewGroup.LayoutParams params = view.getLayoutParams();

                // Calculate the height/width of the GIF based on aspectRatio and orientation
                if (orientation == OrientationHelper.VERTICAL) {
                    params.height = Math.round((view.getMeasuredWidth() / aspectRatio));
                }

                if (orientation == OrientationHelper.HORIZONTAL) {
                    params.width = Math.round((view.getMeasuredHeight() * aspectRatio));
                }
                if (mFetchGifDimensionListener != null) {
                    mFetchGifDimensionListener.onReceiveViewHolderDimension(result.getId(), params.width,
                            params.height, orientation);
                }
                view.setLayoutParams(params);
                return true;
            }
        });
    }

    private void onClicked() {
        if (!hasContext()) {
            return;
        }

        if (mResult == null) {
            return;
        }

        // register share to receive more relevant results in the future
        ApiClient.registerShare(getContext(), mResult.getId());

        // todo - Add in functionality for when a GIF is clicked by the user
        Toast.makeText(getContext(), "Add your click functionality here", Toast.LENGTH_LONG).show();
    }
}

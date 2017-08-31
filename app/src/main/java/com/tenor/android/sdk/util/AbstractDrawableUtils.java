package com.tenor.android.sdk.util;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;

public abstract class AbstractDrawableUtils {

    /**
     * Sets a tint on top of the desired drawable
     *
     * @param context        the context
     * @param drawable       source drawable to apply the tint
     * @param tintColorResId color res id of the desired tint
     */
    public static void setDrawableTint(@Nullable final Context context,
                                       @NonNull final Drawable drawable,
                                       @ColorRes final int tintColorResId) {
        if (context == null || drawable == null) {
            throw new IllegalArgumentException("inputs cannot be null, context: " + context
                    + ", drawable: " + drawable);
        }
        setDrawableTint(drawable, AbstractColorUtils.getColor(context, tintColorResId));
    }

    /**
     * Sets a tint on top of the desired drawable
     *
     * @param drawable     source drawable to apply the tint
     * @param tintColorInt color int of the desired tint
     */
    public static void setDrawableTint(@NonNull final Drawable drawable,
                                       @ColorInt final int tintColorInt) {
        /*
         * === FIXED ===
         * In the latest support library, DrawableCompat.wrap() is no longer needed
         *
         * There is an invalidation issue when setting drawable state on pre-lollipop devices,
         * even if using the DrawableCompat.setTint() method.  It appears to be google support
         * library issue.  The get around is to use DrawableCompat.wrap() to wrap the drawable
         * before setting tint color
         *
         * http://stackoverflow.com/questions/30872101
         * https://code.google.com/p/android/issues/detail?id=172067#c13
         * =============
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            /*
             * Tint ONLY the targeted drawable without affecting other drawables in the app.
             *
             * http://stackoverflow.com/questions/26788251
             */
            Drawable mutateDrawable = drawable.mutate();

            // Work for API 17 and below as well
            mutateDrawable.setColorFilter(tintColorInt, PorterDuff.Mode.SRC_IN);
        } else {
            DrawableCompat.setTint(drawable, tintColorInt);
        }
    }
}
package com.tenor.android.sdk.util;

import android.support.annotation.ColorRes;
import android.support.annotation.IntRange;

import com.tenor.android.demo.search.R;

public class ColorPalette {

    private final int mCount;
    private final int[] mColorPalette;
    private int[] mRandomizedPalette;

    public ColorPalette(@ColorRes int[] colorResIds) {
        mCount = colorResIds.length;
        if (mCount <= 0) {
            throw new IllegalArgumentException("length of input color resource ids cannot be less than 1");
        }
        mColorPalette = colorResIds;
        shuffle();
    }

    public void shuffle() {
        mRandomizedPalette = SdkListUtils.shuffle(mColorPalette);
    }

    /**
     * Get a {@link ColorRes} from the given color resource ids
     *
     * @param index the index of the randomized {@link ColorRes}
     * @return a {@link ColorRes}
     */
    @ColorRes
    public int get(@IntRange(from = 0, to = Integer.MAX_VALUE) int index) {
        if (index < 0) {
            index = Math.abs(index);
        }
        return mColorPalette[index % mCount];
    }

    /**
     * Get a random {@link ColorRes} from the given color resource ids
     *
     * @param index the index of the randomized {@link ColorRes}
     * @return a random {@link ColorRes}
     */
    @ColorRes
    public int random(@IntRange(from = 0, to = Integer.MAX_VALUE) int index) {
        if (index < 0) {
            index = Math.abs(index);
        }
        return mRandomizedPalette[index % mCount];
    }

    /*
     * ==============
     * Static Methods
     * ==============
     */

    @ColorRes
    private static final int[] COLOR_RESOURCE_IDS = new int[]{
            R.color.color_palette_green,
            R.color.color_palette_light_blue,
            R.color.color_palette_yellow,
            R.color.color_palette_deep_purple,
            R.color.color_palette_red,
    };

    private static final ColorPalette COLOR_PALETTE = new ColorPalette(COLOR_RESOURCE_IDS);

    /**
     * Get a random {@link ColorRes} from the randomized {@link #COLOR_RESOURCE_IDS}
     *
     * @param index the index of the randomized {@link ColorRes}
     * @return a random {@link ColorRes}
     */
    @ColorRes
    public static int getRandomColorResId(@IntRange(from = 0, to = Integer.MAX_VALUE) int index) {
        return COLOR_PALETTE.random(index);
    }
}

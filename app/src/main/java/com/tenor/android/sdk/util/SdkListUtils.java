package com.tenor.android.sdk.util;

import android.support.annotation.NonNull;

import com.tenor.android.core.util.AbstractListUtils;

import java.util.Random;

public class SdkListUtils extends AbstractListUtils {

    /**
     * Shuffle a given {@link int}[]
     * <p>
     * A implementation of Fisher–Yates shuffle
     *
     * @param array given {@link int}[] to be shuffled
     * @return a shuffled {@link int}[]
     */
    @NonNull
    public static int[] shuffle(@NonNull int[] array) {
        if (array.length <= 0) {
            return array;
        }
        Random random = RandomCompat.get();
        int randInt;
        int temp;
        for (int i = array.length - 1; i > 0; i--) {
            randInt = random.nextInt(i + 1);
            temp = array[randInt];
            array[randInt] = array[i];
            array[i] = temp;
        }
        return array;
    }
}

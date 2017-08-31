package com.tenor.android.sdk.concurrency;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;

import com.tenor.android.core.weakref.WeakRefObject;

import java.lang.ref.WeakReference;

public abstract class WeakRefOnPreDrawListener<T extends View> extends WeakRefObject<T>
        implements ViewTreeObserver.OnPreDrawListener {

    public WeakRefOnPreDrawListener(@NonNull T t) {
        super(t);
    }

    public WeakRefOnPreDrawListener(@NonNull WeakReference<T> weakRef) {
        super(weakRef);
    }

    @Override
    public boolean onPreDraw() {
        if (!hasRef()) {
            return false;
        }

        final ViewTreeObserver observer = getWeakRef().get().getViewTreeObserver();
        if (observer == null || !observer.isAlive()) {
            return false;
        }

        observer.removeOnPreDrawListener(this);
        return onPreDraw(getWeakRef().get());
    }

    public abstract boolean onPreDraw(@NonNull T t);
}

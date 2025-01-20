package com.felippeneves.tutorial_android_application.common;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseObservable<LISTENER_CLASS> {

    // thread-safe set of listeners
    private final Set<LISTENER_CLASS> mListeners = Collections.newSetFromMap(
            new ConcurrentHashMap<>(1));


    public final void registerListener(LISTENER_CLASS listener) {
        mListeners.add(listener);
    }

    public final void unregisterListener(LISTENER_CLASS listener) {
        mListeners.remove(listener);
    }

    protected final Set<LISTENER_CLASS> getListeners() {
        return Collections.unmodifiableSet(mListeners);
    }
}

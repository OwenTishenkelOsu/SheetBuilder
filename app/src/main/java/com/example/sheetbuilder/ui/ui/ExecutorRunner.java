package com.example.sheetbuilder.ui.ui;

import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import model.Sheet;
import timber.log.Timber;

public class ExecutorRunner {
    // Executor instance
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    // Handler for updating the UI thread
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private List<Sheet> mResult;

    private final String TAG = getClass().getSimpleName();

    public interface Callback<R> {
        void onComplete(R r);
        void onError(Exception e);
    }

    public <R> void execute(Callable<R> callable, Callback<R> callback) {
        mExecutor.execute(() -> {
            final R result;

            try {
                Timber.tag(TAG).d("Invoking call() on callable");
                result = callable.call();
                Timber.tag(TAG).d("Posting onComplete()");
                mHandler.post(() -> callback.onComplete(result));

            } catch (Exception e) {
                Timber.tag(TAG).e("Error running Executor");
                e.printStackTrace();
                mHandler.post(() -> callback.onError(e));
            }
        });
    }
}
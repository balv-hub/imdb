package com.balv.imdb.data.network;

import androidx.annotation.NonNull;

import java.io.IOException;

import kotlin.NotImplementedError;
import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class CallDelegate<TIn, TOut> implements Call<TOut> {
    Call<TIn> proxy;
    public CallDelegate(Call<TIn> proxy) {
        this.proxy = proxy;
    }
    @NonNull
    @Override
    public Response<TOut> execute() throws IOException {
        throw new NotImplementedError();
    }

    @Override
    public void enqueue(@NonNull Callback<TOut> callback) {
        enqueueImpl(callback);
    }

    @Override
    public boolean isExecuted() {
        return proxy.isExecuted();
    }

    @Override
    public void cancel() {
        proxy.cancel();
    }

    @Override
    public boolean isCanceled() {
        return proxy.isCanceled();
    }

    @NonNull
    @Override
    public Call<TOut> clone() {
        return cloneImpl();
    }

    @NonNull
    @Override
    public Request request() {
        return proxy.request();
    }

    @NonNull
    @Override
    public Timeout timeout() {
        return proxy.timeout();
    }

    abstract void enqueueImpl(Callback<TOut> callback);
    abstract Call<TOut> cloneImpl();
}

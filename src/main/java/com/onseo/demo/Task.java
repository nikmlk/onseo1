package com.onseo.demo;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class Task implements Runnable {
    private final static AtomicLong counterAll = new AtomicLong(0);
    private final static AtomicLong counterSuccess = new AtomicLong(0);
    private final static AtomicLong counterFail = new AtomicLong(0);
    private final OkHttpClient httpClient;
    private final Request request;

    public Task(OkHttpClient httpClient, Request request) {
        this.httpClient = httpClient;
        this.request = request;
    }

    public static Long getAllCount() {
        return counterAll.get();
    }

    public static Long getSuccessCount() {
        return counterSuccess.get();
    }

    public static Long getFailCount() {
        return counterFail.get();
    }

    @Override
    public void run() {
        counterAll.incrementAndGet();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                counterFail.incrementAndGet();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.body() != null){
                    response.body().close();
                }
                counterSuccess.incrementAndGet();
            }
        });
    }
}

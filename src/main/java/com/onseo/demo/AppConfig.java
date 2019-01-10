package com.onseo.demo;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class AppConfig {

    @Value("${countThreads}")
    private int countThreads;
    @Value("${url}")
    private String url;

    @Bean
    public ScheduledExecutorService scheduledExecutorService(){
        return Executors.newScheduledThreadPool(countThreads > 0 ? countThreads : Runtime.getRuntime().availableProcessors() + 1);
    }


    @Bean
    public OkHttpClient httpClient(){
        return new OkHttpClient();
    }

    @Bean
    public Request request(){
        return new Request.Builder()
                .get()
                .url(url)
                .build();
    }
}

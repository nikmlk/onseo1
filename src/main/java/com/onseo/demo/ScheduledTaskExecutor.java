package com.onseo.demo;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Component
@Slf4j
public class ScheduledTaskExecutor {
    private final ScheduledExecutorService scheduler;
    private final int countRequests;
    private final int period;
    private final OkHttpClient httpClient;
    private final Request request;

    public ScheduledTaskExecutor(ScheduledExecutorService scheduler, @Value("${countRequests}") int countRequests, @Value("${period}") int period, OkHttpClient httpClient, Request request) {
        this.scheduler = scheduler;
        this.countRequests = countRequests;
        this.period = period;
        this.httpClient = httpClient;
        this.request = request;
    }

    @PostConstruct
    private void init() {
        Stream.generate(() -> new Task(httpClient, request))
                .limit(countRequests)
                .forEach(task -> scheduler.scheduleAtFixedRate(task, 1, period, TimeUnit.SECONDS));

        printResult();
    }

    private void printResult() {
        scheduler.scheduleAtFixedRate(() -> {
            log.info("");
            log.info(Task.getAllCount() + " requests sent");
            log.info(Task.getSuccessCount() + " requests success");
            log.info(Task.getFailCount() + " requests failed");
        }, 1, period, TimeUnit.SECONDS);
    }
}

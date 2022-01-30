package com.example.app_sample.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static volatile AppExecutors sInstance;

    private final Executor diskIO;

    public AppExecutors(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (AppExecutors.class) {
                if (sInstance == null) {
                    sInstance = new AppExecutors(Executors.newSingleThreadExecutor());
                }
            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }



}

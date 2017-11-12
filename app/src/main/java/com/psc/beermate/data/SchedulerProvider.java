package com.psc.beermate.data;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SchedulerProvider {
    public Scheduler getIoScheduler() {
        return Schedulers.io();
    }

    public Scheduler getMainScheduler() {
        return AndroidSchedulers.mainThread();
    }
}

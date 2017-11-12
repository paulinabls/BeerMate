package com.psc.beermate.data;

import com.psc.beermate.data.mapper.BeerMapper;
import com.psc.beermate.data.network.PunkApi;
import com.psc.beermate.domain.Repository;
import com.psc.beermate.domain.model.BeerInfo;

import java.util.List;

import io.reactivex.Observable;

public class PunkRepository implements Repository {
    private final BeerMapper beerMapper;
    private final SchedulerProvider schedulerProvider;
    private PunkApi api;

    public PunkRepository(final PunkApi api, final BeerMapper beerMapper, final SchedulerProvider
            schedulerProvider) {
        this.api = api;
        this.beerMapper = beerMapper;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public Observable<List<BeerInfo>> getBeers(final int pageSize, final int page) {
        return api.getBeers(pageSize, page)
                  .map(beerMapper::map)
                  .subscribeOn(schedulerProvider.getIoScheduler())
                  .observeOn(schedulerProvider.getMainScheduler());
    }
}

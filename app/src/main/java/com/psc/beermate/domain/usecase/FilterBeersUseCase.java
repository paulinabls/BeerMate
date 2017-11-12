package com.psc.beermate.domain.usecase;

import com.psc.beermate.domain.Repository;
import com.psc.beermate.domain.model.BeerInfo;

import java.util.List;

import io.reactivex.Observable;

public class FilterBeersUseCase implements UseCase<List<BeerInfo>, Observable<List<BeerInfo>>> {
    private final Repository repository;
    private static final int PAGE_SIZE = 50;
    private static final int PAGE_NUMBER = 1;

    public FilterBeersUseCase(final Repository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<BeerInfo>> execute(List<BeerInfo> list) {
        return repository.getBeers(PAGE_SIZE, PAGE_NUMBER);
    }


}

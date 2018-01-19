package com.psc.beermate.domain.usecase;

import com.psc.beermate.domain.Repository;
import com.psc.beermate.domain.model.BeerInfo;

import java.util.List;

import io.reactivex.Observable;

public class FetchBeersUseCase implements UseCase<Void, Observable<List<BeerInfo>>> {
    private final Repository repository;
    private static final int PAGE_SIZE = 50;
    private static final int PAGE_NUMBER = 1;

    public FetchBeersUseCase(final Repository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<BeerInfo>> execute(Void v) {
        return repository.getBeers(PAGE_SIZE, PAGE_NUMBER);
    }


}

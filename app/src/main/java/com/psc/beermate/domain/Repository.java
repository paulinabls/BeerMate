package com.psc.beermate.domain;

import com.psc.beermate.domain.model.BeerInfo;

import java.util.List;

import io.reactivex.Observable;
public interface Repository {
    Observable<List<BeerInfo>> getBeers(int pageSize, int page);

}

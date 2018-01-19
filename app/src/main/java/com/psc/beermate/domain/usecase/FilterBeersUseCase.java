package com.psc.beermate.domain.usecase;

import android.support.annotation.NonNull;

import com.psc.beermate.data.SchedulerProvider;
import com.psc.beermate.domain.model.BeerInfo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Predicate;

public class FilterBeersUseCase implements UseCase<FilterBeersUseCase.Param,Single<List<BeerInfo>>> {

    public FilterBeersUseCase() {
    }

    public static class Param {
        private final Observable<List<BeerInfo>> list;
        private final CharSequence query;

        public Param(Observable<List<BeerInfo>> listObservable, CharSequence query) {
            this.list = listObservable;
            this.query = query;
        }
    }

    @Override
    public Single<List<BeerInfo>> execute(Param param) {
        return param.list
                .flatMapIterable(it -> it)
                .filter(byName(param.query))
                .toList();
    }

    @NonNull
    private Predicate<BeerInfo> byName(CharSequence query) {
        return beer -> beer.getName().toLowerCase().contains(query.toString().toLowerCase());
    }

}

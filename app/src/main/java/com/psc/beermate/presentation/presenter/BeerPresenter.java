package com.psc.beermate.presentation.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.psc.beermate.data.SchedulerProvider;
import com.psc.beermate.domain.model.BeerInfo;
import com.psc.beermate.domain.usecase.FetchBeersUseCase;
import com.psc.beermate.domain.usecase.FilterBeersUseCase;
import com.psc.beermate.domain.usecase.FilterBeersUseCase.Param;
import com.psc.beermate.presentation.presenter.base.Presenter;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

public class BeerPresenter implements Presenter<BeersView> {
    private static final int QUERY_CHANGE_TIMEOUT_MILLIS = 350;

    private BeersView view;
    private final FetchBeersUseCase fetchBeersUseCase;
    private final FilterBeersUseCase filterBeersUseCase;
    private final SchedulerProvider schedulerProvider;
    private Disposable fetchSubscription;
    private Disposable filterSubscription;
    private List<BeerInfo> list = Collections.emptyList();
    private BehaviorSubject<CharSequence> observableQuery;

    public BeerPresenter(FetchBeersUseCase fetchBeersUseCase, FilterBeersUseCase filterBeersUseCase, SchedulerProvider schedulerProvider) {
        this.fetchBeersUseCase = fetchBeersUseCase;
        this.filterBeersUseCase = filterBeersUseCase;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public void onViewAttached(final BeersView view) {
        this.view = view;
        if (list.isEmpty()) {
            fetchBeers();
        }
        else if (observableQuery == null) {
            view.setData(list);
        }
    }

    @Override
    public void onViewDetached() {
        this.view = null;
        disposeSubscription(filterSubscription);
        filterSubscription = null;
    }

    @Override
    public void onDestroyed() {
        disposeSubscription(fetchSubscription);
        fetchSubscription = null;
    }

    private void disposeSubscription(Disposable subscription) {
        if (subscription != null) {
            subscription.dispose();
        }
    }

    @VisibleForTesting
    protected void onBeersReceived(final List<BeerInfo> receivedBeers) {
        disposeSubscription(fetchSubscription);
        if (receivedBeers.isEmpty()) {
            onError("No beers");
            return;
        }
        list = receivedBeers;

        setObservableQuery(observableQuery);

        setListAndHideSpinner(receivedBeers);
    }

    @NonNull
    private void fetchBeers() {
        fetchSubscription = fetchBeersUseCase
                .execute(null)
                .subscribe(this::onBeersReceived, e -> onError(e.getMessage()));
    }

    private void onError(final String message) {
        if (view == null) {
            return;
        }
        view.hideLoadingSpinner();
        view.displayErrorMessage(message);
    }

    public void setObservableQuery(@Nullable BehaviorSubject<CharSequence> observableQuery) {
        this.observableQuery = observableQuery;

        disposeSubscription(filterSubscription);
        subscribeToFiltering(observableQuery);
    }

    private void subscribeToFiltering(BehaviorSubject<CharSequence> observableQuery) {
        if (observableQuery == null) {
            return;
        }

        Observable<List<BeerInfo>> listObservable = Observable.just(list);
        filterSubscription = observableQuery.debounce(QUERY_CHANGE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .observeOn(schedulerProvider.getMainScheduler())
                .doOnNext(it -> view.showLoadingSpinner())
                .map(query -> filterBeersUseCase.execute(new Param(listObservable, query)))
                .doOnNext(listSingle -> onListFiltered(listSingle.blockingGet()))
                .subscribe();

    }

    private void onListFiltered(List<BeerInfo> filteredList) {
        setListAndHideSpinner(filteredList);
    }

    private void setListAndHideSpinner(List<BeerInfo> filteredList) {
        if (view == null) {
            return;
        }
        view.setData(filteredList);
        view.hideLoadingSpinner();
    }

    public void onItemClicked(BeerInfo item) {
        if (item != null && item.getImageUrl() != null) {
            view.showImage(item.getImageUrl());
        }
    }
}

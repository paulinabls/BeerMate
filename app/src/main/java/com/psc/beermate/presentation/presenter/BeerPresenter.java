package com.psc.beermate.presentation.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.psc.beermate.domain.model.BeerInfo;
import com.psc.beermate.domain.usecase.FetchBeersUseCase;
import com.psc.beermate.domain.usecase.FilterBeersUseCase;
import com.psc.beermate.domain.usecase.FilterBeersUseCase.Param;
import com.psc.beermate.presentation.presenter.base.Presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

public class BeerPresenter implements Presenter<BeersView> {
    public static final int QUERY_CHANGE_TIMEOUT_MILLIS = 350;
    private final FetchBeersUseCase fetchBeersUseCase;
    private final FilterBeersUseCase filterBeersUseCase;
    private BeersView view;
    private List<BeerInfo> list = new ArrayList<>();
    private Disposable subscription;
    private BehaviorSubject<CharSequence> observableQuery;
    private Disposable filterSubscribtion;
    private Observable<List<BeerInfo>> observableBeers;

    public BeerPresenter(FetchBeersUseCase fetchBeersUseCase, FilterBeersUseCase filterBeersUseCase) {
        this.fetchBeersUseCase = fetchBeersUseCase;
        this.filterBeersUseCase = filterBeersUseCase;
    }

    @Override
    public void onViewAttached(final BeersView view) {
        this.view = view;
        if (list.isEmpty()) {
            getBeers();
        }
    }

    @Override
    public void onViewDetached() {
        this.view = null;
        disposeSubscription(filterSubscribtion);
        filterSubscribtion = null;
    }

    @Override
    public void onDestroyed() {
        disposeSubscription(subscription);
        subscription = null;
    }

    private void disposeSubscription(Disposable subscription) {
        if (subscription != null) {
            subscription.dispose();
        }
    }

    @VisibleForTesting
    protected void onBeersReceived(final List<BeerInfo> receivedBeers) {
        disposeSubscription(subscription);
        if (receivedBeers.isEmpty()) {
            onError("No beers");
            return;
        }
        this.list.addAll(receivedBeers);

        if (view == null) {
            return;
        }
        view.setData(receivedBeers);
        view.hideLoadingSpinner();
    }

    @NonNull
    private Disposable getBeers() {
        observableBeers = fetchBeersUseCase.execute(null);
        return observableBeers.subscribe(this::onBeersReceived, e -> onError(e.getMessage()));
    }

    private void onError(final String message) {
        if (view == null) {
            return;
        }
        view.hideLoadingSpinner();
        view.displayErrorMessage(message);
    }

    public void setObservableQuery(BehaviorSubject<CharSequence> observableQuery) {
        this.observableQuery = observableQuery;
        subscribeToFiltering();
    }

    private void subscribeToFiltering() {
        filterSubscribtion = observableQuery.debounce(QUERY_CHANGE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(it -> view.showLoadingSpinner())
                .subscribe(query -> filterBeersUseCase.execute(new Param(observableBeers, query))
                        .subscribe(this::onListFiltered, e -> onError(e.getMessage()))
                );
    }


    private void onListFiltered(List<BeerInfo> filteredList) {
        if (view == null) {
            return;
        }
        view.setData(filteredList);
        view.hideLoadingSpinner();
    }

}

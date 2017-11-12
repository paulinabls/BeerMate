package com.psc.beermate.presentation.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.psc.beermate.domain.model.BeerInfo;
import com.psc.beermate.domain.usecase.GetBeersUseCase;
import com.psc.beermate.presentation.presenter.base.Presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

public class BeerPresenter implements Presenter<BeersView> {
    private final GetBeersUseCase getBeersUseCase;
    private BeersView view;
    private List<BeerInfo> list = new ArrayList<>();
    private Disposable subscription;
    private BehaviorSubject<CharSequence> filter;
    private Disposable filterSubscribtion;

    public BeerPresenter(GetBeersUseCase getBeersUseCase) {
        this.getBeersUseCase = getBeersUseCase;
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
        return getBeersUseCase
                .execute(null)
                .subscribe(this::onBeersReceived, error -> onError(error.getMessage()));
    }

    private void onError(final String message) {
        if (view == null) {
            return;
        }
        view.hideLoadingSpinner();
        view.displayErrorMessage(message);
    }

    public void setFilter(BehaviorSubject<CharSequence> filter) {
        this.filter = filter;
        subscribeToFiltering();
    }

    private void subscribeToFiltering() {
        filterSubscribtion = filter.debounce(350, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(it -> view.showLoadingSpinner())
                .subscribe(query -> {
                    Observable.fromIterable(list)
                            .filter(byName(query))
                            .toList()
                            .subscribe(
                                    this::onListFiltered, error -> onError(error.getMessage())
                            );
                });
    }

    @NonNull
    private Predicate<BeerInfo> byName(CharSequence query) {
        return beer -> beer.getName().toLowerCase().contains(query.toString().toLowerCase());
    }

    private void onListFiltered(List<BeerInfo> filteredList) {
        if (view == null) {
            return;
        }
        view.setData(filteredList);
        view.hideLoadingSpinner();
    }

}

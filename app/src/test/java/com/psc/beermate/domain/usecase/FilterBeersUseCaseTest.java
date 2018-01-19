package com.psc.beermate.domain.usecase;

import android.support.annotation.NonNull;

import com.psc.beermate.data.SchedulerProvider;
import com.psc.beermate.domain.model.BeerInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FilterBeersUseCaseTest {

    FilterBeersUseCase tested = new FilterBeersUseCase();
    private static final BeerInfo beer1 = createBeerInfo("pale ale");
    private static final BeerInfo beer2 = createBeerInfo("Porter");
    private static final BeerInfo beer3 = createBeerInfo("lager");
    private final static List<BeerInfo> BEER_INFOS = Arrays.asList(beer1, beer2, beer3);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        tested = new FilterBeersUseCase();
    }

    @Test
    public void execute_forEmptyQuery_shouldReturnUnchangedList() {
        Observable<List<BeerInfo>> observableList = Observable.just(BEER_INFOS);
        FilterBeersUseCase.Param param = new FilterBeersUseCase.Param(observableList, "");

        TestObserver<List<BeerInfo>> testSubscriber = new TestObserver<>();
        tested.execute(param).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(list -> list.size() == 3);
    }

    @Test
    public void execute_forOneLetterQuery_shouldReturnItemsContainingTheLetter_withIgnoreCase() {
        Observable<List<BeerInfo>> observableList = Observable.just(BEER_INFOS);
        String query = "p";
        FilterBeersUseCase.Param param = new FilterBeersUseCase.Param(observableList, query);

        TestObserver<List<BeerInfo>> testSubscriber = new TestObserver<>();
        tested.execute(param).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(list -> list.size() == 2);
        testSubscriber.assertValue(list -> list.get(0).equals(beer1));
        testSubscriber.assertValue(list -> list.get(1).equals(beer2));
    }

    @Test
    public void execute_forQuery_shouldReturnItemsContainingThatQuery_withIgnoreCase() {
        Observable<List<BeerInfo>> observableList = Observable.just(BEER_INFOS);
        String query = "LAGER";
        FilterBeersUseCase.Param param = new FilterBeersUseCase.Param(observableList, query);

        TestObserver<List<BeerInfo>> testSubscriber = new TestObserver<>();
        tested.execute(param).subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(list -> list.size() == 1);
        testSubscriber.assertValue(list -> list.get(0).equals(beer3));
    }

    @NonNull
    public static BeerInfo createBeerInfo(String name) {
        return new BeerInfo(11, name, "description", "imageUrl");
    }
}
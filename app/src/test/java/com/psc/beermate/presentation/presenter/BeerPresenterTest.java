package com.psc.beermate.presentation.presenter;

import com.psc.beermate.data.SchedulerProvider;
import com.psc.beermate.domain.model.BeerInfo;
import com.psc.beermate.domain.usecase.FetchBeersUseCase;
import com.psc.beermate.domain.usecase.FilterBeersUseCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationWithTimeout;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

import static com.psc.beermate.domain.usecase.FilterBeersUseCaseTest.createBeerInfo;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class BeerPresenterTest {

    private static final BeerInfo beer1 = createBeerInfo("pale ale");
    private static final BeerInfo beer2 = createBeerInfo("Porter");
    private static final BeerInfo beer3 = createBeerInfo("lager");
    private final static List<BeerInfo> BEER_INFOS = Arrays.asList(beer1, beer2, beer3);
    private static final Observable<List<BeerInfo>> OBSERVABLE_LIST = Observable.just(BEER_INFOS);

    @Mock
    private FetchBeersUseCase fetchBeersUseCase;
    @Mock
    private BeersView view;

    private BeerPresenter tested;
    private BehaviorSubject<CharSequence> queryObservable = BehaviorSubject.createDefault("");
    private final VerificationWithTimeout timeoutDebounce = timeout(400);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        SchedulerProvider schedulerProvider = mock(SchedulerProvider.class);
        tested = new BeerPresenter(fetchBeersUseCase, new FilterBeersUseCase(), schedulerProvider);
        when(fetchBeersUseCase.execute(null)).thenReturn(OBSERVABLE_LIST);
        when(schedulerProvider.getMainScheduler()).thenReturn(Schedulers.trampoline());
        when(schedulerProvider.getIoScheduler()).thenReturn(Schedulers.trampoline());
    }

    @Test
    public void setObservableQuery_calledAfterViewReattached_shouldSetData() throws Exception {
        tested.onViewAttached(view);
        tested.onViewDetached();
        clearInvocations(view);

        tested.onViewAttached(view);
        tested.setObservableQuery(queryObservable);
        queryObservable.onNext("");

        verify(view, timeoutDebounce).setData(BEER_INFOS);
        verify(view, timeoutDebounce).hideLoadingSpinner();
    }

    @Test
    public void onViewAttached_whenViewReattached_shouldSetData() throws Exception {
        tested.onViewAttached(view);
        tested.onViewDetached();
        clearInvocations(view);

        tested.onViewAttached(view);

        verify(view, timeoutDebounce).setData(BEER_INFOS);
    }

    @Test
    public void whenNewQueryPublished_whenViewAttached_shouldSetFilteredData() throws Exception {
        tested.onViewAttached(view);
        tested.setObservableQuery(queryObservable);
        clearInvocations(view);

        queryObservable.onNext("p");

        verify(view, timeoutDebounce).showLoadingSpinner();
        verify(view, timeoutDebounce).setData(Arrays.asList(beer1, beer2));
        verify(view, timeoutDebounce).hideLoadingSpinner();
    }

    @Test
    public void whenNewQueryPublished_andViewDetached_shouldNotInformView() throws Exception {
        tested.onViewAttached(view);
        tested.setObservableQuery(queryObservable);
        clearInvocations(view);

        tested.onViewDetached();
        queryObservable.onNext("p");

        verifyNoMoreInteractions(view);
    }

    @Test
    public void setObservableQuery_whenCalledAfterBeersFetchedAndViewDetached_shouldNotInformView() throws Exception {
        tested.onViewAttached(view);
        clearInvocations(view);

        tested.setObservableQuery(queryObservable);

        verifyNoMoreInteractions(view);
    }

    @Test
    public void onDestroyed_calledRightAfterViewAttached_shouldNotInformView() throws Exception {
        TestObserver<List<BeerInfo>> testSubscriber = OBSERVABLE_LIST.test();
        final Observable<List<BeerInfo>> delayedObservable = OBSERVABLE_LIST.delay(400, TimeUnit.MILLISECONDS);
        when(fetchBeersUseCase.execute(null)).thenReturn(delayedObservable);
        tested.onViewAttached(view);
        clearInvocations(view);

        tested.onDestroyed();

        Thread.sleep(300);
        assertTrue(testSubscriber.isDisposed());
        verifyNoMoreInteractions(view);
    }

    @Test
    public void onViewAttached_whenNotEmptyListFetched_informsView() throws Exception {
        tested.onViewAttached(view);

        verify(view).setData(BEER_INFOS);
        verify(view).hideLoadingSpinner();
    }

    @Test
    public void onViewAttached_whenEmptyListFetched_shouldShowError_andHideLoadingSpinner() throws Exception {
        when(fetchBeersUseCase.execute(null)).thenReturn(Observable.just(Collections.emptyList()));
        tested.onViewAttached(view);

        verify(view).hideLoadingSpinner();
        verify(view).displayErrorMessage(anyString());
    }

    @Test
    public void onBeersReceived_whenEmptyList_andWhenViewNotAttached_shouldNotInformView() throws Exception {
        tested.onBeersReceived(Collections.emptyList());

        verify(view, never()).hideLoadingSpinner();
        verify(view, never()).displayErrorMessage(anyString());
    }

    @Test
    public void onBeersReceived_whenNotEmptyList_andWhenViewNotAttached_shouldNotInformView() throws Exception {
        tested.onBeersReceived(BEER_INFOS);

        verify(view, never()).setData(BEER_INFOS);
        verify(view, never()).hideLoadingSpinner();
    }

}
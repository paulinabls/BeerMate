package com.psc.beermate.presentation.presenter;

import com.psc.beermate.domain.model.BeerInfo;
import com.psc.beermate.domain.usecase.GetBeersUseCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;

import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BeerPresenterTest {

    private static final List<BeerInfo> BEER_INFOS = Arrays.asList(getMockedBeerInfo(), getMockedBeerInfo());
    private static final Observable<List<BeerInfo>> OBSERVABLE_LIST = Observable.just(BEER_INFOS);
    @Mock
    private GetBeersUseCase getBeersUseCase;
    @Mock
    private BeersView view;
    private BeerPresenter tested;

    private static BeerInfo getMockedBeerInfo() {
        return mock(BeerInfo.class);
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        tested = new BeerPresenter(getBeersUseCase);
    }

    @Test
    public void onViewAttached_calledAfterBeersReceivedAndViewDetached_shouldSetData() throws Exception {
        tested.onBeersReceived(BEER_INFOS);
        tested.onViewDetached();

        tested.onViewAttached(view);
//        tested.setFilter(setFilter);
        verify(view).setData(BEER_INFOS);
    }

    @Test
    public void onViewDetached() throws Exception {
    }

//    @Test
//    public void onDestroyed_calledRightAfterSearchClicked_should_haveNoInteractionsOnView() throws Exception {
//        tested.onViewAttached(view);
//        TestObserver<List<BeerInfo>> testSubscriber = OBSERVABLE_LIST.test();
//        final Observable<List<BeerInfo>> delayedObservable = OBSERVABLE_LIST.delay(300, TimeUnit.MILLISECONDS);
//        when(getBeersUseCase.execute(null)).thenReturn(delayedObservable);
//
//        tested.onSearchButtonClicked("");
//        clearInvocations(view);
//
//        tested.onDestroyed();
//
//        Thread.sleep(300);
//        assertTrue(testSubscriber.isDisposed());
//        verifyNoMoreInteractions(view);
//    }

    private void arrangeMocksForSearch() {
        tested.onViewAttached(view);
        when(getBeersUseCase.execute(null)).thenReturn(OBSERVABLE_LIST);
    }

    @Test
    public void onBeersReceived_whenNotEmptyList_andWhenViewAttached_informsView() throws Exception {
        tested.onViewAttached(view);

        tested.onBeersReceived(BEER_INFOS);

        verify(view).setData(BEER_INFOS);
        verify(view).hideLoadingSpinner();
    }

    @Test
    public void onBeersReceived_whenEmptyList_andWhenViewAttached_should_displayErrorMessage_and_hideLoadingSpinner
            () throws Exception {
        tested.onViewAttached(view);

        tested.onBeersReceived(Collections.emptyList());

        verify(view).hideLoadingSpinner();
        verify(view).displayErrorMessage(anyString());
    }

    @Test
    public void onBeersReceived_whenEmptyList_should_setNoResultReceived() throws Exception {
        tested.onBeersReceived(Collections.emptyList());

//        verify(searchDataHolder).setNoResultReceived();
    }

    @Test
    public void onBeersReceived_whenEmptyList_andWhenViewNotAttached_should_notCallViewMethods() throws Exception {
        tested.onBeersReceived(Collections.emptyList());

        verify(view, never()).hideLoadingSpinner();
        verify(view, never()).displayErrorMessage(anyString());
    }

    @Test
    public void onBeersReceived_whenNotEmptyList_andWhenViewNotAttached_doesntInformView() throws Exception {
        tested.onBeersReceived(BEER_INFOS);

        verify(view, never()).setData(BEER_INFOS);
        verify(view, never()).hideLoadingSpinner();
    }


}
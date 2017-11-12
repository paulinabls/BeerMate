package com.psc.beermate.data;

import com.psc.beermate.data.mapper.BeerMapper;
import com.psc.beermate.data.model.Beer;
import com.psc.beermate.data.network.PunkApi;
import com.psc.beermate.domain.model.BeerInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PunkRepositoryTest {
    @Mock
    PunkApi api;
    @Mock
    BeerMapper beerMapper;
    @Mock
    SchedulerProvider schedulerProvider;

    private PunkRepository tested;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        tested = new PunkRepository(api, beerMapper, schedulerProvider);
    }

    @Test
    public void getBeers_WhenSubscribed() throws Exception {
        final int pageSize = 6;
        final int page = 5;

        Beer beer = new Beer(2, "name", "DEscription", "imgUrl", "firstBrewed");
        Observable<List<Beer>> observable = Observable.just(Arrays.asList(beer));
        when(api.getBeers(pageSize, page)).thenReturn(observable);
        when(schedulerProvider.getIoScheduler()).thenReturn(Schedulers.trampoline());
        when(schedulerProvider.getMainScheduler()).thenReturn(Schedulers.trampoline());

        final Observable<List<BeerInfo>> listObservable = tested.getBeers(pageSize, page);
        listObservable.subscribe();

        verify(api).getBeers(pageSize, page);
        verify(beerMapper).map(any());
        verify(schedulerProvider).getIoScheduler();
        verify(schedulerProvider).getMainScheduler();
    }

}
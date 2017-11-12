package com.psc.beermate.presentation.view;

import android.content.Context;

import com.psc.beermate.data.PunkRepository;
import com.psc.beermate.data.SchedulerProvider;
import com.psc.beermate.data.mapper.BeerMapper;
import com.psc.beermate.data.network.PunkApi;
import com.psc.beermate.data.network.HttpClientProvider;
import com.psc.beermate.data.network.ServiceProvider;
import com.psc.beermate.domain.Repository;
import com.psc.beermate.domain.usecase.GetBeersUseCase;
import com.psc.beermate.presentation.presenter.BeerPresenter;
import com.psc.beermate.presentation.presenter.base.PresenterFactory;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

class BeerPresenterFactory implements PresenterFactory<BeerPresenter> {

    @Override
    public BeerPresenter create(Context context) {

        OkHttpClient httpClient = new HttpClientProvider().create(context.getApplicationContext());
        final PunkApi punkService = ServiceProvider.createRestService(httpClient);
        final Repository repository = new PunkRepository(punkService, new BeerMapper(), new SchedulerProvider());

        return new BeerPresenter(new GetBeersUseCase(repository));
    }
}

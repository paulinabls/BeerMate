package com.psc.beermate.data.network;

import com.psc.beermate.data.model.Beer;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PunkApi {

    @GET("v2/beers")
    Observable<List<Beer>> getBeers(@Query("per_page") int perPage,
                                    @Query("page") int page);

}

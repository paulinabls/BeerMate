package com.psc.beermate.data.mapper;

import android.support.annotation.Nullable;

import com.psc.beermate.data.model.Beer;
import com.psc.beermate.domain.model.BeerInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeerMapper {

    public List<BeerInfo> map(@Nullable final List<Beer> beers) {
        if (beers == null) {
            return Collections.emptyList();
        }

        List<BeerInfo> list = new ArrayList<>(beers.size());
        for (Beer beer : beers) {
            list.add(mapFromApi(beer));
        }
        return list;
    }

    private BeerInfo mapFromApi(Beer beer) {
        return new BeerInfo(beer.getId(), beer.getName(), beer.getDescription(), beer.getImageUrl());
    }
}

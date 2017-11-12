package com.psc.beermate.data.mapper;

import com.psc.beermate.data.model.Beer;
import com.psc.beermate.domain.model.BeerInfo;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BeerMapperTest {

    private BeerMapper tested = new BeerMapper();

    @Test
    public void map_whenNullPassed_returnsEmptyList() throws Exception {
        final List<BeerInfo> beerInfos = tested.map(null);
        assertTrue(beerInfos.isEmpty());
    }

    @Test
    public void map_whenBeerListPassed_returnsItAsBeerInfoList() throws Exception {

        Beer beer1 = new Beer(11, "pale ale", "description", "imageUrl", "brewedDate" );
        Beer beer2= new Beer(21, "dark beer", "another description", "second imageUrl", "a brewedDate" );

        final List<Beer> list = Arrays.asList(beer1, beer2);

        final List<BeerInfo> beerInfos = tested.map(list);

        assertEquals(2, beerInfos.size());
        verifyBeerInfoEquals(beer1, beerInfos.get(0));
        verifyBeerInfoEquals(beer2, beerInfos.get(1));
    }

    private void verifyBeerInfoEquals(Beer expectedBeer, BeerInfo beerInfo) {
        assertEquals(expectedBeer.getId(), beerInfo.getId());
        assertEquals(expectedBeer.getName(), beerInfo.getName());
        assertEquals(expectedBeer.getDescription(),beerInfo.getDescription());
        assertEquals(expectedBeer.getImageUrl(), beerInfo.getImageUrl());
    }

}
package com.psc.beermate.presentation.presenter;

import com.psc.beermate.domain.model.BeerInfo;

import java.util.List;

public interface BeersView {
    void setData(List<BeerInfo> list);

    void showLoadingSpinner();

    void hideLoadingSpinner();

    void displayErrorMessage(String message);
}

package com.psc.beermate.presentation.presenter.base;


import android.content.Context;

public interface PresenterFactory<T extends Presenter> {
    T create(Context context);
}

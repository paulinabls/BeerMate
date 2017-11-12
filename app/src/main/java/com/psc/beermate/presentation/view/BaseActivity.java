package com.psc.beermate.presentation.view;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.psc.beermate.presentation.presenter.base.Presenter;
import com.psc.beermate.presentation.presenter.base.PresenterFactory;
import com.psc.beermate.presentation.presenter.base.PresenterLoader;

public abstract class BaseActivity<P extends Presenter<V>, V> extends Activity {

    private P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int i = getLoaderId();
        Loader<P> loader = getLoaderManager().getLoader(i);
        if (loader == null) {
            initLoader();
        } else {
            this.presenter = ((PresenterLoader<P>) loader).getPresenter();
            onPresenterLoaded(presenter);
        }
    }

    private void initLoader() {
        getLoaderManager().initLoader(getLoaderId(), null, new LoaderManager.LoaderCallbacks<P>() {
            @Override
            public final Loader<P> onCreateLoader(int id, Bundle args) {
                return new PresenterLoader<>(BaseActivity.this, getPresenterFactory());
            }

            @Override
            public final void onLoadFinished(Loader<P> loader, P presenter) {
                BaseActivity.this.presenter = presenter;
                onPresenterLoaded(presenter);
            }

            @Override
            public final void onLoaderReset(Loader<P> loader) {
                BaseActivity.this.presenter = null;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached(getPresenterView());
    }

    @Override
    protected void onStop() {
        presenter.onViewDetached();
        super.onStop();
    }

    @NonNull
    protected abstract PresenterFactory<P> getPresenterFactory();

    protected abstract void onPresenterLoaded(@NonNull P presenter);

    @NonNull
    protected V getPresenterView() {
        return (V) this;
    }

    protected abstract int getLoaderId();
}

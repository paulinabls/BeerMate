package com.psc.beermate.presentation.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.psc.beermate.R;
import com.psc.beermate.domain.model.BeerInfo;
import com.psc.beermate.presentation.presenter.BeerPresenter;
import com.psc.beermate.presentation.presenter.BeersView;
import com.psc.beermate.presentation.presenter.base.PresenterFactory;
import com.psc.beermate.presentation.view.adapter.ImageAdapter;
import com.psc.beermate.presentation.view.adapter.RxEditTextAdapter;

import java.util.List;

import io.reactivex.subjects.BehaviorSubject;

public class MainActivity extends BaseActivity<BeerPresenter, BeersView> implements BeersView {

    private static final int LOADER_ID = 0x007;
    private EditText searchEditText;
    private ImageAdapter adapter;
    private BeerPresenter presenter;
    private View loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupRecyclerView();
        setupTopControls();
    }

    private void setupTopControls() {
        loadingSpinner = findViewById(R.id.loading_spinner);
        searchEditText = findViewById(R.id.search_query_edit_text);

    }

    private void setupRecyclerView() {
        adapter = new ImageAdapter();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                layoutManager.getOrientation());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
    }

    @NonNull
    @Override
    protected PresenterFactory<BeerPresenter> getPresenterFactory() {
        return new BeerPresenterFactory();
    }

    @Override
    protected void onPresenterLoaded(@NonNull final BeerPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onStart() {
        super.onStart();
        BehaviorSubject<CharSequence> filterSubject = RxEditTextAdapter.from(searchEditText);
        presenter.setFilter(filterSubject);
    }

    @Override
    protected int getLoaderId() {
        return LOADER_ID;
    }

    public void setData(final List<BeerInfo> list) {
        adapter.setData(list);
    }

    @Override
    public void showLoadingSpinner() {
        loadingSpinner.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingSpinner() {
        loadingSpinner.setVisibility(View.INVISIBLE);
    }

    @Override
    public void displayErrorMessage(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG)
             .show();
    }

}

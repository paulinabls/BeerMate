package com.psc.beermate.presentation.view.adapter;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import io.reactivex.subjects.BehaviorSubject;

public class RxEditTextAdapter {
    public static BehaviorSubject<CharSequence> from(@NonNull final EditText searchEditText) {
        final BehaviorSubject<CharSequence> subject = BehaviorSubject.createDefault(searchEditText.getText().toString());

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                subject.onNext(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //do nothing
                Log.i("ASG", "afterTextChanged " + editable.toString());
            }
        });

        return subject;
    }
}

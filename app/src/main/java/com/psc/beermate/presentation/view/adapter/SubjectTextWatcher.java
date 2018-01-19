package com.psc.beermate.presentation.view.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import io.reactivex.subjects.BehaviorSubject;

public class SubjectTextWatcher implements TextWatcher {
    private final BehaviorSubject<CharSequence> subject;

    public SubjectTextWatcher(BehaviorSubject<CharSequence> subject) {
        this.subject = subject;
    }

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
    }
}

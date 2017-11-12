package com.psc.beermate.domain.usecase;

public interface UseCase<P, T> {

    T execute(P parameter);
}

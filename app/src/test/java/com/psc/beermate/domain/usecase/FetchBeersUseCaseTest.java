package com.psc.beermate.domain.usecase;

import com.psc.beermate.domain.Repository;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FetchBeersUseCaseTest {
    @Test
    public void execute_shouldCallRepositoryWithSpecificParams() throws Exception {
        final Repository repository = mock(Repository.class);
        FetchBeersUseCase tested = new FetchBeersUseCase(repository);

        tested.execute(null);

        final int expectedPageSize = 50;
        final int expectedPageNumber = 1;
        verify(repository).getBeers(expectedPageSize, expectedPageNumber);
    }
}
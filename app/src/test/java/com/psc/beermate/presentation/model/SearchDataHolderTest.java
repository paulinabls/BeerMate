//package com.psc.beermate.presentation.model;
//
//import com.psc.beermate.domain.usecase.GetBeersUseCase.Param;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
//public class SearchDataHolderTest {
//
//    private static final String QUERY = "query";
//    private SearchDataHolder tested;
//
//    @Before
//    public void setUp() throws Exception {
//        tested = new SearchDataHolder();
//    }
//
//    @Test
//    public void setNewQuery_shouldResetNoResultFlag() throws Exception {
//        tested.setNewQuery(QUERY);
//        assertFalse(tested.noResultReceived());
//    }
//
//    @Test(expected = IllegalStateException.class)
//    public void getNextQueryParam_calledWithoutCallTo_setNewQuery_shouldThrowException() throws Exception {
//        tested.getNextQueryParam();
//    }
//
//    @Test
//    public void getNextQueryParam_calledAfter_setNewQuery_shouldReturnParams() throws Exception {
//        tested.setNewQuery(QUERY);
//        final Param param = tested.getNextQueryParam();
//
//        Param expected = new Param(12, 1, QUERY);
//        assertEquals(expected, param);
//    }
//
//    @Test
//    public void getNextQueryParam_calledTwiceAfter_setNewQuery_shouldReturnParams_withDifferentPageNumber() throws
//            Exception {
//        tested.setNewQuery(QUERY);
//
//        final Param param1 = tested.getNextQueryParam();
//        final Param param2 = tested.getNextQueryParam();
//
//        assertEquals(new Param(12, 1, QUERY), param1);
//        assertEquals(new Param(12, 2, QUERY), param2);
//    }
//
//    @Test
//    public void getNextQueryParam_calledAfter_setNewQuery_shouldReturnParams_withNewQuery() throws
//            Exception {
//        final String newQuery = "new query";
//        tested.setNewQuery(QUERY);
//
//        final Param param1 = tested.getNextQueryParam();
//        tested.setNewQuery(newQuery);
//        final Param param2 = tested.getNextQueryParam();
//
//        assertEquals(new Param(12, 1, QUERY), param1);
//        assertEquals(new Param(12, 1, newQuery), param2);
//    }
//
//    @Test
//    public void noResultReceived_whenCalledAfter_SetNoResultReceived_andAfterNewRequest_shouldReturnFalse() throws
//            Exception {
//        tested.setNoResultReceived();
//        tested.setNewQuery(QUERY);
//
//        final boolean noResultReceived = tested.noResultReceived();
//
//        assertFalse(noResultReceived);
//    }
//
//    @Test
//    public void setNoResultReceived_shouldSetNoResultFlag() throws Exception {
//        tested.setNoResultReceived();
//        assertTrue(tested.noResultReceived());
//    }
//
//}
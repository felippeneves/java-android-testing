package com.felippeneves.unit_testing_in_android.example12;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StringRetrieverTest {

    //region constants

    public static final int ID = 10;
    public static final String STRING = "string";

    //endregion constants

    // region helper fields

    @Mock
    Context mContextMock;

    // endregion helper fields

    // region SUT

    StringRetriever SUT;

    // endregion SUT

    // region setup

    @Before
    public void setUp() {
        SUT = new StringRetriever(mContextMock);

    }

    // endregion setup

    // region test methods

    @Test
    public void getString_correctParameterPassedToContext() {
        // Arrange
        // Act
        SUT.getString(ID);
        // Assert
        verify(mContextMock).getString(ID);
    }

    @Test
    public void getString_correctResultReturned() {
        // Arrange
        when(mContextMock.getString(ID)).thenReturn(STRING);
        // Act
        String result = SUT.getString(ID);
        // Assert
        assertEquals(STRING, result);
    }

    // endregion test methods

    // region helper methods

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}

package com.felippeneves.unit_testing_in_android.example13;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AndroidUnitTestingProblemsTest {

    //region constants

    //endregion constants

    // region helper fields

    // endregion helper fields

    // region SUT

    AndroidUnitTestingProblems SUT;

    // endregion SUT

    // region setup

    @Before
    public void setUp() {
        SUT = new AndroidUnitTestingProblems();

    }

    // endregion setup

    // region test methods

    @Test
    public void testStaticApiCall() {
        // Arrange
        // Act
        // this won't pass because TextUtils.isEmpty() is a static method of Android API
        SUT.callStaticAndroidApi("");
        // Assert
        assertTrue(true);
    }


    // endregion test methods

    // region helper methods

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}

package com.felippeneves.unit_testing_in_android.example14;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MyActivityTest {

    //region constants

    //endregion constants

    // region helper fields

    // endregion helper fields

    // region SUT

    MyActivity SUT;

    // endregion SUT

    // region setup

    @Before
    public void setUp() {
        SUT = new MyActivity();

    }

    // endregion setup

    // region test methods

    @Test
    public void onStart_incrementsCountByOne() {
        // Arrange
        // Act
        // this won't pass because MyActivity extends Activity and it's not possible to mock
        SUT.onStart();
        int result = SUT.getCount();
        // Assert
        assertEquals(1, result);
    }


    // endregion test methods

    // region helper methods

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}

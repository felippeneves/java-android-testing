package com.felippeneves.test_doubles_fundamentals.example5;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UserInputValidatorTest {

    UserInputValidator SUT;

    @Before
    public void setup() throws Exception {
        SUT = new UserInputValidator();
    }

    @Test
    public void isValidFullName_validFullName_trueReturned() {
        boolean result = SUT.isValidFullName("validFullName");
        assertTrue(result);
    }

    @Test
    public void isValidFullName_invalidFullName_falseReturned() {
        boolean result = SUT.isValidFullName("");
        assertFalse(result);
    }

    @Test
    public void isValidUsername_validUsername_trueReturned() {
        // The method has a static method call, so we can't mock it
        boolean result = SUT.isValidUsername("validUsername");
        assertTrue(result);
    }

    @Test
    public void isValidUsername_invalidUsername_falseReturned() {
        // The method has a static method call, so we can't mock it
        boolean result = SUT.isValidUsername("");
        assertFalse(result);
    }
}

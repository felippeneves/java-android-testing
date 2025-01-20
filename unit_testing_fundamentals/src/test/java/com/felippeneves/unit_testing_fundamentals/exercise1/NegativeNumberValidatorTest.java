package com.felippeneves.unit_testing_fundamentals.exercise1;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class NegativeNumberValidatorTest {

    NegativeNumberValidator SUT;

    @Before
    public void setup() {
        SUT = new NegativeNumberValidator();
    }

    @Test
    public void testNegative() {
        boolean result = SUT.isNegative(-1);
        assertTrue(result);
    }

    @Test
    public void testZero() {
        boolean result = SUT.isNegative(0);
        assertTrue(result);
    }

    @Test
    public void testPositive() {
        boolean result = SUT.isNegative(1);
        assertFalse(result);
    }
}
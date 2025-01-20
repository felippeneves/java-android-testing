package com.felippeneves.unit_testing_fundamentals.example1;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class PositiveNumberValidatorTest {

    PositiveNumberValidator SUT;

    @Before
    public void setup() {
        SUT = new PositiveNumberValidator();
    }

    @Test
    public void testNegative() {
        boolean result = SUT.isPositive(-1);
        assertFalse(result);
    }

    @Test
    public void testZero() {
        boolean result = SUT.isPositive(0);
        assertFalse(result);
    }

    @Test
    public void testPositive() {
        boolean result = SUT.isPositive(1);
        assertTrue(result);
    }
}

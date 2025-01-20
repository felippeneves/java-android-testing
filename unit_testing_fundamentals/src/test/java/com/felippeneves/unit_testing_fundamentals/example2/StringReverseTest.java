package com.felippeneves.unit_testing_fundamentals.example2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class StringReverseTest {
    
    StringReverse SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new StringReverse();
    }

    @Test
    public void reverse_emptyString_emptyStringReturned() throws Exception {
        String result = SUT.reverse("");
        assertEquals("", result);
    }

    @Test
    public void reverse_singleCharacter_sameStringReturned() throws Exception {
        String result = SUT.reverse("a");
        assertEquals("a", result);
    }

    @Test
    public void reverse_longString_reversedStringReturned() throws Exception {
        String result = SUT.reverse("felippe neves");
        assertEquals("seven eppilef", result);
    }
}
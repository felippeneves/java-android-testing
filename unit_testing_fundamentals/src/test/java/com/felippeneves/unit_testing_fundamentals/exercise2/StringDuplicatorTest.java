package com.felippeneves.unit_testing_fundamentals.exercise2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class StringDuplicatorTest {
    
    StringDuplicator SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new StringDuplicator();
    }

    @Test
    public void duplicate_emptyString_emptyStringReturned() {
        String result = SUT.duplicate("");
        assertEquals("", result);
    }

    @Test
    public void duplicate_singleCharacter_duplicatedStringReturned() {
        String result = SUT.duplicate("a");
        assertEquals("aa", result);
    }

    @Test
    public void duplicate_longString_duplicatedStringReturned() {
        String result = SUT.duplicate("felippe neves");
        assertEquals("felippe nevesfelippe neves", result);
    }
}
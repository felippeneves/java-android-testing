package com.felippeneves.test_doubles_fundamentals.example6;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class FitnessTrackerTest {

    FitnessTracker SUT;

    @Before
    public void setup() {
        SUT = new FitnessTracker();
    }

    @Test
    public void step_totalIncremented() {
        SUT.step();
        // The method has a static object, so we can't mock it
        assertEquals(1, SUT.getTotalSteps());
    }

    @Test
    public void runStep_totalIncrementedByCorrectRatio() {
        SUT.runStep();
        assertEquals(2, SUT.getTotalSteps());
    }
}
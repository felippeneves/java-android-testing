package com.felippeneves.unit_testing_fundamentals.exercise3;

import com.felippeneves.unit_testing_fundamentals.example3.Interval;

public class IntervalsAdjacencyDetector {

    public boolean isAdjacent(Interval interval1, Interval interval2) {
        return interval1.getEnd() == interval2.getStart() || interval1.getStart() == interval2.getEnd()
                || isSameIntervals(interval1, interval2);
    }

    private boolean isSameIntervals(Interval interval1, Interval interval2) {
        return interval1.getStart() == interval2.getEnd() && interval1.getEnd() == interval2.getStart();
    }
}

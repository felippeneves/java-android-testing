package com.felippeneves.test_doubles_fundamentals.example5;

public class FullNameValidator {

    public static boolean isValidFullName(String fullName) {
        // trivially simple task
        return !fullName.isEmpty();
    }
}

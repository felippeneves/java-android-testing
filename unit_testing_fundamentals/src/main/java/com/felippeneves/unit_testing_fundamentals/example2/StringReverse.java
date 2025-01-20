package com.felippeneves.unit_testing_fundamentals.example2;

public class StringReverse {

    public String reverse(String string) {
        if (string == null || string.isEmpty())
            return "";

        StringBuilder stringReverse = new StringBuilder();

        for (int i = string.length() - 1; i >= 0; i--) {
            stringReverse.append(string.charAt(i));
        }

        return stringReverse.toString();
    }
}

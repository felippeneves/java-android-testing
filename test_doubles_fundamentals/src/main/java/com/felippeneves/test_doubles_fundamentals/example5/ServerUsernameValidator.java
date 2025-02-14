package com.felippeneves.test_doubles_fundamentals.example5;

public class ServerUsernameValidator {

    public static boolean isValidUsername(String username) {
        // this sleep mimics network request that checks whether username is free, but fails due to
        // absence of network connection
        try {
            Thread.sleep(1000);
            throw new RuntimeException("no network connection");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

}

package com.felippeneves.mockito_fundamentals.example7.authtoken;

public interface AuthTokenCache {

    void cacheAuthToken(String authToken);

    String getAuthToken();
}

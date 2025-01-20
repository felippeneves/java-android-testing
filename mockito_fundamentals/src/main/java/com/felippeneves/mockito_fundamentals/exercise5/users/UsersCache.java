package com.felippeneves.mockito_fundamentals.exercise5.users;

public interface UsersCache {

    void cacheUser(User user);

    User getUser(String userId);
}

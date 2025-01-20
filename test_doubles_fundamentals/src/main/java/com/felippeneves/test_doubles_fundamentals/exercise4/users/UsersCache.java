package com.felippeneves.test_doubles_fundamentals.exercise4.users;

import androidx.annotation.Nullable;

public interface UsersCache {

    void cacheUser(User user);

    @Nullable
    User getUser(String userId);
}

package com.felippeneves.test_driven_development.exercise6.users;

import androidx.annotation.Nullable;

public interface UsersCache {

    void cacheUser(User user);

    @Nullable
    User getUser(String userId);
}

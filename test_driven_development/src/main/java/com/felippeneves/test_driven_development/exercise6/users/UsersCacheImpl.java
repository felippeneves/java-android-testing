package com.felippeneves.test_driven_development.exercise6.users;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UsersCacheImpl implements UsersCache {

    private List<User> userList;

    @Override
    public void cacheUser(User user) {
        if (userList == null) {
            userList = new ArrayList<>();
        }

        userList.add(user);
    }

    @Nullable
    @Override
    public User getUser(String userId) {
        if (userList != null) {
            for (User user : userList) {
                if (user.getUserId().equals(userId)) {
                    return user;
                }
            }
        }

        return null;
    }
}

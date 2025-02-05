package com.socialmeli.socialmeli.utils;

import com.socialmeli.socialmeli.models.User;

public class UserTestUtils {

    private Integer maxId = 1;

    public User createSeller(String name) {
        return new User(maxId++, name, true);
    }

    public User createSeller() {
        return new User(maxId++, null, true);
    }

    public User createBuyer(String name) {
        return new User(maxId++, name, false);
    }

    public User createBuyer() {
        return new User(maxId++, null, false);
    }

    public User createSeller(Integer id, String name) {
        return new User(id, name, true);
    }

    public User createBuyer(Integer id, String name) {
        return new User(id, name, true);
    }
}

package com.socialmeli.socialmeli.utils;

import com.socialmeli.socialmeli.models.User;

import java.util.List;

public class UserFactory {

    public static User createSeller(Integer id) {
        return new User(id, null, true);
    }

    public static User createBuyer(Integer id) {
        return new User(id, null, false);
    }

    public static User createSeller(Integer id, String name) {
        return new User(id, name, true);
    }

    public static User createBuyer(Integer id, String name) {
        return new User(id, name, false);
    }

    public static List<User> getListOfSellers() {
        return List.of(createSeller(1), createSeller(2));
    }
}

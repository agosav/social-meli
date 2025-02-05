package com.socialmeli.socialmeli.repositories;

import com.socialmeli.socialmeli.models.Follow;
import com.socialmeli.socialmeli.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FollowRepositoryTest {

    private FollowRepository followRepository;

    @BeforeEach
    void setUp() throws IOException {
        followRepository = new FollowRepository();
        followRepository.loadDataBase();
    }

    @Test
    @DisplayName("findAllByIdFollowerTest - should return a list of follows by id follower")
    void findAllByIdFollowerTest() {
        // Arrange
        User user1 = new User(1, "Agostina Avalle", true);
        List<Follow> expected = List.of(
                new Follow(user1, new User(3, "Ciro Sánchez", true))
        );

        // Act
        List<Follow> result = followRepository.findAllByIdFollower(user1.getId());

        // Assert
        assertEquals(expected, result);
    }
    @Test
    @DisplayName("findFollowedUsersTest - should return a list of user that one user follows")
    void  findFollowedUsersTest_whenUserFollowsSellers_thenReturnListOfFollowedUsers() {
        // Arrange
        User user1 = new User(1, "Agostina Avalle", true);
        List<User> expected = List.of(
                new User(3, "Ciro Sánchez", true)
        );

        // Act
        List<User> result = followRepository.findFollowedUsers(user1);

        // Assert
        assertEquals(expected, result);
    }
}

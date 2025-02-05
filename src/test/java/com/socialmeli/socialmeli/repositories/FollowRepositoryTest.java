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
                new Follow(user1, new User(2, "Carolina Comba", false))
        );

        // Act
        List<Follow> result = followRepository.findAllByIdFollower(user1.getId());

        // Assert
        assertEquals(expected, result);
    }
}

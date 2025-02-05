package com.socialmeli.socialmeli.repositories;

import com.socialmeli.socialmeli.models.Follow;
import com.socialmeli.socialmeli.models.User;
import com.socialmeli.socialmeli.utils.UserTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FollowRepositoryTest {

    private FollowRepository followRepository;

    private final UserTestUtils userTestUtils = new UserTestUtils();

    @BeforeEach
    void setUp() throws IOException {
        followRepository = new FollowRepository();
        followRepository.loadDataBase();
    }

    @Test
    @DisplayName("findAllByIdFollowerTest - should return a list of follows by id follower")
    void findAllByIdFollowerTest_whenSuccess_thenReturnListFollow() {
        // Arrange
        User user1 = userTestUtils.createSeller(1, "Agostina Avalle");
        User user3 = userTestUtils.createSeller(3, "Ciro Sánchez");
        User user4 = userTestUtils.createBuyer(4, "Eliana Navarro");

        List<Follow> expected = List.of(new Follow(user4, user3), new Follow(user4, user1));

        // Act
        List<Follow> result = followRepository.findAllByIdFollower(user4.getId());

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("findAllByIdFollowedTest - should return a list of follows by id follower")
    void findAllByIdFollowedTest_thenReturnListFollows() {
        //Arrange
        User user1 = userTestUtils.createSeller(1, "Agostina Avalle");
        User user2 = userTestUtils.createBuyer(2, "Carolina Comba");
        User user4 = userTestUtils.createBuyer(4, "Eliana Navarro");
        User user6 = userTestUtils.createBuyer(6, "Katerinne Peralta");
        List<Follow> expected =  List.of(
                new Follow(user2, user1),
                new Follow(user4, user1),
                new Follow(user6, user1)
        );

        //Act
        List<Follow> result = followRepository.findAllByIdFollowed(user1.getId());

        //Assertions
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("exists - should return true when follow exists")
    void existsTest_whenFollowExists_thenReturnTrue() {
        // Arrange
        User user1 = userTestUtils.createBuyer(2, "Carolina Comba");
        User user2 = userTestUtils.createSeller(1, "Agostina Avalle");
        Follow follow = new Follow(user1, user2);

        // Act
        boolean result = followRepository.exists(follow);

        // Assert
        assertEquals(true, result);
    }
}

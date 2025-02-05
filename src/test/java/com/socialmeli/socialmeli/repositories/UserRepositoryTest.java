package com.socialmeli.socialmeli.repositories;

import com.socialmeli.socialmeli.models.User;
import com.socialmeli.socialmeli.utils.UserTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserRepositoryTest {

    private UserRepository userRepository;

    private final UserTestUtils userTestUtils = new UserTestUtils();

    @BeforeEach
    void setUp() throws IOException {
        userRepository = new UserRepository();
        userRepository.loadDataBase();
    }

    @Test
    @DisplayName("findByIdTest - should return user when user exists")
    void findByIdTest_whenUserExists_thenReturnUser() {
        // Arrange
        User expected = userTestUtils.createSeller(1, "Agostina Avalle");

        // Act
        User result = userRepository.findById(expected.getId()).orElse(null);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("findByIdTest - should return user when user doesn't exists")
    void findByIdTest_whenUserDoesntExists_thenReturnUser() {
        // Arrange
        Integer userId = 999;

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userRepository.findById(userId).orElseThrow();
        });
    }
}

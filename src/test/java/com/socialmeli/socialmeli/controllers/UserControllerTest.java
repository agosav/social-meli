package com.socialmeli.socialmeli.controllers;

import com.socialmeli.socialmeli.enums.Message;
import com.socialmeli.socialmeli.models.User;
import com.socialmeli.socialmeli.utils.UserTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final UserTestUtils userTestUtils = new UserTestUtils();

    @Test
    @DisplayName("followToUserTest")
    public void followToUserTest_success() throws Exception {
        // Arrange
        User user1 = userTestUtils.createSeller(1, "Agostina Avalle");
        User user2 = userTestUtils.createSeller(3, "Ciro Sánchez");

        Message message = Message.USER_FOLLOWED;

        // Act & Assert
        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}", user1.getId(),
                        user2.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(message.format(user2.getName())));
    }

    @Test
    @DisplayName("followToUserTest")
    public void getFollowedUsersTest_() throws Exception {
        // Arrange
        String order = "name_asc";
        User user1 = userTestUtils.createSeller(1, "Agostina Avalle");
        User user2 = userTestUtils.createBuyer(2, "Carolina Comba");

        // Act & Assert
        mockMvc.perform(get("/users/{userId}/followed/list", user1.getId())
                        .param("order", order))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id").value(user1.getId()))
                .andExpect(jsonPath("$.user_name").value(user1.getName()))
                .andExpect(jsonPath("$.followed").isArray())
                .andExpect(jsonPath("$.followed[0].user_id").value(user2.getId()))
                .andExpect(jsonPath("$.followed[0].user_name").value(user2.getName()));
    }
}

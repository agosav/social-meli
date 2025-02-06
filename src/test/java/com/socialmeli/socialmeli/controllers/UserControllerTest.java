package com.socialmeli.socialmeli.controllers;

import com.socialmeli.socialmeli.dto.response.MessageDto;
import com.socialmeli.socialmeli.enums.Message;
import com.socialmeli.socialmeli.models.Follow;
import com.socialmeli.socialmeli.models.User;
import com.socialmeli.socialmeli.services.IUserService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
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

    @Mock
    private IUserService userService;

    @Test
    @DisplayName("followToUserTest")
    public void followToUserTest() throws Exception {
        // Arrange
        String order = "name_asc";
        User user1 = new User(1, "Agostina Avalle", true);
        User user2 = new User(2, "Carolina Comba", false);

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

    @Test
    @DisplayName("unfollowToUserTest")
    public void unfollowUserTest() throws Exception {
        // Arrange
        User user1 = new User(1, "Agostina Avalle", false);
        User user2 = new User(2, "Carolina Comba", true);
        Integer userId = 1;
        Integer userIdToUnfollow = 2;
        String expectedMessage = Message.USER_UNFOLLOWED.format(user2.getName());

        when(userService.unfollow(userId, userIdToUnfollow)).thenReturn(new MessageDto(expectedMessage));

        // Act & Assert
        mockMvc.perform(post("/" + userId + "/unfollow/" + userIdToUnfollow)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(expectedMessage));
    }
}

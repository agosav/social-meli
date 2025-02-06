package com.socialmeli.socialmeli.controllers;

import com.socialmeli.socialmeli.enums.Message;
import com.socialmeli.socialmeli.dto.response.UserFollowerCountDto;
import com.socialmeli.socialmeli.models.User;
import com.socialmeli.socialmeli.services.IUserService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

    @Mock
    private IUserService userService;

    @Test
    @DisplayName("followToUserTest")
    public void followToUserTest() throws Exception {
        // Arrange
        String order = "name_asc";
        User user1 = new User(1, "Agostina Avalle", true);
        User user2 = new User(2, "Carolina Comba", false);
        User user3 = new User(3, "Ciro Sánchez", true);
        User user5 = new User(5, "Franca Pairetti", true);
        // Act & Assert
        mockMvc.perform(get("/users/{userId}/followed/list", user2.getId())
                        .param("order", order))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id").value(user2.getId()))
                .andExpect(jsonPath("$.user_name").value(user2.getName()))
                .andExpect(jsonPath("$.followed").isArray())
                .andExpect(jsonPath("$.followed[0].user_id").value(user1.getId()))
                .andExpect(jsonPath("$.followed[0].user_name").value(user1.getName()))
                .andExpect(jsonPath("$.followed[1].user_id").value(user3.getId()))
                .andExpect(jsonPath("$.followed[1].user_name").value(user3.getName()))
                .andExpect(jsonPath("$.followed[2].user_id").value(user5.getId()))
                .andExpect(jsonPath("$.followed[2].user_name").value(user5.getName()));
    }

    //US-0002: Obtener el resultado de la cantidad de usuarios que siguen a un determinado vendedor
    @Test
    @DisplayName("countFollowersForSeller")
    public void getCountFollowerForSellerTest() throws Exception {
        //Arrange
        UserFollowerCountDto user1 = new UserFollowerCountDto(1, "Agostina Avalle", 3);
        //Act & Assertions
        mockMvc.perform(get("/users/{userId}/followers/count", user1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id").value(user1.getId()))
                .andExpect(jsonPath("$.user_name").value(user1.getName()))
                .andExpect(jsonPath("$.followers_count").value(user1.getFollowersCount()));
    }

    @Test
    @DisplayName("unfollowToUserTest")
    public void unfollowUserTest() throws Exception {
        // Arrange
        User follower = new User(2, "Carolina Comba", false);
        User followed = new User(1, "Agostina Avalle", true);

        String expectedMessage = Message.USER_UNFOLLOWED.format(followed.getName());

        // Act & Assert
        mockMvc.perform(post("/users/{userId}/unfollow/{userIdToFollow}", follower.getId(), followed.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(expectedMessage));
    }
}

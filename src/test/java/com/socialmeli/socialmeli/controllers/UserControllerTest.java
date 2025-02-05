package com.socialmeli.socialmeli.controllers;

import com.socialmeli.socialmeli.dto.response.UserFollowerCountDto;
import com.socialmeli.socialmeli.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test to validate sorting order by 'name_asc'")
    public void testFollowedListOrderNameAsc() throws Exception {
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
    @DisplayName("Test to validate sorting order by 'name_desc'")
    public void testFollowedListOrderNameDesc() throws Exception {
        // Arrange:
        String order = "name_desc";
        User user1 = new User(1, "Agostina Avalle", true);
        User user2 = new User(2, "Carolina Comba", false);

        // Act & Assert:
        mockMvc.perform(get("/users/{userId}/followed/list", 1)
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
    @DisplayName("Test to validate exception when invalid order is provided for followed list")
    public void testFollowedListOrderException() throws Exception {
        // Arrange
        String order = "nam";

        // Act & Assert:
        mockMvc.perform(get("/users/{userId}/followed/list", 1)
                        .param("order", order))
                .andExpect(status().isBadRequest());
    }
}

package com.socialmeli.socialmeli.controllers;

import com.socialmeli.socialmeli.enums.Message;
import com.socialmeli.socialmeli.dto.response.UserFollowerCountDto;
import com.socialmeli.socialmeli.models.User;
import com.socialmeli.socialmeli.utils.UserFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

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

    // US 0001 - Poder realizar la acción de “Follow” (seguir) a un determinado vendedor.
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

    @DisplayName("follow - successful")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void followTest_whenSuccessful_then200() throws Exception {
        // Arrange
        User user1 = UserFactory.createSeller(1, "Agostina Avalle");
        User user5 = UserFactory.createSeller(5, "Franca Pairetti");

        String message = Message.USER_FOLLOWED.format(user1.getName());

        // Act & Assert
        performFollow(user5.getId(), user1.getId(), message, 200);
    }

    @Test
    @DisplayName("follow - user follower not found")
    public void followTest_whenUserFollowerDoesntExist_thenReturn404() throws Exception {
        // Arrange
        Integer userFollowerId = 999;
        Integer userFollowedId = 2;

        String message = Message.USER_NOT_FOUND.format(userFollowerId);

        // Act & Assert
        performFollow(userFollowerId, userFollowedId, message, 404);
    }

    @Test
    @DisplayName("follow - user followed not found")
    public void followTest_whenUserFollowedDoesntExist_thenReturn404() throws Exception {
        // Arrange
        Integer userFollowerId = 1;
        Integer userFollowedId = 999;

        String message = Message.USER_NOT_FOUND.format(userFollowedId);

        // Act & Assert
        performFollow(userFollowerId, userFollowedId, message, 404);
    }

    @Test
    @DisplayName("follow - user followed not seller")
    public void followTest_whenUserFollowedIsNotSeller_thenReturn400() throws Exception {
        // Arrange
        Integer userIdSeller = 1;
        User userNotSeller = UserFactory.createBuyer(2, "Carolina Comba");

        String message = Message.USER_NOT_SELLER.format(userNotSeller.getName());

        // Act & Assert
        performFollow(userIdSeller, userNotSeller.getId(), message, 400);
    }

    @Test
    @DisplayName("follow - user followed is the same as follower")
    public void followTest_whenUserFollowedIsTheSameAsFollower_thenReturn400() throws Exception {
        // Arrange
        Integer userId = 1;
        String message = Message.CANNOT_FOLLOW_SELF.getStr();

        // Act & Assert
        performFollow(userId, userId, message, 400);
    }

    @Test
    @DisplayName("follow - user already followed")
    public void followTest_whenUserAlreadyFollowed_thenReturn409() throws Exception {
        // Arrange
        User user2 = UserFactory.createBuyer(2, "Carolina Comba");
        User user1 = UserFactory.createSeller(1, "Agostina Avalle");

        String message = Message.USER_ALREADY_FOLLOWED.format(user1.getName(), user2.getName());

        // Act & Assert
        performFollow(user2.getId(), user1.getId(), message, 409);
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
        User user3 = new User(3, "Ciro Sánchez", true);
        User user5 = new User(5, "Franca Pairetti", true);

        // Act & Assert:
        mockMvc.perform(get("/users/{userId}/followed/list", user2.getId())
                        .param("order", order))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id").value(user2.getId()))
                .andExpect(jsonPath("$.user_name").value(user2.getName()))
                .andExpect(jsonPath("$.followed").isArray())
                .andExpect(jsonPath("$.followed[0].user_id").value(user5.getId())) //
                .andExpect(jsonPath("$.followed[0].user_name").value(user5.getName()))
                .andExpect(jsonPath("$.followed[1].user_id").value(user3.getId()))
                .andExpect(jsonPath("$.followed[1].user_name").value(user3.getName()))
                .andExpect(jsonPath("$.followed[2].user_id").value(user1.getId()))
                .andExpect(jsonPath("$.followed[2].user_name").value(user1.getName()));
    }

    // US 0004 - Obtener un listado de todos los vendedores a los cuales sigue un determinado usuario (¿A quién sigo?).
    @ParameterizedTest
    @CsvSource({"name_asc", "name_desc", "DEFAULT"})
    @DisplayName("getFollowedUsers - successful with different order or default")
    public void getFollowedUsersTest_whenOrderIsParametrizedOrNotProvided_thenReturnOrderedList(String order)
            throws Exception {
        // Arrange
        User user1 = UserFactory.createSeller(1, "Agostina Avalle");
        User user2 = UserFactory.createBuyer(2, "Carolina Comba");
        User user3 = UserFactory.createSeller(3, "Ciro Sánchez");
        User user5 = UserFactory.createSeller(5, "Franca Pairetti");

        List<User> usersExpected;

        if ("name_asc".equals(order) || "DEFAULT".equals(order)) {
            usersExpected = List.of(user1, user3, user5); // Orden ascendente
        } else {
            usersExpected = List.of(user5, user3, user1); // Orden descendente
        }

        // Act & Assert
        ResultActions result = mockMvc.perform(get("/users/{userId}/followed/list", user2.getId())
                        .param("order", "DEFAULT".equals(order) ? "" : order))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id").value(user2.getId()))
                .andExpect(jsonPath("$.user_name").value(user2.getName()))
                .andExpect(jsonPath("$.followed").isArray())
                .andExpect(jsonPath("$.followed[0].user_id").value(user5.getId())) //
                .andExpect(jsonPath("$.followed[0].user_name").value(user5.getName()))
                .andExpect(jsonPath("$.followed[1].user_id").value(user3.getId()))
                .andExpect(jsonPath("$.followed[1].user_name").value(user3.getName()))
                .andExpect(jsonPath("$.followed[2].user_id").value(user1.getId()))
                .andExpect(jsonPath("$.followed[2].user_name").value(user1.getName()))
                .andExpect(jsonPath("$.followed.length()").value(usersExpected.size()));

        for (int i = 0; i < usersExpected.size(); i++) {
            String expectedName = usersExpected.get(i).getName();
            Integer expectedId = usersExpected.get(i).getId();
            result.andExpect(jsonPath("$.followed[" + i + "].user_id").value(expectedId))
                    .andExpect(jsonPath("$.followed[" + i + "].user_name").value(expectedName));
        }
    }

    @Test
    @Disabled
    @DisplayName("Test to validate exception when invalid order is provided for followed list")
    public void testFollowedListOrderException() throws Exception {
        // Arrange
        String order = "nam";

        // Act & Assert:
        mockMvc.perform(get("/users/{userId}/followed/list", 1)
                        .param("order", order))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("getFollowedUsers - invalid order parameter")
    public void getFollowedUsersTest_whenOrderIsInvalid_thenReturnDefaultOrderDesc() throws Exception {
        // Arrange
        String order = "asdasfcdxfvg";
        Integer userId = 1;

        // Act & Assert
        mockMvc.perform(get("/users/{userId}/followed/list", userId)
                        .param("order", order))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("getFollowedUsers - user not found")
    public void getFollowedUsersTest_whenUserDoesntExist_thenReturn404() throws Exception {
        // Arrange
        String order = "name_asc";
        Integer userId = 999;

        String message = Message.USER_NOT_FOUND.format(userId);

        // Act & Assert
        ResultActions result = mockMvc.perform(get("/users/{userId}/followed/list", userId)
                        .param("order", order))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(message));
    }

    private void performFollow(Integer followerId, Integer followedId, String expectedMessage, int expectedStatus)
            throws Exception {
        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}", followerId, followedId))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(expectedMessage));
    }
}

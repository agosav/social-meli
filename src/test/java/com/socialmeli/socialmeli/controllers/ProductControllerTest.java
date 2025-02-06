package com.socialmeli.socialmeli.controllers;

import com.socialmeli.socialmeli.dto.PostDto;
import com.socialmeli.socialmeli.models.User;
import com.socialmeli.socialmeli.utils.UserFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmeli.socialmeli.enums.Message;
import com.socialmeli.socialmeli.utils.PostFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("T-0005: getPostsOfFollowedSellers by date asc")
    public void getPostsOfFollowedSellersOrderByDateAscTest() throws Exception {
        // Arrange
        String order = "date_asc";
        User user = UserFactory.createBuyer(2);
        List<PostDto> postsExpected = List.of(
                PostFactory.createPostIdDateDto(4, LocalDate.of(2025, 1, 25)),
                PostFactory.createPostIdDateDto(5, LocalDate.of(2025, 1, 27)),
                PostFactory.createPostIdDateDto(3, LocalDate.of(2025, 1, 30)),
                PostFactory.createPostIdDateDto(2, LocalDate.of(2025, 1, 31)),
                PostFactory.createPostIdDateDto(1, LocalDate.of(2025, 2, 2))
        );

        // Act & Assert
        ResultActions result = mockMvc.perform(get("/products/followed/{userId}/list", user.getId())
                        .param("order", order))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id").value(user.getId()))
                .andExpect(jsonPath("$.posts").isArray())
                .andExpect(jsonPath("$.posts.length()").value(postsExpected.size()));

        for (int i = 0; i < postsExpected.size(); i++) {
            Integer expectedId = postsExpected.get(i).getUserId();
            String expectedDate = postsExpected.get(i).getDate().toString();
            result.andExpect(jsonPath("$.posts[" + i + "].post_id").value(expectedId))
                    .andExpect(jsonPath("$.posts[" + i + "].date").value(expectedDate));
        }
    }

    @Test
    @DisplayName("T-0005: getPostsOfFollowedSellers by date desc")
    public void getPostsOfFollowedSellersOrderByDateDescTest() throws Exception {
        // Arrange
        String order = "date_desc";
        User user = UserFactory.createBuyer(2);

        List<PostDto> postsExpected = List.of(
                PostFactory.createPostIdDateDto(1, LocalDate.of(2025, 2, 2)),
                PostFactory.createPostIdDateDto(2, LocalDate.of(2025, 1, 31)),
                PostFactory.createPostIdDateDto(3, LocalDate.of(2025, 1, 30)),
                PostFactory.createPostIdDateDto(5, LocalDate.of(2025, 1, 27)),
                PostFactory.createPostIdDateDto(4, LocalDate.of(2025, 1, 25))
        );

        // Act & Assert
        ResultActions result = mockMvc.perform(get("/products/followed/{userId}/list", user.getId())
                        .param("order", order))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id").value(user.getId()))
                .andExpect(jsonPath("$.posts").isArray())
                .andExpect(jsonPath("$.posts.length()").value(postsExpected.size()));

        for (int i = 0; i < postsExpected.size(); i++) {
            Integer expectedId = postsExpected.get(i).getUserId();
            String expectedDate = postsExpected.get(i).getDate().toString();
            result.andExpect(jsonPath("$.posts[" + i + "].post_id").value(expectedId))
                    .andExpect(jsonPath("$.posts[" + i + "].date").value(expectedDate));
        }
    }

    @Test
    @DisplayName("T-0005: getPostsOfFollowedSellers - should return 400 when order does not match")
    public void getPostsOfFollowedSellersOrderNotValidExceptionTests() throws Exception {
        // Arrange
        String order = "word";
        User user = UserFactory.createBuyer(2);

        // Act & Assert
        mockMvc.perform(get("/products/followed/{userId}/list", user.getId())
                        .param("order", order))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("T-0005: getPostsOfFollowedSellers - should return 400 when user id is negative")
    public void getPostsOfFollowedSellersUserIdNotValidExceptionTests() throws Exception {
        // Arrange
        String order = "date_asc";
        Integer userIdNegative = -1;

        // Act & Assert
        mockMvc.perform(get("/products/followed/{userId}/list", userIdNegative)
                        .param("order", order))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("T-0005: getPostsOfFollowedSellers - should return 404 when user is not found")
    public void getPostsOfFollowedSellersUserNotFoundExceptionTests() throws Exception {
        // Arrange
        String order = "date_asc";
        Integer userId = 999;
        String message = Message.USER_NOT_FOUND.format(userId);

        // Act & Assert
        mockMvc.perform(get("/products/followed/{userId}/list", userId)
                        .param("order", order))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(message));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/products/post", "/products/promo-post"})
    @DisplayName("savePost - should return 200 OK when product does not exist and user is not seller")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void savePost_whenProductDoesNotExistAndUserIsNotSeller_thenSavePost(String url) throws Exception {
        // Arrange
        Object post;

        if (url.equals("/products/post")) {
            post = PostFactory.createPostDto(2, 1);
        } else {
            post = PostFactory.createPostSaleDto(2, 1);
        }

        String message = Message.POST_PUBLISHED.getStr();

        // Act & Assert
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(message));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/products/post", "/products/promo-post"})
    @DisplayName("savePost - should return 200 when product does not exist and user is seller")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void savePost_whenProductDoesNotExistAndUserIsSeller_thenSavePost(String url) throws Exception {
        // Arrange
        Object post;

        if (url.equals("/products/post")) {
            post = PostFactory.createPostDto(1, 1);
        } else {
            post = PostFactory.createPostSaleDto(1, 1);
        }

        String message = Message.POST_PUBLISHED.getStr();

        // Act & Assert
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(message));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/products/post", "/products/promo-post"})
    @DisplayName("savePost - should return 409 when product already exists")
    void savePost_whenProductAlreadyExists_thenReturn400(String url) throws Exception {
        // Arrange
        Object post;

        if (url.equals("/products/post")) {
            post = PostFactory.createPostDto(1, 201);
        } else {
            post = PostFactory.createPostSaleDto(1, 201);
        }

        String message = Message.PRODUCT_ALREADY_EXISTS.getStr();

        // Act & Assert
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(message));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/products/post", "/products/promo-post"})
    @DisplayName("savePost - should return 400 when no body is received")
    void savePost_whenNoBodyReceived_thenReturn400(String url) throws Exception {
        // Act & Assert
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}

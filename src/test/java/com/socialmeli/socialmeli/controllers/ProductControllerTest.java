package com.socialmeli.socialmeli.controllers;

import com.socialmeli.socialmeli.dto.PostDto;
import com.socialmeli.socialmeli.enums.Message;
import com.socialmeli.socialmeli.models.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmeli.socialmeli.repositories.IPostRepository;
import com.socialmeli.socialmeli.repositories.IUserRepository;
import com.socialmeli.socialmeli.services.PostService;
import com.socialmeli.socialmeli.utils.PostFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.annotation.DirtiesContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest

@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostService service;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("T-0005: getPostsOfFollowedSellers by date asc")
    public void getPostsOfFollowedSellersOrderByDateAscTest() throws Exception {
        // Arrange
        String order = "date_asc";
        User user = new User(2, "Carolina Comba", false);
        List<PostDto> posts = List.of(
                PostFactory.createPostIdDateDto(1, LocalDate.of(2025, 2, 2)),
                PostFactory.createPostIdDateDto(2, LocalDate.of(2025, 1, 31)),
                PostFactory.createPostIdDateDto(3, LocalDate.of(2025, 1, 30)),
                PostFactory.createPostIdDateDto(4, LocalDate.of(2025, 1, 25)),
                PostFactory.createPostIdDateDto(5, LocalDate.of(2025, 1, 27))
        );

        // Act & Assert
        mockMvc.perform(get("/products/followed/{userId}/list", user.getId())
                        .param("order", order))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id").value(user.getId()))
                .andExpect(jsonPath("$.posts[0].post_id").value(posts.get(3).getUserId()))
                .andExpect(jsonPath("$.posts[0].date").value(posts.get(3).getDate().toString()))
                .andExpect(jsonPath("$.posts[1].post_id").value(posts.get(4).getUserId()))
                .andExpect(jsonPath("$.posts[1].date").value(posts.get(4).getDate().toString()))
                .andExpect(jsonPath("$.posts[2].post_id").value(posts.get(2).getUserId()))
                .andExpect(jsonPath("$.posts[2].date").value(posts.get(2).getDate().toString()))
                .andExpect(jsonPath("$.posts[3].post_id").value(posts.get(1).getUserId()))
                .andExpect(jsonPath("$.posts[3].date").value(posts.get(1).getDate().toString()))
                .andExpect(jsonPath("$.posts[4].post_id").value(posts.get(0).getUserId()))
                .andExpect(jsonPath("$.posts[4].date").value(posts.get(0).getDate().toString()));
    }

    @Test
    @DisplayName("T-0005: getPostsOfFollowedSellers by date desc")
    public void getPostsOfFollowedSellersOrderByDateDescTest() throws Exception {
        // Arrange
        String order = "date_desc";
        User user = new User(2, "Carolina Comba", false);

        List<PostDto> posts = List.of(
                PostFactory.createPostIdDateDto(1, LocalDate.of(2025, 2, 2)),
                PostFactory.createPostIdDateDto(2, LocalDate.of(2025, 1, 31)),
                PostFactory.createPostIdDateDto(3, LocalDate.of(2025, 1, 30)),
                PostFactory.createPostIdDateDto(4, LocalDate.of(2025, 1, 25)),
                PostFactory.createPostIdDateDto(5, LocalDate.of(2025, 1, 27))
        );

        // Act & Assert
        mockMvc.perform(get("/products/followed/{userId}/list", user.getId())
                        .param("order", order))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id").value(user.getId()))
                .andExpect(jsonPath("$.posts[0].post_id").value(posts.get(0).getUserId()))
                .andExpect(jsonPath("$.posts[0].date").value(posts.get(0).getDate().toString()))
                .andExpect(jsonPath("$.posts[1].post_id").value(posts.get(1).getUserId()))
                .andExpect(jsonPath("$.posts[1].date").value(posts.get(1).getDate().toString()))
                .andExpect(jsonPath("$.posts[2].post_id").value(posts.get(2).getUserId()))
                .andExpect(jsonPath("$.posts[2].date").value(posts.get(2).getDate().toString()))
                .andExpect(jsonPath("$.posts[3].post_id").value(posts.get(4).getUserId()))
                .andExpect(jsonPath("$.posts[3].date").value(posts.get(4).getDate().toString()))
                .andExpect(jsonPath("$.posts[4].post_id").value(posts.get(3).getUserId()))
                .andExpect(jsonPath("$.posts[4].date").value(posts.get(3).getDate().toString()));
    }

    @Test
    @DisplayName("T-0005: getPostsOfFollowedSellers - should return 400 when order does not match")
    public void getPostsOfFollowedSellersOrderNotValidExceptionTests() throws Exception {
        // Arrange
        String order = "word";
        User user = new User(2, "Carolina Comba", false);

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

        // Act & Assert
        mockMvc.perform(get("/products/followed/{userId}/list", userId)
                        .param("order", order))
                .andExpect(status().isNotFound());
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

    @ParameterizedTest
    @ValueSource(strings = {"/products/post", "/products/promo-post"})
    @DisplayName("savePost - when body is invalid should return 400")
    @Disabled
    void savePost_whenBodyIsInvalid_thenReturn400(String url) {
        // TODO: Implementar este test cuando estén hechas las validaciones
    }


    @Test
    @DisplayName("US-0011 - Get the number of promotional products for a specific seller")
    void getPromoPostCountTest() throws Exception {

        mockMvc.perform(get("/products/promo-post/count")
                        .param("user_id", "5"))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.promo_products_count").value(1))
                .andExpect(jsonPath("$.user_name").value("Franca Pairetti"));
    }

    @Test
    @DisplayName("US-0011 - NotFoundException")
    void getPromoPostCountTest_whenUserNotFound_thenThrow404() throws Exception {

        mockMvc.perform(get("/products/promo-post/count")
                        .param("user_id", "203"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("US-0011 - Invalid param")
    void getPromoPostCountTest_when() throws Exception {

        mockMvc.perform(get("/products/promo-post/count")
                        .param("user_id", "-5"))
                .andExpect(status().isBadRequest());
    }

}

package com.socialmeli.socialmeli.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmeli.socialmeli.enums.Message;
import com.socialmeli.socialmeli.repositories.IPostRepository;
import com.socialmeli.socialmeli.repositories.IUserRepository;
import com.socialmeli.socialmeli.services.PostService;
import com.socialmeli.socialmeli.utils.PostFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}

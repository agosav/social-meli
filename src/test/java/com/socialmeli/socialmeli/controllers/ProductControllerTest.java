package com.socialmeli.socialmeli.controllers;

import com.socialmeli.socialmeli.dto.response.PostIdDto;
import com.socialmeli.socialmeli.dto.response.ProductListDto;
import com.socialmeli.socialmeli.models.Post;
import com.socialmeli.socialmeli.models.Product;
import com.socialmeli.socialmeli.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.time.LocalDate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @DisplayName("T-0005: getPostsOfFollowedSellers by date desc")
    public void getPostsOfFollowedSellersOrderByDateDescTest() throws Exception {
        // Arrange
        String order = "date_desc";
        User user = new User(2, "Carolina Comba", false);
        List<Post> posts = List.of(
                new Post(1, new User(1, "Agostina Avalle", true), LocalDate.of(2025, 2, 2),
                        new Product(201, "headphones", "Electronics", "Dell", "Silver", ""),
                        1, 1200.00, true, 50.00),
                new Post(2, new User(1, "Agostina Avalle", true), LocalDate.of(2025, 1, 31),
                        new Product(202, "Laptop", "Electronics", "Dell", "Silver", ""),
                        2, 1200.00, true, 50.00),
                new Post(3, new User(1, "Agostina Avalle", true), LocalDate.of(2025, 1, 30),
                        new Product(203, "chair", "Furniture", "Dell", "Silver", ""),
                        2, 1200.00, true, 50.00),
                new Post(4, new User(3, "Ciro Sánchez", true), LocalDate.of(2025, 1, 25),
                        new Product(204, "Smartphone", "Electronics", "Apple", "Black", ""),
                        1, 999.99, false, 0.0),
                new Post(5, new User(5, "Franca Pairetti", true), LocalDate.of(2025, 1, 27),
                        new Product(205, "Office Chair", "Furniture", "ErgoTech", "Gray", ""),
                        2, 150.00, true, 25.00)
        );

        // Act & Assert
        mockMvc.perform(get("/products/followed/{userId}/list", user.getId())
                        .param("order", order))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id").value(user.getId()))
                .andExpect(jsonPath("$.posts[0].post_id").value(posts.get(0).getId()))
                .andExpect(jsonPath("$.posts[0].date").value(posts.get(0).getDate().toString()))
                .andExpect(jsonPath("$.posts[1].post_id").value(posts.get(1).getId()))
                .andExpect(jsonPath("$.posts[1].date").value(posts.get(1).getDate().toString()))
                .andExpect(jsonPath("$.posts[2].post_id").value(posts.get(2).getId()))
                .andExpect(jsonPath("$.posts[2].date").value(posts.get(2).getDate().toString()))
                .andExpect(jsonPath("$.posts[3].post_id").value(posts.get(4).getId()))
                .andExpect(jsonPath("$.posts[3].date").value(posts.get(4).getDate().toString()))
                .andExpect(jsonPath("$.posts[4].post_id").value(posts.get(3).getId()))
                .andExpect(jsonPath("$.posts[4].date").value(posts.get(3).getDate().toString()));
    }

    @Test
    @DisplayName("T-0005: getPostsOfFollowedSellers - order exception")
    public void getPostsOfFollowedSellersOrderExceptionTests() throws Exception {
        // Arrange
        String order = "palabra";
        User user = new User(2, "Carolina Comba", false);

        // Act & Assert
        mockMvc.perform(get("/products/followed/{userId}/list", user.getId())
                        .param("order", order))
                .andExpect(status().isBadRequest());
    }
}

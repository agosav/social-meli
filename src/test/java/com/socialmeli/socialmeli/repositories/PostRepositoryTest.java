package com.socialmeli.socialmeli.repositories;

import com.socialmeli.socialmeli.models.Post;
import com.socialmeli.socialmeli.models.Product;
import com.socialmeli.socialmeli.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostRepositoryTest {
    private PostRepository postRepository;
    @BeforeEach
    void setUp() throws IOException {
        postRepository = new PostRepository();
        postRepository.loadDataBase();
    }
    @Test
    @DisplayName("postFromUsers - should return a list of post form followed users")
    void  postFromUsersTest_whenSellersFollowedPost_thenReturnListOfPostFromUsersFollowed() {
        // Arrange
        User follower = new User(2, "Carolina Comba", false);
        List<User> follows = List.of(
                new User(1, "Agostina Avalle", true),
                new User(3, "Ciro Sánchez", true)
        );
        List<Post> expected = new ArrayList<>();

        expected.add(Post.builder()
                .id(1)
                .user(new User(3, "Ciro Sánchez", true))
                .date(LocalDate.of(2025, 1, 19))
                .product(Product.builder()
                        .id(201)
                        .name("headphones")
                        .type("Electronics")
                        .brand("Dell")
                        .color("Silver")
                        .notes("Includes charger and carrying case")
                        .build())
                .category(1)
                .price(1200.00)
                .hasPromo(true)
                .discount(50.00)
                .build());
        expected.add(Post.builder()
                .id(2)
                .user(new User(3, "Ciro Sánchez", true))
                .date(LocalDate.of(2025, 1, 20))
                .product(Product.builder()
                        .id(202)
                        .name("Laptop")
                        .type("Electronics")
                        .brand("Dell")
                        .color("Silver")
                        .notes("Includes charger and carrying case")
                        .build())
                .category(2)
                .price(1200.00)
                .hasPromo(true)
                .discount(50.00)
                .build());
        expected.add(Post.builder()
                .id(3)
                .user(new User(3, "Ciro Sánchez", true))
                .date(LocalDate.of(2025, 1, 21))
                .product(Product.builder()
                        .id(203)
                        .name("chair")
                        .type("Furniture")
                        .brand("Dell")
                        .color("Silver")
                        .notes("Includes charger and carrying case")
                        .build())
                .category(2)
                .price(1200.00)
                .hasPromo(true)
                .discount(50.00)
                .build());
        expected.add(Post.builder()
                .id(6)
                .user(new User(1, "Agostina Avalle", true))
                .date(LocalDate.of(2025, 1, 16))
                .product(Product.builder()
                        .id(206)
                        .name("Gaming Monitor")
                        .type("Electronics")
                        .brand("Samsung")
                        .color("Black")
                        .notes("32-inch, 4K resolution")
                        .build())
                .category(1)
                .price(450.00)
                .hasPromo(false)
                .discount(null)
                .build());

        // Act
        List<Post> result = postRepository.postFromUsers(follows);

        // Assert
        assertEquals(expected, result);
    }

}

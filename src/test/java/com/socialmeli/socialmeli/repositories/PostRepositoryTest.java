package com.socialmeli.socialmeli.repositories;

import com.socialmeli.socialmeli.models.Post;
import com.socialmeli.socialmeli.models.Product;
import com.socialmeli.socialmeli.models.User;
import com.socialmeli.socialmeli.utils.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostRepositoryTest {
    private PostRepository postRepository;
    @BeforeEach
    void setUp() throws IOException {
        postRepository = new PostRepository();
        postRepository.loadDataBase();
    }
    @Test
    @DisplayName("postFromUsers - should return a list of post form followed users")
    @Disabled
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
                .user(new User(1, "Agostina Avalle", true))
                .date(LocalDate.of(2025, 2, 2))
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
                .user(new User(1, "Agostina Avalle", true))
                .date(LocalDate.of(2025, 1, 31))
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
                .user(new User(1, "Agostina Avalle", true))
                .date(LocalDate.of(2025, 1, 30))
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
                .id(4)
                .user(new User(3, "Ciro Sánchez", true))
                .date(LocalDate.of(2025, 1, 25))
                .product(Product.builder()
                        .id(204)
                        .name("Smartphone")
                        .type("Electronics")
                        .brand("Apple")
                        .color("Black")
                        .notes("128GB storage, unlocked")
                        .build())
                .category(1)
                .price(999.99)
                .hasPromo(false)
                .discount(null)
                .build());

        // Act
        List<Post> result = postRepository.postFromUsers(follows);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("save - should save post to the list")
    void saveTest_whenSave_thenVoid() {
        // Arrange
        Post post = Post.builder().id(500).build();

        // Act
        postRepository.save(post);

        // Assert
        assertTrue(postRepository.getPosts().contains(post));
    }

    @Test
    @DisplayName("existsProductById - should return true when post exists")
    void existsProductByIdTest_whenPostExists_thenReturnTrue() {
        // Act
        boolean result = postRepository.existsProductById(201);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("existsProductById - should return false when post doesnt exists")
    void existsProductByIdTest_whenPostDoesntExists_thenReturnFalse() {
        // Act
        boolean result = postRepository.existsProductById(999);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("findPostsWithPromoByUserTest")
    public void findPostsWithPromoByUserTest_whenPostExists_thenReturnListPosts() {
        //Arrange
        User user = UserFactory.createSeller(3, "Ciro Sánchez");
        List<Post> expected = List.of(
                Post.builder()
                        .id(4)
                        .user(new User(3, "Ciro Sánchez", true))
                        .date(LocalDate.of(2025, 1, 25))
                        .product(new Product(204, "Smartphone", "Electronics", "Apple", "Black", "128GB storage, unlocked"))
                        .category(1)
                        .price(999.99)
                        .hasPromo(true)
                        .discount(null) // Asignamos 0.0 ya que el valor es null
                        .build()
        );

        //Act
        List<Post> result = postRepository.findPostsWithPromoByUser(user.getId());

        //Assertion
        assertEquals(expected, result);
    }
}

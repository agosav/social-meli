package com.socialmeli.socialmeli.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.socialmeli.socialmeli.dto.PostDto;
import com.socialmeli.socialmeli.dto.PostSaleDto;
import com.socialmeli.socialmeli.dto.ProductDto;
import com.socialmeli.socialmeli.models.Post;
import com.socialmeli.socialmeli.models.Product;
import com.socialmeli.socialmeli.models.User;

public class PostFactory {

    public static ProductDto createProductDto(Integer id) {
        return ProductDto.builder()
                .id(id)
                .name("Product")
                .brand("Brand")
                .color("Color")
                .type("Type")
                .notes("Notes")
                .build();
    }

    public static Object createPostDto(Integer userId, Integer productId, boolean hasPromo) {
        if (hasPromo) {
            return PostSaleDto.builder()
                    .idUser(userId)
                    .date(LocalDate.now())
                    .category(1)
                    .price(100.0)
                    .product(createProductDto(productId))
                    .hasPromo(true)
                    .discount(10.0)
                    .build();
        } else {
            return PostDto.builder()
                    .userId(userId)
                    .date(LocalDate.now())
                    .category(1)
                    .price(100.0)
                    .product(createProductDto(productId))
                    .build();
        }
    }

    public static PostDto createPostIdDateDto(Integer userId, LocalDate date) {
        return PostDto.builder()
                .userId(userId)
                .date(date)
                .build();
    }

    public static List<Post> getPostsForUsers(List<User> users) {
        List<Post> posts = new ArrayList<>();
        LocalDate now = LocalDate.now();
        Integer cont = 0;

        for (User user : users) {
            cont++;
            posts.add(Post.builder()
                    .id(posts.size() + 1)
                    .user(user)
                    .date(now.minusDays(cont)) // Dentro de las últimas 2 semanas
                    .product(new Product())
                    .category(1)
                    .price(100.0)
                    .hasPromo(false)
                    .discount(0.0)
                    .build());
            posts.add(Post.builder()
                    .id(posts.size() + 1)
                    .user(user)
                    .date(now.minusDays(cont + 20)) // Fuera de las últimas 2 semanas
                    .product(new Product())
                    .category(1)
                    .price(100.0)
                    .hasPromo(false)
                    .discount(0.0)
                    .build());
        }

        return posts;
    }
}

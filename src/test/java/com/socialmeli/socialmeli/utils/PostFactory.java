package com.socialmeli.socialmeli.utils;

import java.time.LocalDate;
import java.util.List;

import com.socialmeli.socialmeli.dto.PostDto;
import com.socialmeli.socialmeli.dto.PostSaleDto;
import com.socialmeli.socialmeli.dto.ProductDto;
import com.socialmeli.socialmeli.dto.response.PostIdDto;
import com.socialmeli.socialmeli.dto.response.ProductListDto;

public class PostFactory {

    public static ProductDto createProductDto(Integer id) {
        return ProductDto.builder().id(id).build();
    }

    public static PostDto createPostDto(Integer userId, Integer productId) {
        return PostDto.builder()
                .userId(userId)
                .product(createProductDto(productId))
                .build();
    }

    public static PostSaleDto createPostSaleDto(Integer userId, Integer productId) {
        return PostSaleDto.builder()
                .idUser(userId)
                .product(createProductDto(productId))
                .discount(1.0)
                .build();
    }

    public static PostIdDto createPostIdDto(Integer userId, Integer postId, LocalDate date, ProductDto product, Integer category, Double price) {
        return PostIdDto.builder()
                .userId(userId)
                .id(postId)
                .date(date)
                .product(product)
                .category(category)
                .price(price)
                .build();
    }

    public static ProductListDto createProductListDto(Integer userId, List<PostIdDto> posts) {
        return new ProductListDto(userId, posts);
    }

}

package com.socialmeli.socialmeli.utils;

import com.socialmeli.socialmeli.dto.PostDto;
import com.socialmeli.socialmeli.dto.PostSaleDto;
import com.socialmeli.socialmeli.dto.ProductDto;

import java.time.LocalDate;

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

    public static PostDto createPostIdDateDto(Integer userId, LocalDate date) {
        return PostDto.builder()
                .userId(userId)
                .date(date)
                .build();
    }
}

package com.socialmeli.socialmeli.utils;

import java.time.LocalDate;
import java.util.List;

import com.socialmeli.socialmeli.dto.PostDto;
import com.socialmeli.socialmeli.dto.PostSaleDto;
import com.socialmeli.socialmeli.dto.ProductDto;
import com.socialmeli.socialmeli.dto.response.PostIdDto;
import com.socialmeli.socialmeli.dto.response.ProductListDto;

import java.time.LocalDate;

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

    public static ProductDto createProductDtoComplete(Integer id, String name, String brand, String color, String type, String notes) {
        return ProductDto.builder()
                .id(id)
                .name(name)
                .brand(brand)
                .color(color)
                .type(type)
                .notes(notes)
                .build();
    }

    public static PostDto createPostDto(Integer userId, Integer productId) {
        return PostDto.builder()
                .userId(userId)
                .date(LocalDate.now())
                .category(1)
                .price(100.0)
                .product(createProductDto(productId))
                .build();
    }

    public static PostSaleDto createPostSaleDto(Integer userId, Integer productId) {
        return PostSaleDto.builder()
                .idUser(userId)
                .date(LocalDate.now())
                .category(1)
                .price(100.0)
                .product(createProductDto(productId))
                .hasPromo(true)
                .discount(10.0)
                .build();
    }

    public static PostDto createPostIdDateDto(Integer userId, LocalDate date) {
        return PostDto.builder()
                .userId(userId)
                .date(date)
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

package com.socialmeli.socialmeli.utils;

import com.socialmeli.socialmeli.dto.PostDto;
import com.socialmeli.socialmeli.dto.ProductDto;
import com.socialmeli.socialmeli.models.User;

public class PostFactory {

    public static PostDto createPostDtoBySeller(Integer productId) {
        User user = UserFactory.createSeller(1);
        ProductDto productDto = ProductDto.builder().id(productId).build();
        return PostDto.builder().userId(user.getId()).product(productDto).build();
    }

    public static PostDto createPostDtoByBuyer(Integer productId) {
        User user = UserFactory.createBuyer(1);
        ProductDto productDto = ProductDto.builder().id(productId).build();
        return PostDto.builder().userId(user.getId()).product(productDto).build();
    }

    public static PostDto createPostDto(User user, Integer productId) {
        ProductDto productDto = ProductDto.builder().id(productId).build();
        return PostDto.builder().userId(user.getId()).product(productDto).build();
    }
}

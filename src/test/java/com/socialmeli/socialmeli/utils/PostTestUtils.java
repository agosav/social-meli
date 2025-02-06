package com.socialmeli.socialmeli.utils;

import com.socialmeli.socialmeli.dto.PostDto;
import com.socialmeli.socialmeli.dto.ProductDto;
import com.socialmeli.socialmeli.models.User;

public class PostTestUtils {

    private Integer maxId = 1;

    private final UserTestUtils userTestUtils = new UserTestUtils();

    public PostDto createPostDtoBySeller(Integer productId) {
        User user = userTestUtils.createSeller();
        ProductDto productDto = ProductDto.builder().id(productId).build();
        return PostDto.builder().userId(user.getId()).product(productDto).build();
    }

    public PostDto createPostDtoByBuyer(Integer productId) {
        User user = userTestUtils.createBuyer();
        ProductDto productDto = ProductDto.builder().id(productId).build();
        return PostDto.builder().userId(user.getId()).product(productDto).build();
    }

    public PostDto createPostDto(User user) {
        ProductDto productDto = ProductDto.builder().id(maxId++).build();
        return PostDto.builder().userId(user.getId()).product(productDto).build();
    }
}

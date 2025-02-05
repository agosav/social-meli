package com.socialmeli.socialmeli.services;

import com.socialmeli.socialmeli.dto.ProductDto;
import com.socialmeli.socialmeli.dto.response.PostIdDto;
import com.socialmeli.socialmeli.dto.response.ProductListDto;
import com.socialmeli.socialmeli.exception.NotFoundException;
import com.socialmeli.socialmeli.models.Post;
import com.socialmeli.socialmeli.models.User;
import com.socialmeli.socialmeli.repositories.IFollowRepository;
import com.socialmeli.socialmeli.repositories.IPostRepository;
import com.socialmeli.socialmeli.repositories.IUserRepository;
import com.socialmeli.socialmeli.util.EntityCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@SpringBootTest
class PostServiceTest {
    @Mock
    private  IPostRepository postRepository;
    @Mock
    private  IUserRepository userRepository;
    @Mock
    private  IFollowRepository followRepository;
    @InjectMocks
    private PostService service;


    @Test
    @DisplayName("getRecentPostFromUsersAscTest - should retrun a list of post ordered Asc")
    void getRecentPostFromUsersTest_whenFollowsPostedLast2WeeksAndOrderIsDateAsc_thenReturnListOfPostOrderedAsc() {
        //ARRANGE
        String order = "date_asc";
        User user = EntityCreator.getUser();
        Integer id = user.getId();
        List<User> sellersFollowed = EntityCreator.getListOfSellers();
        List<Post> postsFromFollows = EntityCreator.getPostsForUsers(sellersFollowed);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(followRepository.findFollowedUsers(user)).thenReturn(sellersFollowed);
        when(postRepository.postFromUsers(sellersFollowed)).thenReturn(postsFromFollows);
        List<PostIdDto> expectedPosts = new ArrayList<>();
        expectedPosts.add(PostIdDto.builder()
                .id(3)
                .userId(2)
                .date(LocalDate.now().minusDays(2))
                .product(new ProductDto())
                .category(1)
                .price(100.0)
                .build());

        expectedPosts.add(PostIdDto.builder()
                .id(1)
                .userId(1)
                .date(LocalDate.now().minusDays(1))
                .product(new ProductDto())
                .category(1)
                .price(100.0)
                .build());


        ProductListDto expected = new ProductListDto(id, expectedPosts);

        //ACT
        ProductListDto actual = service.getRecentPostFromUsers(order, id);
        //ASSERT
        assertEquals(expected, actual);
        assertEquals(expected.getPosts(), actual.getPosts());
        verify(userRepository).findById(id);
        verify(followRepository).findFollowedUsers(user);
        verify(postRepository).postFromUsers(sellersFollowed);
    }
    @Test
    @DisplayName("getRecentPostFromUsersDescTest - should retrun a list of post ordered Desc")
    void getRecentPostFromUsersTest_whenFollowedPostedLast2WeeksAndOrderIsDateDesc_thenReturnListOfPostOrderedDesc() {
        //ARRANGE

        String order = "date_desc";
        User user = EntityCreator.getUser();
        Integer id = user.getId();
        List<User> sellersFollowed = EntityCreator.getListOfSellers();
        List<Post> postsFromFollows = EntityCreator.getPostsForUsers(sellersFollowed);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(followRepository.findFollowedUsers(user)).thenReturn(sellersFollowed);
        when(postRepository.postFromUsers(sellersFollowed)).thenReturn(postsFromFollows);
        List<PostIdDto> expectedPosts = new ArrayList<>();
        expectedPosts.add(PostIdDto.builder()
                .id(1)
                .userId(1)
                .date(LocalDate.now().minusDays(1))
                .product(new ProductDto())
                .category(1)
                .price(100.0)
                .build());

        expectedPosts.add(PostIdDto.builder()
                .id(3)
                .userId(2)
                .date(LocalDate.now().minusDays(2))
                .product(new ProductDto())
                .category(1)
                .price(100.0)
                .build());

        ProductListDto expected = new ProductListDto(id, expectedPosts);

        //ACT
        ProductListDto actual = service.getRecentPostFromUsers(order, id);
        //ASSERT
        assertEquals(expected, actual);
        assertEquals(expected.getPosts(), actual.getPosts());
        verify(userRepository).findById(id);
        verify(followRepository).findFollowedUsers(user);
        verify(postRepository).postFromUsers(sellersFollowed);
    }
    @Test
    @DisplayName("getRecentPostFromUsersNotFoundTest - should throw NotFoundException")
    void getRecentPostFromUsersTest_whenUserNotFound_thenThrowUserNotFoundException() {
        //ARRANGE
        String order = "date_asc";
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(NotFoundException.class, () -> service.getRecentPostFromUsers(order, anyInt()));
    }
  /*  @Test
    void getRecentPostFromUsersTest_whenOrderIsInvalid_thenThrowInvalidOrderException() {
        //ARRANGE
        String order = "MartinCapo";
        Integer id = 1;
        //Act & Assert
        assertThrows(BadRequestException.class, () -> service.getRecentPostFromUsers(order, id));

    }

   */
}

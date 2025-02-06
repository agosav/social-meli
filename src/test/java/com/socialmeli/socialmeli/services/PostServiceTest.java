package com.socialmeli.socialmeli.services;

import com.socialmeli.socialmeli.dto.PostDto;
import com.socialmeli.socialmeli.dto.PostSaleDto;
import com.socialmeli.socialmeli.dto.ProductDto;
import com.socialmeli.socialmeli.dto.response.MessageDto;
import com.socialmeli.socialmeli.dto.response.PostIdDto;
import com.socialmeli.socialmeli.dto.response.ProductListDto;
import com.socialmeli.socialmeli.enums.Message;
import com.socialmeli.socialmeli.exception.AlreadyExistsException;
import com.socialmeli.socialmeli.exception.NotFoundException;
import com.socialmeli.socialmeli.models.Post;
import com.socialmeli.socialmeli.models.User;
import com.socialmeli.socialmeli.repositories.IFollowRepository;
import com.socialmeli.socialmeli.repositories.IPostRepository;
import com.socialmeli.socialmeli.repositories.IUserRepository;
import com.socialmeli.socialmeli.util.EntityCreator;
import com.socialmeli.socialmeli.utils.PostFactory;
import com.socialmeli.socialmeli.utils.UserFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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

    // US 0005 - Dar de alta una nueva publicación.
    @ParameterizedTest
    @ValueSource(strings = {"/products/post", "/products/promo-post"})
    @DisplayName("savePost - should save the post when product does not exist and user is not seller")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void savePost_whenProductDoesNotExistAndUserIsNotSeller_thenSavePost(String url) {
        savePostTest(url, 1, 300, false, false);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/products/post", "/products/promo-post"})
    @DisplayName("savePost - should save the post when product does not exist and user is seller")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void savePost_whenProductDoesNotExistAndUserIsSeller_thenSavePost(String url) {
        savePostTest(url, 2, 300, false, true);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/products/post", "/products/promo-post"})
    @DisplayName("savePost - should throw AlreadyExistsException when product already exists")
    void savePost_whenProductAlreadyExists_thenThrowAlreadyExistsException(String url) {
        savePostTest(url, 1, 201, true, false);
    }

    private void savePostTest(String url, Integer userId, Integer productId, boolean productExists, boolean isSeller) {
        // Arrange
        User user = isSeller ? UserFactory.createSeller(userId) : UserFactory.createBuyer(userId);
        Object post = createPostDto(url, userId, productId);

        String message = Message.POST_PUBLISHED.getStr();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(postRepository.existsProductById(productId)).thenReturn(productExists);

        // Act & Assert
        if (productExists) {
            assertThrows(AlreadyExistsException.class, () -> {
                executeSavePost(url, post);
            });
        } else {
            MessageDto result = executeSavePost(url, post);

            if (isSeller) {
                verify(userRepository, never()).update(user);
            } else {
                verify(userRepository).update(user);
            }

            verify(postRepository).save(any(Post.class));
            assertEquals(message, result.getMessage());
        }
    }

    private Object createPostDto(String url, Integer userId, Integer productId) {
        if (url.equals("/products/post")) {
            return PostFactory.createPostDto(userId, productId);
        } else {
            return PostFactory.createPostSaleDto(userId, productId);
        }
    }

    private MessageDto executeSavePost(String url, Object post) {
        if (url.equals("/products/post")) {
            return service.savePost((PostDto) post);
        } else {
            return service.savePostSale((PostSaleDto) post);
        }
    }
}

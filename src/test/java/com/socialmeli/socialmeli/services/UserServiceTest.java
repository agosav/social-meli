package com.socialmeli.socialmeli.services;

import com.socialmeli.socialmeli.dto.response.FollowedListDto;
import com.socialmeli.socialmeli.dto.response.MessageDto;
import com.socialmeli.socialmeli.dto.response.UserDto;
import com.socialmeli.socialmeli.enums.Message;
import com.socialmeli.socialmeli.exception.NotFoundException;
import com.socialmeli.socialmeli.dto.response.UserFollowerCountDto;
import com.socialmeli.socialmeli.models.Follow;
import com.socialmeli.socialmeli.models.User;
import com.socialmeli.socialmeli.repositories.IFollowRepository;
import com.socialmeli.socialmeli.repositories.IUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IFollowRepository followRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("getFollowersTest - should return followers list ordered by name asc")
    void getFollowedListTest_whenUserExists_thenReturnFollowedListAsc() {
        // Arrange
        String order = "name_asc";
        User user1 = User.builder().id(1).name("Emilia Mernes").build();
        User user2 = User.builder().id(2).name("Taylor Swift").build();
        User user3 = User.builder().id(3).name("Tini Stoessel").build();
        List<UserDto> listUserDto = List.of(
                new UserDto(user2.getId(), user2.getName()),
                new UserDto(user3.getId(), user3.getName())
        );
        FollowedListDto expected = new FollowedListDto(user1.getId(), user1.getName(), listUserDto);
        List<Follow> follows = List.of(
                new Follow(user1, user3),
                new Follow(user1, user2)
        );
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(followRepository.findAllByIdFollower(user1.getId())).thenReturn(follows);

        // Act
        FollowedListDto result = userService.getFollowedList(user1.getId(), order);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("getFollowersTest - should return followers list ordered by name desc")
    void getFollowedListTest_whenUserExists_thenReturnFollowedListDesc() {
        // Arrange
        String order = "name_desc";
        User user1 = User.builder().id(1).name("Emilia Mernes").build();
        User user2 = User.builder().id(2).name("Taylor Swift").build();
        User user3 = User.builder().id(3).name("Tini Stoessel").build();
        List<UserDto> listUserDto = List.of(
                new UserDto(user3.getId(), user3.getName()),
                new UserDto(user2.getId(), user2.getName())
        );
        FollowedListDto expected = new FollowedListDto(user1.getId(), user1.getName(), listUserDto);
        List<Follow> follows = List.of(
                new Follow(user1, user2),
                new Follow(user1, user3)
        );
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(followRepository.findAllByIdFollower(user1.getId())).thenReturn(follows);

        // Act
        FollowedListDto result = userService.getFollowedList(user1.getId(), order);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("unfollowTest - should return Message USER_UNFOLLOWED")
    void unfollowTest_whenFollowedExists_thenReturnMessageOk() {
        // Arrange
        User follower = User.builder().id(1).name("Emilia Mernes").build();
        User followed = User.builder().id(2).name("Taylor Swift").build();
        Follow follow = new Follow(follower, followed);
        when(userRepository.findById(follower.getId())).thenReturn(Optional.of(follower));
        when(userRepository.findById(followed.getId())).thenReturn(Optional.of(followed));
        when(followRepository.exists(follow)).thenReturn(true);

        // Act
        MessageDto result = userService.unfollow(follower.getId(), followed.getId());

        // Assert
        assertEquals(Message.USER_UNFOLLOWED.format(followed.getName()), result.getMessage());
    }

    @Test
    @DisplayName("unfollowTest - should return NotFoundException")
    void unfollowTest_whenFollowedNotExists_thenReturnNotFoundException() {
        // Arrange
        Integer userId = 1;
        Integer followedId = 999;
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User(userId, "Emilia Mernes", false)));
        when(userRepository.findById(followedId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.unfollow(userId, followedId));

        assertThat(exception.getMessage()).isEqualTo(Message.USER_NOT_FOUND.format(followedId));
    }

    //US-0002 : Obtener el resultado de la cantidad de usuarios que siguen a un determinado vendedor
    @Test
    @DisplayName("countFollowersForSeller")
    void countFollowersForSaller_whenUserExists_thenReturnCountFollowersForSeller() {
        //Arrange
        User user1 = User.builder().id(1).name("Agostina Avalle").isSeller(true).build();
        UserFollowerCountDto expected = new UserFollowerCountDto(user1.getId(), user1.getName(), 3);
        List<Follow> followers = List.of(
                new Follow(new User(2, "Carolina Comba", false), user1),
                new Follow(new User(4, "Eliana Navarro", false), user1),
                new Follow(new User(6, "Katerinne Peralta", false), user1)
        );

        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(followRepository.findAllByIdFollowed(user1.getId())).thenReturn(followers);

        //Act
        UserFollowerCountDto result = userService.countFollowers(user1.getId());

        //Assert
        assertEquals(expected, result);
    }
}

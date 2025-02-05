package com.socialmeli.socialmeli.services;

import com.socialmeli.socialmeli.dto.response.FollowedListDto;
import com.socialmeli.socialmeli.dto.response.MessageDto;
import com.socialmeli.socialmeli.dto.response.UserDto;
import com.socialmeli.socialmeli.enums.Message;
import com.socialmeli.socialmeli.exception.AlreadyExistsException;
import com.socialmeli.socialmeli.exception.IllegalActionException;
import com.socialmeli.socialmeli.exception.NotFoundException;
import com.socialmeli.socialmeli.exception.UserNotSellerException;
import com.socialmeli.socialmeli.models.Follow;
import com.socialmeli.socialmeli.models.User;
import com.socialmeli.socialmeli.repositories.IFollowRepository;
import com.socialmeli.socialmeli.repositories.IUserRepository;

import com.socialmeli.socialmeli.utils.UserTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IFollowRepository followRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final UserTestUtils userTestUtils = new UserTestUtils();

    @Test
    @DisplayName("follow - successful")
    void followTest_whenSuccessful_thenReturnMessageDto() {
        User user1 = userTestUtils.createBuyer();
        User user2 = userTestUtils.createSeller();

        Follow follow = new Follow(user1, user2);
        Message expected = Message.USER_FOLLOWED;

        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        when(followRepository.exists(follow)).thenReturn(false);

        // Act
        MessageDto result = userService.follow(user1.getId(), user2.getId());

        // Arrange
        verify(followRepository).add(follow);
        assertEquals(expected.format(user2.getName()), result.getMessage());
    }

    @Test
    @DisplayName("follow - user follower not found")
    void followTest_whenUserFollowerDoesntExists_thenThrowNotFoundException() {
        // Arrange
        Integer userFollowerId = 1;
        Integer userFollowedId = 2;

        when(userRepository.findById(userFollowerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.follow(userFollowerId, userFollowedId));
    }

    @Test
    @DisplayName("follow - user followed not found")
    void followTest_whenUserFollowedDoesntExists_thenThrowNotFoundException() {
        // Arrange
        Integer userFollowerId = 1;
        Integer userFollowedId = 2;

        when(userRepository.findById(userFollowerId)).thenReturn(Optional.of(mock(User.class)));
        when(userRepository.findById(userFollowedId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.follow(userFollowerId, userFollowedId));
    }

    @Test
    @DisplayName("follow - user followed not seller")
    void followTest_whenUserFollowedIsNotSeller_thenThrowNotFoundException() {
        // Arrange
        User user1 = userTestUtils.createBuyer();
        User user2 = userTestUtils.createBuyer();

        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        // Act & Assert
        assertThrows(UserNotSellerException.class, () -> userService.follow(user1.getId(), user2.getId()));
    }

    @Test
    @DisplayName("follow - user followed is the same as follower")
    void followTest_whenUserFollowedIsTheSameAsFollower_thenThrowIllegalActionException() {
        // Arrange
        User user1 = userTestUtils.createSeller();

        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));

        // Act & Assert
        assertThrows(IllegalActionException.class, () -> userService.follow(user1.getId(), user1.getId()));
    }

    @Test
    @DisplayName("follow - user already followed")
    void followTest_whenUserAlreadyFollowed_thenThrowAlreadyExistsException() {
        // Arrange
        User user1 = userTestUtils.createBuyer();
        User user2 = userTestUtils.createSeller();

        Follow follow = new Follow(user1, user2);

        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        when(followRepository.exists(follow)).thenReturn(true);

        // Act & Assert
        assertThrows(AlreadyExistsException.class, () -> userService.follow(user1.getId(), user2.getId()));
    }

    @Test
    @DisplayName("getFollowersTest - should return followers list ordered by name asc")
    void getFollowedListTest_whenUserExists_thenReturnFollowedListAsc() {
        // Arrange
        String order = "name_asc";
        User user1 = userTestUtils.createSeller("Emilia Mernes");
        User user2 = userTestUtils.createSeller("Taylor Swift");
        User user3 = userTestUtils.createBuyer("Tini Stoessel");

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
        User user1 = userTestUtils.createSeller("Emilia Mernes");
        User user2 = userTestUtils.createSeller("Taylor Swift");
        User user3 = userTestUtils.createBuyer("Tini Stoessel");

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
}

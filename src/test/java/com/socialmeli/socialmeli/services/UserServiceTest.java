package com.socialmeli.socialmeli.services;

import com.socialmeli.socialmeli.dto.response.FollowedListDto;
import com.socialmeli.socialmeli.dto.response.MessageDto;
import com.socialmeli.socialmeli.dto.response.UserDto;
import com.socialmeli.socialmeli.enums.Message;
import com.socialmeli.socialmeli.exception.AlreadyExistsException;
import com.socialmeli.socialmeli.exception.IllegalActionException;
import com.socialmeli.socialmeli.exception.NotFoundException;
import com.socialmeli.socialmeli.exception.UserNotSellerException;
import com.socialmeli.socialmeli.dto.response.UserFollowerCountDto;
import com.socialmeli.socialmeli.models.Follow;
import com.socialmeli.socialmeli.models.User;
import com.socialmeli.socialmeli.repositories.IFollowRepository;
import com.socialmeli.socialmeli.repositories.IUserRepository;

import com.socialmeli.socialmeli.utils.UserFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    // US 0001 - Poder realizar la acción de “Follow” (seguir) a un determinado vendedor.
    @Test
    @DisplayName("follow - successful")
    void followTest_whenSuccessful_thenReturnMessageDto() {
        // Arrange
        User user1 = UserFactory.createBuyer(1);
        User user2 = UserFactory.createSeller(2);

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
        User user1 = UserFactory.createBuyer(1);
        User user2 = UserFactory.createBuyer(2);

        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        // Act & Assert
        assertThrows(UserNotSellerException.class, () -> userService.follow(user1.getId(), user2.getId()));
    }

    @Test
    @DisplayName("follow - user followed is the same as follower")
    void followTest_whenUserFollowedIsTheSameAsFollower_thenThrowIllegalActionException() {
        // Arrange
        User user1 = UserFactory.createSeller(1);

        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));

        // Act & Assert
        assertThrows(IllegalActionException.class, () -> userService.follow(user1.getId(), user1.getId()));
    }

    @Test
    @DisplayName("follow - user already followed")
    void followTest_whenUserAlreadyFollowed_thenThrowAlreadyExistsException() {
        // Arrange
        User user1 = UserFactory.createBuyer(1);
        User user2 = UserFactory.createSeller(2);

        Follow follow = new Follow(user1, user2);

        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        when(followRepository.exists(follow)).thenReturn(true);

        // Act & Assert
        assertThrows(AlreadyExistsException.class, () -> userService.follow(user1.getId(), user2.getId()));
    }

    // US 0002 - Obtener el resultado de la cantidad de usuarios que siguen a un determinado vendedor.
    @Test
    @DisplayName("countFollowersForSeller")
    void countFollowersForSeller_whenUserExists_thenReturnCountFollowersForSeller() {
        // Arrange
        User user1 = UserFactory.createSeller(1, "Agostina Avalle");
        User user2 = UserFactory.createBuyer(2, "Carolina Comba");
        User user4 = UserFactory.createBuyer(4, "Eliana Navarro");
        User user6 = UserFactory.createBuyer(6, "Katerinne Peralta");

        UserFollowerCountDto expected = new UserFollowerCountDto(user1.getId(), user1.getName(), 3);
        List<Follow> followers = List.of(
                new Follow(user2, user1),
                new Follow(user4, user1),
                new Follow(user6, user1)
        );

        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(followRepository.findAllByIdFollowed(user1.getId())).thenReturn(followers);

        // Act
        UserFollowerCountDto result = userService.countFollowers(user1.getId());

        // Assert
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("countFollowersForSeller - user not found")
    void countFollowersForSeller_whenUserDoesntExists_thenReturnThrowNotFoundException() {
        // Arrange
        Integer userId = 7;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.countFollowers(userId));
    }

    @Test
    @DisplayName("countFollowersForSeller - user not seller")
    void countFollowersForSeller_whenUserDoesntSeller_thenReturnThrowNotSellerException() {
        // Arrange
        User user2 = UserFactory.createBuyer(2, "Carolina Comba");
        List<Follow> followers = List.of();

        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        // Act & Assert
        assertThrows(UserNotSellerException.class, () -> userService.countFollowers(user2.getId()));
    }


    // US 0004 - Obtener un listado de todos los vendedores a los cuales sigue un determinado usuario (¿A quién sigo?).
    @ParameterizedTest
    @DisplayName("getFollowedList - should return followers list ordered by name")
    @CsvSource({"name_asc", "name_desc"})
    void getFollowedListTest_whenUserExists_thenReturnFollowedList(String order) {
        // Arrange
        User user1 = UserFactory.createSeller(1, "Emilia Mernes");
        User user2 = UserFactory.createSeller(2, "Taylor Swift");
        User user3 = UserFactory.createBuyer(3, "Tini Stoessel");

        List<UserDto> expectedList = (order.equals("name_asc"))
                ? List.of(new UserDto(user2.getId(), user2.getName()), new UserDto(user3.getId(), user3.getName()))
                : List.of(new UserDto(user3.getId(), user3.getName()), new UserDto(user2.getId(), user2.getName()));

        FollowedListDto expected = new FollowedListDto(user1.getId(), user1.getName(), expectedList);
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
    @DisplayName("getFollowedList - when user doesn't exist should throw user not found exception")
    void getFollowedListTest_whenUserDoesntExist_thenThrowNotFound() {
        // Arrange
        Integer userId = 999;
        String order = "name_asc";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.getFollowedList(userId, order));
    }
}

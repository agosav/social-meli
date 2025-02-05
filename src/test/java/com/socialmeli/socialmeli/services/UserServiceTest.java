package com.socialmeli.socialmeli.services;

import com.socialmeli.socialmeli.dto.response.FollowedListDto;
import com.socialmeli.socialmeli.dto.response.UserDto;
import com.socialmeli.socialmeli.models.Follow;
import com.socialmeli.socialmeli.models.User;
import com.socialmeli.socialmeli.repositories.IFollowRepository;
import com.socialmeli.socialmeli.repositories.IUserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void getFollowedListTest_whenUserExists_thenReturnFollowedListAsc() {
        // Arrange
        Integer userId = 1;
        String order = "name_asc";
        User emi = User.builder().id(1).name("Emilia Mernes").build();
        User taylor = User.builder().id(2).name("Taylor Swift").build();
        User tini = User.builder().id(3).name("Tini Stoessel").build();
        List<UserDto> listUserDto = List.of(
                new UserDto(taylor.getId(), taylor.getName()),
                new UserDto(tini.getId(), tini.getName())
        );
        FollowedListDto expected = new FollowedListDto(userId, emi.getName(), listUserDto);
        List<Follow> follows = List.of(
                new Follow(emi, tini),
                new Follow(emi, taylor)
        );
        when(userRepository.findById(userId)).thenReturn(Optional.of(emi));
        when(followRepository.findAllByIdFollower(userId)).thenReturn(follows);

        // Act
        FollowedListDto result = userService.getFollowedList(userId, order);

        // Assert
        assertEquals(expected, result);
    }
}

package com.romsa.library.service;

import com.romsa.library.entity.User;
import com.romsa.library.repository.BookLoanRepository;
import com.romsa.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookLoanRepository loanRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
    }

    @Test
    void givenNewUser_whenSaveUser_thenSavedSuccessfully() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        userService.saveUser(testUser, bindingResult);

        verify(userRepository).save(testUser);
        verify(bindingResult, never()).rejectValue(any(), any(), any());
    }

    @Test
    void givenExistingEmail_whenSaveUser_thenRejectsWithError() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        when(messageSource.getMessage(any(), any(), any())).thenReturn("Email exists error");

        userService.saveUser(testUser, bindingResult);

        verify(bindingResult).rejectValue("email", "error.user", "Email exists error");
        verify(userRepository, never()).save(any());
    }

    @Test
    void givenValidUpdate_whenUpdateUser_thenUpdatesFields() {
        User updatedUser = new User();
        updatedUser.setName("New Name");
        updatedUser.setEmail("new@example.com");

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(testUser));
        when(userRepository.existsByEmailAndNotSameUser(anyString(), anyLong())).thenReturn(false);

        userService.updateUser(1L, updatedUser, bindingResult);

        assertThat(testUser.getName()).isEqualTo("New Name");
        assertThat(testUser.getEmail()).isEqualTo("new@example.com");
        verify(userRepository).save(testUser);
    }

    @Test
    void givenUserWithoutLoans_whenDeleteUser_thenDeletesUser() {
        when(loanRepository.existsByUserId(anyLong())).thenReturn(false);

        userService.deleteUser(1L);

        verify(userRepository).deleteUserById(1L);
    }

    @Test
    void givenUserWithLoans_whenDeleteUser_thenThrowsException() {
        when(loanRepository.existsByUserId(anyLong())).thenReturn(true);

        assertThatThrownBy(() -> userService.deleteUser(1L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void whenFindAllUsers_thenReturnsAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(testUser));

        List<User> users = userService.findAllUsers();

        assertThat(users).containsExactly(testUser);
    }

    @Test
    void givenExistingId_whenFindById_thenReturnsUser() {
        when(userRepository.findUserById(1L)).thenReturn(testUser);

        User found = userService.findById(1L);

        assertThat(found).isEqualTo(testUser);
    }
}
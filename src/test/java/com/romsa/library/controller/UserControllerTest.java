package com.romsa.library.controller;

import com.romsa.library.entity.User;
import com.romsa.library.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
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
    void givenUsers_whenViewUsersPage_thenReturnsViewWithUsers() throws Exception {
        when(userService.findAllUsers()).thenReturn(Collections.singletonList(testUser));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/list"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", Collections.singletonList(testUser)));
    }

    @Test
    void whenShowAddUserForm_thenReturnsAddFormWithUser() throws Exception {
        mockMvc.perform(get("/users/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/add-user"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void givenValidUser_whenSaveUser_thenRedirectsToUsersList() throws Exception {
        mockMvc.perform(post("/users/save")
                        .param("name", "New User")
                        .param("city", "New City")
                        .param("email", "new@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(userService).saveUser(any(User.class), any(BindingResult.class));
    }

    @Test
    void givenInvalidUser_whenSaveUser_thenReturnsAddFormWithErrors() throws Exception {
        mockMvc.perform(post("/users/save")
                        .param("name", "")
                        .param("email", "invalid-email"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/add-user"))
                .andExpect(model().attributeHasErrors("user"))
                .andExpect(model().attributeHasFieldErrors("user", "name", "email"));
    }

    @Test
    void givenDuplicateEmail_whenSaveUser_thenReturnsAddFormWithErrors() throws Exception {
        doAnswer(invocation -> {
            BindingResult result = invocation.getArgument(1);
            result.rejectValue("email", "duplicate", "Email already exists");
            return null;
        }).when(userService).saveUser(any(User.class), any(BindingResult.class));

        mockMvc.perform(post("/users/save")
                        .param("name", "User")
                        .param("email", "duplicate@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/add-user"))
                .andExpect(model().attributeHasErrors("user"));
    }

    @Test
    void givenUserId_whenShowUpdateForm_thenReturnsUpdateFormWithUser() throws Exception {
        when(userService.findById(1L)).thenReturn(testUser);

        mockMvc.perform(get("/users/update")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/update-user"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", testUser));
    }

    @Test
    void givenValidUser_whenUpdateUser_thenRedirectsToUsersList() throws Exception {
        mockMvc.perform(post("/users/update")
                        .param("id", "1")
                        .param("name", "Updated User")
                        .param("city", "Updated City")
                        .param("email", "updated@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(userService).updateUser(eq(1L), any(User.class), any(BindingResult.class));
    }

    @Test
    void givenInvalidUser_whenUpdateUser_thenReturnsUpdateFormWithErrors() throws Exception {
        mockMvc.perform(post("/users/update")
                        .param("id", "1")
                        .param("name", "")
                        .param("email", "invalid-email"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/update-user"))
                .andExpect(model().attributeHasErrors("user"))
                .andExpect(model().attributeHasFieldErrors("user", "name", "email"));
    }

    @Test
    void givenDuplicateEmailOnUpdate_whenUpdateUser_thenReturnsUpdateFormWithErrors() throws Exception {
        doAnswer(invocation -> {
            BindingResult result = invocation.getArgument(2);
            result.rejectValue("email", "duplicate", "Email already exists");
            return null;
        }).when(userService).updateUser(anyLong(), any(User.class), any(BindingResult.class));

        mockMvc.perform(post("/users/update")
                        .param("id", "1")
                        .param("name", "User")
                        .param("email", "duplicate@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/update-user"))
                .andExpect(model().attributeHasErrors("user"));
    }

    @Test
    void givenValidUserId_whenDeleteUser_thenRedirectsToUsersList() throws Exception {
        mockMvc.perform(get("/users/delete")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(userService).deleteUser(1L);
    }

    @Test
    void givenUserWithLoans_whenDeleteUser_thenRedirectsWithErrorMessage() throws Exception {
        doThrow(new RuntimeException("user.error.loans"))
                .when(userService).deleteUser(anyLong());

        mockMvc.perform(get("/users/delete")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute("errorMessage", "user.error.loans"));
    }
}
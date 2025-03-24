package com.romsa.library.controller;

import com.romsa.library.entity.Book;
import com.romsa.library.entity.User;
import com.romsa.library.service.BookLoanService;
import com.romsa.library.service.BookService;
import com.romsa.library.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookLoanController.class)
@ExtendWith(SpringExtension.class)
class BookLoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookLoanService loanService;

    private User testUser;
    private Book testBook;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");

        testBook = new Book();
        testBook.setId(100L);
        testBook.setTitle("Test Book");
    }

    @Test
    void givenUserId_whenGetBorrowedBooks_thenReturnsViewWithUser() throws Exception {
        given(userService.findById(1L)).willReturn(testUser);

        mockMvc.perform(get("/users/borrow")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("loan/borrowed-books"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void givenUserId_whenShowLoanForm_thenReturnsViewWithBooks() throws Exception {
        given(userService.findById(1L)).willReturn(testUser);
        given(bookService.findAll()).willReturn(Collections.singletonList(testBook));

        mockMvc.perform(get("/users/borrow/loan")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("loan/loan-book"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("books", Collections.singletonList(testBook)));
    }

    @Test
    void givenValidLoanRequest_whenLoanBook_thenRedirectsToUserPage() throws Exception {
        mockMvc.perform(post("/users/borrow/loan")
                        .param("id", "1")
                        .param("bookId", "100"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/borrow?id=1"));
    }

    @Test
    void givenLoanError_whenLoanBook_thenRedirectsWithErrorMessage() throws Exception {
        doThrow(new RuntimeException("Test error message"))
                .when(loanService).loanBook(anyLong(), anyLong());

        mockMvc.perform(post("/users/borrow/loan")
                        .param("id", "1")
                        .param("bookId", "100"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/borrow/loan?id=1"))
                .andExpect(flash().attribute("errorMessage", "Test error message"));
    }

    @Test
    void givenValidReturnRequest_whenReturnBook_thenRedirectsToUserPage() throws Exception {
        mockMvc.perform(get("/users/borrow/return")
                        .param("id", "1")
                        .param("bookId", "100"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/borrow?id=1"));
    }
}
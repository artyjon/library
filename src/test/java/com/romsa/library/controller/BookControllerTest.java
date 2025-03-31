package com.romsa.library.controller;

import com.romsa.library.entity.Book;
import com.romsa.library.service.BookLoanService;
import com.romsa.library.service.BookService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookLoanService bookLoanService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
    }

    @Test
    void givenBooksAndLoans_whenViewBooksPage_thenReturnsViewWithBooksAndLoans() throws Exception {
        when(bookService.findAll()).thenReturn(Collections.singletonList(testBook));
        when(bookLoanService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"))
                .andExpect(model().attributeExists("books", "bookLoans"))
                .andExpect(model().attribute("books", Collections.singletonList(testBook)));
    }

    @Test
    void whenShowAddBookForm_thenReturnsAddFormWithBook() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/add-book"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    void givenValidBook_whenSaveBook_thenRedirectsToBooksList() throws Exception {
        mockMvc.perform(post("/books/save")
                        .param("title", "New Book")
                        .param("author", "New Author"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService).saveBook(any(Book.class), any(BindingResult.class));
    }

    @Test
    void givenInvalidBook_whenSaveBook_thenReturnsAddFormWithErrors() throws Exception {
        mockMvc.perform(post("/books/save")
                        .param("title", "")
                        .param("author", "Author"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/add-book"))
                .andExpect(model().attributeHasErrors("book"))
                .andExpect(model().attributeHasFieldErrors("book", "title"));
    }

    @Test
    void givenDuplicateBook_whenSaveBook_thenReturnsAddFormWithErrors() throws Exception {
        doAnswer(invocation -> {
            BindingResult result = invocation.getArgument(1);
            result.rejectValue("title", "duplicate", "Book already exists");
            return null;
        }).when(bookService).saveBook(any(Book.class), any(BindingResult.class));

        mockMvc.perform(post("/books/save")
                        .param("title", "Existing Book")
                        .param("author", "Author"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/add-book"))
                .andExpect(model().attributeHasErrors("book"));
    }

    @Test
    void givenValidBookId_whenDeleteBook_thenRedirectsToBooksList() throws Exception {
        mockMvc.perform(get("/books/delete")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService).deleteBook(1L);
    }

    @Test
    void givenBookWithLoans_whenDeleteBook_thenRedirectsWithErrorMessage() throws Exception {
        doThrow(new RuntimeException("book.error.loans"))
                .when(bookService).deleteBook(anyLong());

        mockMvc.perform(get("/books/delete")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"))
                .andExpect(flash().attribute("errorMessage", "book.error.loans"));
    }
}
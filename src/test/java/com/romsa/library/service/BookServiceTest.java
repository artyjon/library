
package com.romsa.library.service;

import com.romsa.library.entity.Book;
import com.romsa.library.repository.BookLoanRepository;
import com.romsa.library.repository.BookRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookLoanRepository loanRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setTotalCopies(5);
    }

    @Test
    void givenNewBook_whenSaveBook_thenSavedSuccessfully() {
        when(bookRepository.existsByTitleAndAuthor(anyString(), anyString())).thenReturn(false);

        bookService.saveBook(testBook, bindingResult);

        verify(bookRepository).save(testBook);
        verify(bindingResult, never()).rejectValue(any(), any(), any());
    }

    @Test
    void givenExistingBook_whenSaveBook_thenRejectsWithError() {
        when(bookRepository.existsByTitleAndAuthor(anyString(), anyString())).thenReturn(true);
        when(messageSource.getMessage(any(), any(), any())).thenReturn("Book exists error");

        bookService.saveBook(testBook, bindingResult);

        verify(bindingResult).rejectValue("title", "error.book", "Book exists error");
        verify(bookRepository, never()).save(any());
    }

    @Test
    void givenBookWithoutLoans_whenDeleteBook_thenDeletesBook() {
        when(loanRepository.existsByBookId(anyLong())).thenReturn(false);

        bookService.deleteBook(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void givenBookWithLoans_whenDeleteBook_thenThrowsException() {
        when(loanRepository.existsByBookId(anyLong())).thenReturn(true);

        assertThatThrownBy(() -> bookService.deleteBook(1L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void givenExistingId_whenFindById_thenReturnsBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        Book found = bookService.findById(1L);

        assertThat(found).isEqualTo(testBook);
    }

    @Test
    void whenFindAllBooks_thenReturnsAllBooks() {
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(testBook));

        List<Book> books = bookService.findAll();

        assertThat(books).containsExactly(testBook);
    }
}
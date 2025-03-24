package com.romsa.library.service;

import com.romsa.library.entity.Book;
import com.romsa.library.entity.BookLoan;
import com.romsa.library.entity.User;
import com.romsa.library.repository.BookLoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookLoanServiceTest {

    @Mock
    private BookLoanRepository loanRepository;

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private BookLoanService loanService;

    private User user;
    private Book book;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setTotalCopies(2);
    }

    @Test
    void givenUserAndBookExist_whenLoanBook_thenLoanCreatedSuccessfully() {

        given(userService.findById(user.getId())).willReturn(user);
        given(bookService.findById(book.getId())).willReturn(book);
        given(loanRepository.existsByUserIdAndBookId(user.getId(), book.getId())).willReturn(false);

        loanService.loanBook(user.getId(), book.getId());

        ArgumentCaptor<BookLoan> loanCaptor = ArgumentCaptor.forClass(BookLoan.class);
        verify(loanRepository).save(loanCaptor.capture());

        BookLoan savedLoan = loanCaptor.getValue();
        assertThat(savedLoan.getUser()).isEqualTo(user);
        assertThat(savedLoan.getBook()).isEqualTo(book);
        assertThat(savedLoan.getBorrowDate()).isEqualTo(LocalDate.now());
        assertThat(book.getTotalCopies()).isEqualTo(1);
    }

    @Test
    void givenBookAlreadyLoaned_whenLoanBook_thenThrowsException() {

        given(userService.findById(user.getId())).willReturn(user);
        given(bookService.findById(book.getId())).willReturn(book);
        given(loanRepository.existsByUserIdAndBookId(user.getId(), book.getId())).willReturn(true);
        given(messageSource.getMessage(eq("user.borrow.error.alreadyLoaned"), any(), any()))
                .willReturn("Already loaned error message");

        assertThatThrownBy(() -> loanService.loanBook(user.getId(), book.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Already loaned error message");
    }

    @Test
    void givenNoAvailableCopies_whenLoanBook_thenThrowsException() {

        book.setTotalCopies(0);
        given(userService.findById(user.getId())).willReturn(user);
        given(bookService.findById(book.getId())).willReturn(book);
        given(messageSource.getMessage(eq("user.borrow.error.noCopies"), any(), any()))
                .willReturn("No copies error message");

        assertThatThrownBy(() -> loanService.loanBook(user.getId(), book.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No copies error message");
    }

    @Test
    void givenExistingLoan_whenReturnBook_thenCopiesIncreasedAndLoanDeleted() {

        BookLoan loan = new BookLoan(null, user, book, LocalDate.now());
        given(userService.findById(user.getId())).willReturn(user);
        given(bookService.findById(book.getId())).willReturn(book);
        given(loanRepository.findByUserIdAndBookId(user.getId(), book.getId()))
                .willReturn(Optional.of(loan));

        loanService.returnBook(user.getId(), book.getId());

        assertThat(book.getTotalCopies()).isEqualTo(3);
        verify(loanRepository).delete(loan);
    }

    @Test
    void givenNoActiveLoan_whenReturnBook_thenThrowsException() {

        given(userService.findById(user.getId())).willReturn(user);
        given(bookService.findById(book.getId())).willReturn(book);
        given(loanRepository.findByUserIdAndBookId(user.getId(), book.getId()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> loanService.returnBook(user.getId(), book.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Loan not found error");
    }
}
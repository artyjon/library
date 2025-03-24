package com.romsa.library.service;

import com.romsa.library.entity.Book;
import com.romsa.library.repository.BookLoanRepository;
import com.romsa.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookLoanRepository loanRepository;
    private final MessageSource messageSource;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    public void saveBook(Book book, BindingResult result) {
        if (bookRepository.existsByTitleAndAuthor(book.getTitle(), book.getAuthor())) {
            result.rejectValue("title", "error.book",
                    messageSource.getMessage("book.error.exists", null, LocaleContextHolder.getLocale()));
            return;
        }
        bookRepository.save(book);
    }
    @Transactional
    public void deleteBook(Long id) {
        if (loanRepository.existsByBookId(id)) {
            throw new RuntimeException();
        } else {
            bookRepository.deleteById(id);
        }
    }
    @Transactional(readOnly = true)
    public Book findById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }
}
package com.romsa.library.service;

import com.romsa.library.entity.Book;
import com.romsa.library.repository.BookLoanRepository;
import com.romsa.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final BookLoanRepository loanRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        if (loanRepository.existsByBookId(id)) {
            throw new RuntimeException("Удаление невозможно, как минимум 1 экземпляр книги не возвращен");
        } else {
            bookRepository.deleteById(id);
        }
    }

    public Book findById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }
}
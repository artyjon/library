package com.romsa.library.service;

import com.romsa.library.entity.Book;
import com.romsa.library.repository.BookLoanRepository;
import com.romsa.library.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookLoanRepository loanRepository;
    private final MessageSource messageSource;

    private Map<Long, Book> bookCache;

    private ScheduledExecutorService scheduler;

    @PostConstruct
    public void init() {

        bookCache = new HashMap<>();
        bookRepository.findAll().forEach(book -> bookCache.put(book.getId(), book));
        System.out.println("Кэш книг инициализирован: " + bookCache.size() + " книг загружено");

        // 2. Добавление тестовых данных при пустой БД
        if (bookCache.isEmpty()) {
            Book sampleBook = new Book(null, "Test Book", "Test Author", 2022, 10);
            bookRepository.save(sampleBook);
            System.out.println("Добавлена тестовая книга");
        }

        // 3. Запуск фоновой задачи (пример)
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(
                () -> System.out.println("Проверка состояния книг..."),
                0, 24, TimeUnit.HOURS);
    }

    @PreDestroy
    public void cleanup() {

        if (bookCache != null) {
            bookCache.clear();
            System.out.println("Кэш книг очищен");
        }

        if (scheduler != null) {
            scheduler.shutdown();
            System.out.println("Фоновые задачи остановлены");
        }
    }


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
package com.romsa.library.controller;

import com.romsa.library.entity.Book;
import com.romsa.library.service.BookLoanService;
import com.romsa.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final BookLoanService loanService;

    @GetMapping
    public String viewBooksPage(Model model) {
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("bookLoans", loanService.findAll());
        return "books/list";
    }

    @GetMapping("/add")
    public String addBookForm(Book book, Model model) {
        model.addAttribute("book", book);
        return "books/add-book";
    }

    @PostMapping("/save")
    public String addBook(@ModelAttribute("book") @Valid Book book, BindingResult result) {
        if (result.hasErrors()) {
            return "books/add-book";
        }

        bookService.saveBook(book, result);

        if (result.hasErrors()) {
            return "books/add-book";
        }

        return "redirect:/books";
    }

    @GetMapping("/delete")
    public String deleteBook(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteBook(id);
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "book.error.loans");
        }
        return "redirect:/books";
    }
}

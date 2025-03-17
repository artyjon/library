package com.romsa.library.controller;

import com.romsa.library.service.BookLoanService;
import com.romsa.library.service.BookService;
import com.romsa.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users/borrow/{id}")
public class BookLoanController {

    private final BookService bookService;
    private final BookLoanService loanService;
    private final UserService userService;

    @GetMapping
    public String borrowedBooks(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "loan/borrowed-books";
    }

    @GetMapping("/loan")
    public String loanBookForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("books", bookService.findAll());
        return "loan/loan-book";
    }

    @PostMapping("/loan")
    public String loanBook(@PathVariable Long id, @RequestParam Long bookId, RedirectAttributes redirectAttributes) {
        try {
            loanService.loanBook(id, bookId);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return String.format("redirect:/users/borrow/%d/loan", id);
        }
        return String.format("redirect:/users/borrow/%d", id);
    }

    @GetMapping("/return/{bookId}")
    public String returnBook(@PathVariable Long id, @PathVariable Long bookId) {
        loanService.returnBook(id, bookId);
        return String.format("redirect:/users/borrow/%d", id);
    }
}
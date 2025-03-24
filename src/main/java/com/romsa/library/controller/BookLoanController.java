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
@RequestMapping("/users/borrow")
public class BookLoanController {

    private final BookService bookService;
    private final BookLoanService loanService;
    private final UserService userService;

    @GetMapping
    public String borrowedBooks(@RequestParam Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "loan/borrowed-books";
    }

    @GetMapping("/loan")
    public String loanBookForm(@RequestParam Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("books", bookService.findAll());
        return "loan/loan-book";
    }

    @PostMapping("/loan")
    public String loanBook(@RequestParam("id") Long userId, @RequestParam Long bookId, RedirectAttributes redirectAttributes) {
        try {
            loanService.loanBook(userId, bookId);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return String.format("redirect:/users/borrow/loan?id=%d", userId);
        }
        return String.format("redirect:/users/borrow?id=%d", userId);
    }

    @GetMapping("/return")
    public String returnBook(@RequestParam Long id, @RequestParam Long bookId) {
        loanService.returnBook(id, bookId);
        return String.format("redirect:/users/borrow?id=%d", id);
    }
}
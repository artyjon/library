package com.romsa.library.controller;

import com.romsa.library.entity.User;
import com.romsa.library.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
@RequestMapping({"/", "/users"})
public class UserController {

    private final UserService userService;

    @GetMapping
    public String viewUsersPage(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "users/list";
    }

    @GetMapping("/add")
    public String addUserForm(User user, Model model) {
        model.addAttribute("user", user);
        return "users/add-user";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") @Valid User user, RedirectAttributes redirectAttributes, BindingResult result) {
        if (result.hasErrors()) {
            return "users/add-user";
        }

        try {
            userService.saveUser(user);
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/users/add";
        }
        return "redirect:/users";
    }

    @GetMapping("/update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "users/update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(id, user);

        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/users/update/" + id;
        }

        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUserById(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/users";
    }

}

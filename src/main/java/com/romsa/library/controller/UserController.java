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
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "users/add-user";
        }

        userService.saveUser(user, result);

        if (result.hasErrors()) {//валидация уникальности email
            return "users/add-user";
        }

        return "redirect:/users";
    }

    @GetMapping("/update")
    public String updateUserForm(@RequestParam Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "users/update-user";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "users/update-user";
        }

        userService.updateUser(user.getId(), user, result);

        if (result.hasErrors()) {  //валидация уникальности email
            return "users/update-user";
        }

        return "redirect:/users";
    }
    @GetMapping("/delete")
    public String deleteUserById(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "user.error.loans");
        }
        return "redirect:/users";
    }

}

package com.romsa.library.service;

import com.romsa.library.entity.User;
import com.romsa.library.repository.BookLoanRepository;
import com.romsa.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BookLoanRepository loanRepository;
    private final MessageSource messageSource;

    @Transactional
    public void saveUser(User user, BindingResult result) {
        if (userRepository.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", messageSource.getMessage("user.email.exists", null, LocaleContextHolder.getLocale()));
            return;
        }
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(Long id, User updatedUser, BindingResult result) {

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            result.rejectValue("id", "error.user", "User not found");
            return;
        }

        if (userRepository.existsByEmailAndNotSameUser(updatedUser.getEmail(), id)) {
            result.rejectValue("email", "error.user", messageSource.getMessage("user.email.exists", null, LocaleContextHolder.getLocale()));
            return;
        }
        user.setName(updatedUser.getName());
        user.setCity(updatedUser.getCity());
        user.setEmail(updatedUser.getEmail());

        userRepository.save(user);
    }
    @Transactional
    public void deleteUser(Long id) {
        if (loanRepository.existsByUserId(id)) {
            throw new RuntimeException();
        } else {
            userRepository.deleteUserById(id);
        }
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }


    public User findById(Long id) {
        return userRepository.findUserById(id);
    }
}

package com.romsa.library.service;

import com.romsa.library.entity.User;
import com.romsa.library.repository.BookLoanRepository;
import com.romsa.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BookLoanRepository loanRepository;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void saveUser(User user) {

        if(userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        userRepository.save(user);
    }


    public void updateUser(Long id, User updatedUser) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userRepository.existsByEmailAndEmailIsNotTheSame(user.getEmail(), updatedUser.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
            user.setName(updatedUser.getName());
            user.setCity(updatedUser.getCity());
            user.setEmail(updatedUser.getEmail());

        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (loanRepository.existsByUserId(id)) {
            throw new RuntimeException("Удаление пользователя невозможно, как минимум 1 книга не возвращена");
        } else {
            userRepository.deleteUserById(id);
        }
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

package com.romsa.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 30, message = "Имя пользователя должно содержать от 2 до 30 символов")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String name;

    @Size(min = 2, max = 30, message = "Город должен содержать от 2 до 30 символов")
    @NotBlank(message = "Город не может быть пустым")
    private String city;

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Введите корректный email")
    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookLoan> borrowedBooks = new ArrayList<>();

}

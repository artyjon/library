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

    @Size(min = 2, max = 30, message = "{user.name.size.error}")
    @NotBlank(message = "{user.name.blank}")
    private String name;

    @Size(min = 2, max = 30, message = "{user.city.size.error}")
    @NotBlank(message = "{user.city.blank}")
    private String city;

    @NotBlank(message = "{user.email.blank}")
    @Email(message = "{user.email.invalid}")
    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookLoan> borrowedBooks = new ArrayList<>();

}

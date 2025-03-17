package com.romsa.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @NotBlank(message = "Title is required")
    private String title;

    @Size(min = 3, max = 100, message = "Author must be between 3 and 100 characters")
    @NotBlank(message = "Author is required")
    private String author;

    @NotNull(message = "Year is required")
    private int year;

    @NotNull(message = "Total copies is required")
    private int totalCopies;

}
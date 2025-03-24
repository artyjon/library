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

        @Size(min = 3, max = 100, message = "{book.title.size.error}")
        @NotBlank(message = "{book.title.blank}")
        private String title;

        @Size(min = 3, max = 100, message = "{book.author.size.error}")
        @NotBlank(message = "{book.author.blank}")
        private String author;

        @NotNull(message = "{book.year.null}")
        private int year;

        @NotNull(message = "{book.copies.null}")
        private int totalCopies;
    }
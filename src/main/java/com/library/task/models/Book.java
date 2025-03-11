package com.library.task.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 50, message = "Title must be less than 50 characters")
    private String title;
    @NotBlank(message = "Author is required")
    @Size(max = 50, message = "Author must be less than 50 characters")
    private String author;

    @NotNull(message = "Publication year is required")
    @Min(value = 1500, message = "Publication year must be a valid year")
    @Max(value = 2025, message = "Publication year must be a valid year")
    private Integer publicationYear;


    @Column(nullable = false)
    private boolean isDeleted = false;

    @NotBlank(message = "ISBN is required")
    @Size(max = 20, message = "ISBN must be less than 20 characters")
    private String isbn;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BorrowingRecord> borrowingRecords;





}
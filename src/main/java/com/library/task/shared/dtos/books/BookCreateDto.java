package com.library.task.shared.dtos.books;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class BookCreateDto {
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

    @NotBlank(message = "ISBN is required")
    @Size(max = 20, message = "ISBN must be less than 20 characters")
    private String isbn;


}
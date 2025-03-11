package com.library.task.shared.dtos.borrwoings;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingRecordDto {
    @NotNull(message = "Borrowing record id is required")
    private Long id;
    @NotNull(message = "Book ID is required")
    private Long bookId;
    @NotNull(message = "Patron ID is required")
    private Long patronId;

    @NotNull(message = "Borrowing date is required")
    private LocalDate borrowingDate;

    private LocalDate returnDate;


}
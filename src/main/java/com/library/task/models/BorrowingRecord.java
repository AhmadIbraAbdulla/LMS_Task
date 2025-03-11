package com.library.task.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_borrowingrecord_book", columnList = "book_id"),
        @Index(name = "idx_borrowingrecord_patron", columnList = "patron_id")
})
public class BorrowingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Book is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @NotNull(message = "Patron is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patron_id", nullable = false)
    private Patron patron;

    @NotNull(message = "Borrowing date is required")
    private LocalDate borrowingDate;

    private LocalDate returnDate;

    @AssertTrue(message = "Return date must be after borrowing date")
    public boolean isReturnDateValid() {
        return returnDate == null || !returnDate.isBefore(borrowingDate);
    }


}
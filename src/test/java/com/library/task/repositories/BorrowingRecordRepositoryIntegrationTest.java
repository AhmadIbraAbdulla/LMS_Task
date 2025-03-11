package com.library.task.repositories;

import com.library.task.models.BorrowingRecord;
import com.library.task.models.Book;
import com.library.task.models.Patron;
import com.library.task.infrastructure.repositories.BookRepository;
import com.library.task.infrastructure.repositories.BorrowingRecordRepository;
import com.library.task.infrastructure.repositories.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BorrowingRecordRepositoryIntegrationTest {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PatronRepository patronRepository;

    private Book book;
    private Patron patron;
    private BorrowingRecord borrowingRecord;

    @BeforeEach
    public void setUp() {
        // Create and save a Book
        book = new Book();
        book.setTitle("Sample Book");
        book.setAuthor("Sample Author");
        book.setPublicationYear(2023);
        book.setIsbn("1234567890");
        bookRepository.save(book);

        // Create and save a Patron
        patron = new Patron();
        patron.setName("Sample Patron");
        patron.setContactInformation("sample@patron.com");
        patronRepository.save(patron);

        // Create a BorrowingRecord
        borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowingDate(LocalDate.now());
        borrowingRecord.setReturnDate(null);

        // Save the BorrowingRecord
        borrowingRecordRepository.save(borrowingRecord);
    }

    @Test
    public void testFindByBookIdAndPatronIdAndReturnDateIsNull() {
        Optional<BorrowingRecord> foundRecord = borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(book.getId(), patron.getId());

        assertTrue(foundRecord.isPresent());
        assertEquals(borrowingRecord.getId(), foundRecord.get().getId());
    }

    @Test
    public void testFindByBookIdAndPatronIdAndReturnDateIsNull_NotFound() {
        Optional<BorrowingRecord> foundRecord = borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(999L, 999L);

        assertFalse(foundRecord.isPresent());
    }
}
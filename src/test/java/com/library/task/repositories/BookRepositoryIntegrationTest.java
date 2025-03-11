package com.library.task.repositories;

import com.library.task.models.Book;
import com.library.task.infrastructure.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BookRepositoryIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    private Book testBook;

    @BeforeEach
    public void setUp() {
        testBook = new Book(null, "Test Book", "Test Author", 2021,false, "1234567890", null);
        bookRepository.save(testBook);
    }

    @Test
    public void testFindById() {
        Optional<Book> foundBook = bookRepository.findById(testBook.getId());
        assertTrue(foundBook.isPresent());
        assertEquals(testBook.getTitle(), foundBook.get().getTitle());
        assertEquals(testBook.getAuthor(), foundBook.get().getAuthor());
        assertEquals(testBook.getPublicationYear(), foundBook.get().getPublicationYear());
        assertEquals(testBook.getIsbn(), foundBook.get().getIsbn());
    }

    @Test
    public void testSave() {
        Book newBook = new Book(null, "New Book", "New Author", 2023,false, "0987654321", null);
        Book savedBook = bookRepository.save(newBook);

        assertNotNull(savedBook.getId());
        assertEquals(newBook.getTitle(), savedBook.getTitle());
        assertEquals(newBook.getAuthor(), savedBook.getAuthor());
        assertEquals(newBook.getPublicationYear(), savedBook.getPublicationYear());
        assertEquals(newBook.getIsbn(), savedBook.getIsbn());
    }

    @Test
    public void testDelete() {
        bookRepository.delete(testBook);
        Optional<Book> foundBook = bookRepository.findById(testBook.getId());
        assertFalse(foundBook.isPresent());
        assertEquals(0, bookRepository.count()); // Check that the count is now 0
    }

    @Test
    public void testUpdate() {
        testBook.setTitle("Updated Title");
        testBook.setAuthor("Updated Author");
        testBook.setPublicationYear(2022);
        bookRepository.save(testBook);
        Optional<Book> updatedBook = bookRepository.findById(testBook.getId());
        assertTrue(updatedBook.isPresent());
        assertEquals("Updated Title", updatedBook.get().getTitle());
        assertEquals("Updated Author", updatedBook.get().getAuthor());
        assertEquals(2022, updatedBook.get().getPublicationYear());
    }

    @Test
    public void testCountBooks() {
        long initialCount = bookRepository.count();
        Book anotherBook = new Book(null, "Another Book", "Another Author", 2023,false, "1122334455", null);
        bookRepository.save(anotherBook);
        assertEquals(initialCount + 1, bookRepository.count()); // Verify the count increased by 1
    }
}
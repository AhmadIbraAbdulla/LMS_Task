package com.library.task.services;

import com.library.task.shared.dtos.books.BookCreateDto;
import com.library.task.shared.dtos.books.BookDto;
import com.library.task.shared.dtos.books.BookUpdateDto;
import com.library.task.shared.exception.BadRequestException;
import com.library.task.shared.exception.ResourceNotFoundException;
import com.library.task.models.Book;
import com.library.task.infrastructure.repositories.BookRepository;
import com.library.task.service.book.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetBookById() {
        Long bookId = 1L;
        Book book = new Book(bookId, "Book One", "Author One", 2021,false, "1234567890", null);
        BookDto bookDto = new BookDto(bookId, "Book One", "Author One", 2021, "1234567890");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);

        BookDto result = bookService.getBookById(bookId);

        assertNotNull(result);
        assertEquals(bookDto, result);
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    public void testGetBookById_NotFound() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(bookId);
        });

        assertEquals("Book not found with id: " + bookId, exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    public void testAddBook() {
        BookCreateDto bookCreateDto = new BookCreateDto("Book One", "Author One", 2021, "1234567890");
        Book book = new Book(null, "Book One", "Author One", 2021,false ,"1234567890", null);
        Book savedBook = new Book(1L, "Book One", "Author One", 2021,false ,"1234567890", null);
        BookDto bookDto = new BookDto(1L, "Book One", "Author One", 2021, "1234567890");

        when(modelMapper.map(bookCreateDto, Book.class)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(modelMapper.map(savedBook, BookDto.class)).thenReturn(bookDto);

        BookDto result = bookService.addBook(bookCreateDto);

        assertNotNull(result);
        assertEquals(bookDto, result);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testUpdateBook() {
        Long bookId = 1L;
        BookUpdateDto newBookDto = new BookUpdateDto(bookId, "Updated Book", "Updated Author", 2022, "1234567890");
        Book existingBook = new Book(bookId, "Old Book", "Old Author", 2021,false, "1234567890", null);
        BookDto expectedBookDto = new BookDto(bookId, "Updated Book", "Updated Author", 2022, "1234567890");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(modelMapper.map(newBookDto, Book.class)).thenReturn(existingBook); // Map DTO to existing entity
        when(bookRepository.save(existingBook)).thenReturn(existingBook); // Save and return updated entity
        when(modelMapper.map(existingBook, BookDto.class)).thenReturn(expectedBookDto); // Map to DTO

        BookDto result = bookService.updateBook(bookId, newBookDto);

        // Assertions
        assertNotNull(result);
        assertEquals(expectedBookDto, result);
        assertEquals(bookId, result.getId());
        assertEquals("Updated Book", result.getTitle());
        assertEquals("Updated Author", result.getAuthor());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    public void testUpdateBook_IdMismatch() {
        Long bookId = 1L;
        BookUpdateDto bookDto = new BookUpdateDto(2L, "Updated Book", "Updated Author", 2022, "1234567890");

        Exception exception = assertThrows(BadRequestException.class, () -> {
            bookService.updateBook(bookId, bookDto);
        });

        assertEquals("ID in path and request body do not match", exception.getMessage());
        verify(bookRepository, times(0)).findById(anyLong());
    }

    @Test
    public void testDeleteBook() {
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);

        bookService.deleteBook(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    public void testDeleteBook_NotFound() {
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.deleteBook(bookId);
        });

        assertEquals("Book not found with id: " + bookId, exception.getMessage());
        verify(bookRepository, times(0)).deleteById(anyLong());
    }
}

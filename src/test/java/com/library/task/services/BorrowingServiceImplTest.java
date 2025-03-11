package com.library.task.services;

import com.library.task.shared.dtos.borrwoings.BorrowingRecordDto;
import com.library.task.shared.exception.ConflictException;
import com.library.task.shared.exception.ResourceNotFoundException;
import com.library.task.models.Book;
import com.library.task.models.BorrowingRecord;
import com.library.task.models.Patron;
import com.library.task.infrastructure.repositories.BookRepository;
import com.library.task.infrastructure.repositories.BorrowingRecordRepository;
import com.library.task.infrastructure.repositories.PatronRepository;
import com.library.task.service.borrowing.BorrowingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowingServiceImplTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BorrowingServiceImpl borrowingService;

    private Book book;
    private Patron patron;
    private BorrowingRecord borrowingRecord;
    private BorrowingRecordDto borrowingRecordDto;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Sample Book");

        patron = new Patron();
        patron.setId(1L);
        patron.setName("John Doe");

        borrowingRecord = new BorrowingRecord();
        borrowingRecord.setId(1L);
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowingDate(LocalDate.now());

        borrowingRecordDto = new BorrowingRecordDto();
        borrowingRecordDto.setId(1L);
        borrowingRecordDto.setBookId(1L);
        borrowingRecordDto.setPatronId(1L);
        borrowingRecordDto.setBorrowingDate(LocalDate.now());
    }

    @Test
    void borrowBook_Success() {

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L)).thenReturn(Optional.empty());
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);
        when(modelMapper.map(borrowingRecord, BorrowingRecordDto.class)).thenReturn(borrowingRecordDto);


        BorrowingRecordDto result = borrowingService.borrowBook(1L, 1L);


        assertNotNull(result);
        assertEquals(1L, result.getBookId());
        assertEquals(1L, result.getPatronId());
        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
    }

    @Test
    void borrowBook_BookNotFound() {

        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> borrowingService.borrowBook(1L, 1L));
    }

    @Test
    void borrowBook_PatronNotFound() {

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> borrowingService.borrowBook(1L, 1L));
    }

    @Test
    void borrowBook_ConflictException() {

        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L))
                .thenReturn(Optional.of(borrowingRecord));


        assertThrows(ConflictException.class, () -> borrowingService.borrowBook(1L, 1L));
    }







    @Test
    void returnBook_Success() {

        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L)).thenReturn(Optional.of(borrowingRecord));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);
        when(modelMapper.map(borrowingRecord, BorrowingRecordDto.class)).thenReturn(borrowingRecordDto);


        BorrowingRecordDto result = borrowingService.returnBook(1L, 1L);


        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
    }

    @Test
    void returnBook_NotFound() {

        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L)).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> borrowingService.returnBook(1L, 1L));
    }
}
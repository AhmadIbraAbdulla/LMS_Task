package com.library.task.controllers;

import com.library.task.api.controllers.BorrowingController;
import com.library.task.shared.dtos.borrwoings.BorrowingRecordDto;
import com.library.task.shared.exception.ResourceNotFoundException;
import com.library.task.service.borrowing.IBorrowingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BorrowingControllerTest {

    @InjectMocks
    private BorrowingController borrowingController;

    @Mock
    private IBorrowingService borrowingService;

    private BorrowingRecordDto borrowingRecordDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        borrowingRecordDto = new BorrowingRecordDto(1L, 1L, 1L, LocalDate.now(), null);
    }


    @Test
    public void testBorrowBook_Success() {
        when(borrowingService.borrowBook(1L, 1L)).thenReturn(borrowingRecordDto);

        ResponseEntity<BorrowingRecordDto> response = borrowingController.borrowBook(1L, 1L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(borrowingRecordDto, response.getBody());
        verify(borrowingService, times(1)).borrowBook(1L, 1L);
    }

    @Test
    public void testReturnBook_Success() {
        when(borrowingService.returnBook(1L, 1L)).thenReturn(borrowingRecordDto);

        ResponseEntity<BorrowingRecordDto> response = borrowingController.returnBook(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(borrowingRecordDto, response.getBody());
        verify(borrowingService, times(1)).returnBook(1L, 1L);
    }


    @Test
    public void testBorrowBook_BookNotFound() {
        when(borrowingService.borrowBook(1L, 1L)).thenThrow(new ResourceNotFoundException("Book not found"));

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingController.borrowBook(1L, 1L));
        assertEquals("Book not found", exception.getMessage());
        verify(borrowingService, times(1)).borrowBook(1L, 1L);
    }

    @Test
    public void testBorrowBook_PatronNotFound() {
        when(borrowingService.borrowBook(1L, 1L)).thenThrow(new ResourceNotFoundException("Patron not found"));

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingController.borrowBook(1L, 1L));
        assertEquals("Patron not found", exception.getMessage());
        verify(borrowingService, times(1)).borrowBook(1L, 1L);
    }

    @Test
    public void testReturnBook_NotFound() {
        when(borrowingService.returnBook(2L, 1L)).thenThrow(new ResourceNotFoundException("Borrowing record not found"));

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> borrowingController.returnBook(2L, 1L));
        assertEquals("Borrowing record not found", exception.getMessage());
        verify(borrowingService, times(1)).returnBook(2L, 1L);
    }

    @Test
    public void testReturnBook_BookNotBorrowed() {
        when(borrowingService.returnBook(1L, 1L)).thenThrow(new IllegalStateException("Book was not borrowed by this patron"));

        Exception exception = assertThrows(IllegalStateException.class, () -> borrowingController.returnBook(1L, 1L));
        assertEquals("Book was not borrowed by this patron", exception.getMessage());
        verify(borrowingService, times(1)).returnBook(1L, 1L);
    }


}
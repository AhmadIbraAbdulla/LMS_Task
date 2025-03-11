package com.library.task.api.controllers;

import com.library.task.shared.dtos.borrwoings.BorrowingRecordDto;
import com.library.task.service.borrowing.IBorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BorrowingController {

    private IBorrowingService borrowingService;

    public BorrowingController(IBorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecordDto> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        return new ResponseEntity<>(borrowingService.borrowBook(bookId, patronId), HttpStatus.CREATED);
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecordDto> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        return new ResponseEntity<>(borrowingService.returnBook(bookId, patronId), HttpStatus.OK);
    }
}

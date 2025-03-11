package com.library.task.service.borrowing;

import com.library.task.shared.dtos.borrwoings.BorrowingRecordDto;

import java.util.List;

public interface IBorrowingService {
    BorrowingRecordDto borrowBook(Long bookId, Long patronId);
    BorrowingRecordDto returnBook(Long bookId, Long patronId);

}

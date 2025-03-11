package com.library.task.service.borrowing;

import com.library.task.shared.dtos.borrwoings.BorrowingRecordDto;
import com.library.task.shared.exception.ConflictException;
import com.library.task.shared.exception.ResourceNotFoundException;
import com.library.task.models.Book;
import com.library.task.models.BorrowingRecord;
import com.library.task.models.Patron;
import com.library.task.infrastructure.repositories.BookRepository;
import com.library.task.infrastructure.repositories.BorrowingRecordRepository;
import com.library.task.infrastructure.repositories.PatronRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowingServiceImpl implements IBorrowingService {

    private BorrowingRecordRepository borrowingRecordRepository;

    private BookRepository bookRepository;

    private PatronRepository patronRepository;

    private ModelMapper modelMapper;

    public BorrowingServiceImpl(BorrowingRecordRepository borrowingRecordRepository, BookRepository bookRepository, PatronRepository patronRepository, ModelMapper modelMapper) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.modelMapper = modelMapper;
    }



    @Override
    @Transactional
    public BorrowingRecordDto borrowBook(Long bookId, Long patronId) {
        // Check if there's an existing borrowing record for the book and patron with no return date
        borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId)
                .ifPresent(record -> {
                    throw new ConflictException("You must return the book (ID: " + bookId + ") before borrowing it again.");
                });

        // Fetch the book and patron
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new ResourceNotFoundException("Patron not found with id: " + patronId));

        // Create a new borrowing record
        BorrowingRecord record = new BorrowingRecord();
        record.setBook(book);
        record.setPatron(patron);
        record.setBorrowingDate(LocalDate.now());

        // Save and return the new borrowing record
        return modelMapper.map(borrowingRecordRepository.save(record), BorrowingRecordDto.class);
    }


    @Override
    @Transactional
    public BorrowingRecordDto returnBook(Long bookId, Long patronId) {
        BorrowingRecord record = borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing record not found with book id: " + bookId + ", patron id: " + patronId + "and return date is null"));
        record.setReturnDate(LocalDate.now());
        return modelMapper.map(borrowingRecordRepository.save(record), BorrowingRecordDto.class);
    }
}

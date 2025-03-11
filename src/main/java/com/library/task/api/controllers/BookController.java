package com.library.task.api.controllers;

import com.library.task.shared.dtos.books.BookCreateDto;
import com.library.task.shared.dtos.books.BookDto;
import com.library.task.shared.dtos.books.BookUpdateDto;
import com.library.task.shared.dtos.shared.PageRequestDto;
import com.library.task.shared.dtos.shared.PageResponse;
import com.library.task.service.book.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private IBookService bookService;

    public BookController(IBookService bookService) {
        this.bookService = bookService;
    }



    @GetMapping
    public ResponseEntity<PageResponse<BookDto>> getBooksByPage(@ModelAttribute PageRequestDto pageRequestDto) {
        Sort sort = Sort.by(Sort.Direction.fromString(pageRequestDto.getSortDirection()), pageRequestDto.getSortBy());
        Pageable pageable = PageRequest.of(pageRequestDto.getPage(), pageRequestDto.getSize(), sort);

        PageResponse<BookDto> response = bookService.getBooksByPage(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.getBookById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BookDto> addBook(@RequestBody BookCreateDto bookCreateDto) {
        return new ResponseEntity<>(bookService.addBook(bookCreateDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody BookUpdateDto bookDto) {
        return new ResponseEntity<>(bookService.updateBook(id, bookDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
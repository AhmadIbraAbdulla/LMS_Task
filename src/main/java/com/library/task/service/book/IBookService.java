package com.library.task.service.book;

import com.library.task.shared.dtos.books.BookCreateDto;
import com.library.task.shared.dtos.books.BookDto;
import com.library.task.shared.dtos.books.BookUpdateDto;
import com.library.task.shared.dtos.shared.PageResponse;

import org.springframework.data.domain.Pageable;

public interface IBookService {
    PageResponse<BookDto> getBooksByPage(Pageable pageable);
    BookDto getBookById(Long id);
    BookDto addBook(BookCreateDto bookCreateDto);
    BookDto updateBook(Long id, BookUpdateDto bookUpdateDto);
    void deleteBook(Long id);
}

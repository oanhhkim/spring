package com.java.spring.books.service;

import com.java.spring.books.dto.request.BookRequest;
import com.java.spring.books.dto.response.BookResponse;
import com.java.spring.books.dto.response.PageResponse;
import com.java.spring.books.entity.Book;
import java.util.List;
import org.springframework.data.domain.Page;

public interface BookService {

  BookResponse create(BookRequest request);

  List<BookResponse> getAll();

  BookResponse getOneById(Long id);

  BookResponse update(BookRequest request, long id);

  void deleteById(long id);

//  Page<Book> getBookPagination(int pageNumber, int pageSize);

  PageResponse getBookPagination(int pageNumber, int pageSize, String sortBy);
}

package com.java.spring.books.service;

import com.java.spring.books.dto.request.BookRequest;
import com.java.spring.books.dto.response.BookResponse;
import java.util.List;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;


public interface BookService {

  BookResponse create(BookRequest request);

  List<BookResponse> getAll();

  BookResponse getOneById(Long id);

  BookResponse update(BookRequest request, long id);

  void deleteById(long id);

}

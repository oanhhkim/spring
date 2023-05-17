package com.java.spring.books.service;

import com.java.spring.books.dto.request.BookRequest;
import com.java.spring.books.dto.response.BookResponse;
import com.java.spring.books.entity.Book;
import java.util.List;


public interface BookService {

  BookResponse create(BookRequest request);

  List<Book> getAll();

  Book getOneById(Long id);

  Book update(Book book);

  void deleteById(String id);

}

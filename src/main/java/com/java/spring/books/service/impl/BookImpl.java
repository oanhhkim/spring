package com.java.spring.books.service.impl;

import com.java.spring.books.dto.request.BookRequest;
import com.java.spring.books.dto.response.BookResponse;
import com.java.spring.books.entity.Book;
import com.java.spring.books.repository.BookRepository;
import com.java.spring.books.service.BookService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class BookImpl implements BookService {

  @Autowired
  private BookRepository bookRepository;

  @Override
  public BookResponse create(BookRequest request) {
    // create book entity from request
    Book book = Book.from(request);
    // save book into db
    book = bookRepository.save(book);
    // create book response from book entity
    BookResponse response = new BookResponse();
    response.setId(book.getId());
    response.setTitle(book.getTitle());
    response.setAuthor(book.getAuthor());
    response.setCategory(book.getCategory());
    response.setPublisher(book.getPublisher());
    response.setPublishTime(book.getPublishTime());
    // return book response
    return response;
  }

  @Override
  public List<Book> getAll() {
    return null;
  }

  @Override
  public Book getOneById(Long id) {
    return null;
  }

  @Override
  public Book update(Book book) {
    return null;
  }

  @Override
  public void delete(Long id) {

  }
}

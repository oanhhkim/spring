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
  public void deleteById(long id) {
    bookRepository.deleteById(String.valueOf(id));
  }

  @Override
  public List<BookResponse> getAll() {
    List<Book> list = bookRepository.findAll();
    BookResponse response = new BookResponse();
    for (Book book : list) {
      response.setId(book.getId());
      response.setTitle(book.getTitle());
      response.setAuthor(book.getAuthor());
      response.setCategory(book.getCategory());
      response.setPublisher(book.getPublisher());
      response.setPublishTime(book.getPublishTime());
    }
    return (List<BookResponse>) response;
  }

  @Override
  public BookResponse getOneById(Long id) {
    Optional<Book> book = bookRepository.findById(String.valueOf(id));
    BookResponse response = new BookResponse();
    response.setId(book.get().getId());
    response.setTitle(book.get().getTitle());
    response.setAuthor(book.get().getAuthor());
    response.setCategory(book.get().getCategory());
    response.setPublisher(book.get().getPublisher());
    response.setPublishTime(book.get().getPublishTime());
    // return book response
    return response;

  }

  @Override
  public BookResponse update(BookRequest request, long id) {
    Optional<Book> book = bookRepository.findById(String.valueOf(id));
    Book bookUpdate = Book.from(request);
    BookResponse response = new BookResponse();
    bookUpdate.setId(book.get().getId());
    bookUpdate.setTitle(book.get().getTitle());
    bookUpdate.setAuthor(book.get().getAuthor());
    bookUpdate.setCategory(book.get().getCategory());
    bookUpdate.setPublisher(book.get().getPublisher());
    bookUpdate.setPublishTime(book.get().getPublishTime());
    // convert entity updateBook to response
    response.setId(bookUpdate.getId());
    response.setTitle(bookUpdate.getTitle());
    response.setAuthor(bookUpdate.getAuthor());
    response.setCategory(bookUpdate.getCategory());
    response.setPublisher(bookUpdate.getPublisher());
    response.setPublishTime(bookUpdate.getPublishTime());
    return response;
  }
}


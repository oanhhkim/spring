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
  public Book create(Book book) {
    return bookRepository.save(book);
  }

  @Override
  public Book create(BookRequest request) {
    Book book = new Book();
    book.setTitle(request.getTitle());
    book.setAuthor(request.getAuthor());
    book.setCategory(request.getCategory());
    book.setPublisher(request.getPublisher());
    book.setPublishTime(request.getPublishTime());
    return bookRepository.save(book);
  }

  @Override
  public List<Book> getAll() {
    return bookRepository.findAll();
  }

  @Override
  public Book getOneById(Long id) {
    Optional<Book> book = bookRepository.findById(id);
    return book.get();
  }

  @Override
  public Book update(Book book) {
    Book existBook = bookRepository.findById(book.getId()).get();
    existBook.setTitle(book.getTitle());
    existBook.setAuthor(book.getAuthor());
    existBook.setCategory(book.getCategory());
    existBook.setPublisher(book.getPublisher());
    existBook.setPublishTime(book.getPublishTime());
    return bookRepository.save(existBook);
  }

  @Override
  public void delete(Long id) {
    bookRepository.deleteById(id);
  }
}

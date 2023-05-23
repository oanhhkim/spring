package com.java.spring.books.service.impl;

import com.java.spring.books.dto.request.BookRequest;
import com.java.spring.books.dto.response.BookResponse;
import com.java.spring.books.dto.response.PageResponse;
import com.java.spring.books.entity.Book;
import com.java.spring.books.repository.BookRepository;
import com.java.spring.books.service.BookService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    bookRepository.deleteById(id);
  }

  @Override
  public PageResponse getBookPagination(int pageNumber, int pageSize) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    Page<Book> bookPage = bookRepository.findAll(pageable);
    PageResponse response = new PageResponse((List<Book>) bookPage.getContent(), bookPage.getNumber(),
        bookPage.getSize(), bookPage.getTotalPages());
    return response;
  }

  @Override
  public List<BookResponse> getAll() {
    List<Book> list = bookRepository.findAll();
    List<BookResponse> response = new ArrayList<>();
    for (Book book : list) {
      BookResponse bookResponse = new BookResponse();
      bookResponse.setId(book.getId());
      bookResponse.setTitle(book.getTitle());
      bookResponse.setAuthor(book.getAuthor());
      bookResponse.setCategory(book.getCategory());
      bookResponse.setPublisher(book.getPublisher());
      bookResponse.setPublishTime(book.getPublishTime());
      response.add(bookResponse);
    }
    return response;
  }

  @Override
  public BookResponse getOneById(Long id) {
    Optional<Book> book = bookRepository.findById(id);
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
    Optional<Book> optionalBook = bookRepository.findById(id);
    if (!optionalBook.isPresent()) {
      throw new RuntimeException();
    }
    Book book = optionalBook.get();
    book.setTitle(request.getTitle());
    book.setAuthor(request.getAuthor());
    book.setCategory(request.getCategory());
    book.setPublisher(request.getPublisher());
    book.setPublishTime(request.getPublishTime());
    BookResponse response = new BookResponse();
    response.setId(book.getId());
    response.setTitle(book.getTitle());
    response.setAuthor(book.getAuthor());
    response.setCategory(book.getCategory());
    response.setPublisher(book.getPublisher());
    response.setPublishTime(book.getPublishTime());
    return response;
  }
//  @Override
//  public Page<Book> getBookPagination(int pageNumber, int pageSize) {
//    Pageable page = PageRequest.of(pageNumber, pageSize);
//    return bookRepository.findAll(page);
//  }
}


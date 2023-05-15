package com.java.spring.books.controller;

import com.java.spring.books.dto.request.BookRequest;
import com.java.spring.books.dto.response.BookResponse;
import com.java.spring.books.entity.Book;
import com.java.spring.books.repository.BookRepository;
import com.java.spring.books.service.BookService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/books")

public class BookController {

  @Autowired
  private BookService bookService;
  @Autowired
  private BookRepository bookRepository;

  //  @PostMapping
//  public ResponseEntity<Book> create(@RequestBody Book book) {
//    Book save = bookService.create(book);
//    return new ResponseEntity<>(save, HttpStatus.CREATED);
//  }
  //TODO: convert entity to dto
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BookResponse create(@Validated @RequestBody BookRequest request) {
    bookService.create(request);
    return BookResponse.toBookRequest(book);
  }

  @GetMapping
  public ResponseEntity<List<Book>> getAll() {
    List<Book> books = bookService.getAll();
    return new ResponseEntity<>(books, HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<Book> getOneById(@PathVariable("id") Long id) {
    Book book = bookService.getOneById(id);
    return new ResponseEntity<>(book, HttpStatus.OK);
  }

  @PutMapping("{id}")
  public ResponseEntity<Book> update(@PathVariable("id") Long id, @RequestBody Book book) {
    Book existBook = bookService.getOneById(id);
    bookService.update(book);
    return new ResponseEntity<>(book, HttpStatus.OK);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Book> delete(@PathVariable("id") Long id) {
    bookService.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}

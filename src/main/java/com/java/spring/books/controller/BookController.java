package com.java.spring.books.controller;

import com.java.spring.books.entity.Book;
import com.java.spring.books.repository.IBookRepository;
import com.java.spring.books.service.IBookService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/books")

public class BookController {

  @Autowired
  private IBookService bookService;
  @Autowired
  private IBookRepository iBookRepository;

  @PostMapping
  public ResponseEntity<Book> create(@RequestBody Book book) {
    Book save = bookService.create(book);
    return new ResponseEntity<>(save, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<Book>> getAll() {
    List<Book> books = bookService.getAll();
    return new ResponseEntity<>(books, HttpStatus.OK);
  }
  @GetMapping("{id}")
  public ResponseEntity<Book> getOneById(@PathVariable("id") Long id){
    Book book = bookService.getOneById(id);
    return new ResponseEntity<>(book, HttpStatus.OK);
  }
  @PutMapping("{id}")
  public ResponseEntity<Book> update(@PathVariable("id") Long id,@RequestBody Book book){
    Book existBook = bookService.getOneById(id);
    existBook.setTitle(existBook.getTitle());
    existBook.setAuthor(existBook.getAuthor());
    existBook.setCategory(existBook.getCategory());
    existBook.setPublisher(existBook.getPublisher());
    existBook.setPublishTime(existBook.getPublishTime());
    return new ResponseEntity<>(book, HttpStatus.OK);
  }
  @DeleteMapping("{id}")
  public ResponseEntity<Book> delete(@PathVariable("id") Long id){
    bookService.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}

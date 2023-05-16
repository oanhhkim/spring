package com.java.spring.books.entity;

import com.java.spring.books.dto.request.BookRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;
  @Column(nullable = false)
  private String title;
  @Column(nullable = false)
  private String author;
  @Column
  private String category;
  @Column
  private String publisher;
  @Column
  private Long publishTime;

  public Book() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public Long getPublishTime() {
    return publishTime;
  }

  public void setPublishTime(Long publishTime) {
    this.publishTime = publishTime;
  }

  public static Book from(BookRequest request) {
    Book book = new Book();
    book.setTitle(request.getTitle());
    book.setAuthor(request.getAuthor());
    book.setCategory(request.getCategory());
    book.setPublisher(request.getPublisher());
    book.setPublishTime(request.getPublishTime());
    return book;
  }
}

package com.java.spring.books.dto.response;

public class BookResponse {

  private long id;
  private String title;
  private String author;
  private String category;
  private String publisher;
  private Long publishTime;

  public BookResponse() {
  }

  public BookResponse(long id, String title, String author, String category, String publisher,
      Long publishTime) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.category = category;
    this.publisher = publisher;
    this.publishTime = publishTime;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
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
}

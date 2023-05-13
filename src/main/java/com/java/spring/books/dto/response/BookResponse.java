package com.java.spring.books.dto.response;

import com.java.spring.books.entity.Book;
import lombok.Data;

@Data
public class BookResponse {

  private int id;
  private String title;
  private String author;
  private String category;
  private String publisher;
  private Long publishTime;

  public BookResponse from(Book book) {
    BookResponse response = new BookResponse();
    response.setTitle(book.getTitle());
    response.setAuthor(book.getAuthor());
    response.setPublisher(book.getPublisher());
    response.setCategory(book.getCategory());
    response.setPublishTime(book.getPublishTime());
    return response;

  }


}

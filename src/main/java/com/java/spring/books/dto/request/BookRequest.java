package com.java.spring.books.dto.request;

import lombok.Data;

@Data
public class BookRequest {

  private int id;
  private String title;
  private String author;
  private String category;
  private String publisher;
  private Long publishTime;

}

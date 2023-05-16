package com.java.spring.books.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookRequest {

  private String title;
  private String author;
  private String category;
  private String publisher;
  private Long publishTime;

}

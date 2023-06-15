package com.java.spring.books.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {

  private long id;
  private String title;
  private String author;
  private String category;
  private String publisher;
  private Long publishTime;

}

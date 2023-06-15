package com.java.spring.books.dto.response;

import com.java.spring.books.entity.Book;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class PageResponse {

  //content
  //pageNumber
  //pageSize
  //totalPage
  private List<Book> content;
  private int pageNumber;
  private int pageSize;
  private int totalPages;


}

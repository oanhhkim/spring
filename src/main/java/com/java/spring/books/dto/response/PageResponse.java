package com.java.spring.books.dto.response;

import com.java.spring.books.entity.Book;
import java.util.List;

public class PageResponse {

  //content
  //pageNumber
  //pageSize
  //totalPage
  private List<Book> content;
  private int pageNumber;
  private int pageSize;
  private int totalPages;

  public PageResponse() {
  }

  public PageResponse(List<Book> content, int pageNumber, int pageSize, int totalPages) {
    this.content = content;
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.totalPages = totalPages;
  }

  public List<Book> getContent() {
    return content;
  }

  public void setContent(List<Book> content) {
    this.content = content;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }
}

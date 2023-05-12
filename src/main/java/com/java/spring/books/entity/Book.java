package com.java.spring.books.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "books")
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)

  private Long id;
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
}

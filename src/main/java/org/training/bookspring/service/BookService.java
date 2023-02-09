package org.training.bookspring.service;

import org.training.bookspring.model.Book;

import java.util.List;

public interface BookService {
    //add function
    public Book add(Book book);
    //update function
    public Book update(String id, Book book);
    //delete function
    public boolean delete(String id);
    //read function - get list of Book
    public List<Book> getAll();
}

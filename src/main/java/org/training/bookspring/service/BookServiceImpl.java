package org.training.bookspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.training.bookspring.model.Book;
import org.training.bookspring.repository.BookRepository;

import java.util.List;
@Service
public class BookServiceImpl implements IBookService{
    @Autowired
    private BookRepository bookRepository;
    @Override
    public Book add(Book book) {
        if(book!=null){
            return bookRepository.save(book);
        }
        return null;
    }

    @Override
    public Book update(String id, Book book) {
        if(book!=null){
            Book book1 = bookRepository.getById(id);
            if(book1!=null){
                book1.setPublicationDate(book.getPublicationDate());
                book1.setTitle(book.getTitle());
                book1.setDescription(book.getDescription());
                book1.setAuthor(book.getAuthor());
                book1.setNumberOfPage(book.getNumberOfPage());
            }
            return bookRepository.save(book);
        }
        return null;
    }

    @Override
    public boolean delete(String id) {
        if(id!="1"){
            Book book = bookRepository.getById(id);
            if(book!=null){
                bookRepository.delete(book);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }
}

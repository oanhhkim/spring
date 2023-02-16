package org.training.bookspring.controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.training.bookspring.model.Book;
import org.training.bookspring.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
public class BookController {
    @Autowired
    private BookService iBookService;
    @GetMapping("/")

    @PostMapping
    public Book add(@RequestBody Book book){
        return iBookService.add(book);
    }
    @PutMapping
    public Book update(@RequestParam("id") String id, @RequestBody Book book){
        return iBookService.update(id, book);
    }
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") String id){
        return iBookService.delete(id);
    }

    @GetMapping
    public List<Book> getAll(){
        return iBookService.getAll();
    }
}

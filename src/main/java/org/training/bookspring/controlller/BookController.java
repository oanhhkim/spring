package org.training.bookspring.controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.training.bookspring.model.Book;
import org.training.bookspring.service.IBookService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
public class BookController {
    @Autowired
    private IBookService iBookService;
    @GetMapping("/")
    public String test(){
        return "check";
    }

    @PostMapping("/add")
    public Book add(@RequestBody Book book){
        return iBookService.add(book);
    }
    @PutMapping("/update")
    public Book update(@RequestParam("id") String id, @RequestBody Book book){
        return iBookService.update(id, book);
    }
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable("id") String id){
        return iBookService.delete(id);
    }

    @GetMapping("/list")
    public List<Book> getAll(){
        return iBookService.getAll();
    }
}

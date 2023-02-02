package org.training.bookspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.training.bookspring.model.Book;
@Repository
public interface BookRepository extends JpaRepository<Book,String> {
}

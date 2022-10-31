package com.example.jpa.bookmanager.repository;

import com.example.jpa.bookmanager.domain.BookAndAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookAndAuthorRepository extends JpaRepository<BookAndAuthor, Long> {
}

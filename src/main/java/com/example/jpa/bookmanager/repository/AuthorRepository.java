package com.example.jpa.bookmanager.repository;

import com.example.jpa.bookmanager.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author,Long> {
}

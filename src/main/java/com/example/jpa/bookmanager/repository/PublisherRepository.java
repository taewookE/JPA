package com.example.jpa.bookmanager.repository;

import com.example.jpa.bookmanager.domain.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher,Long> {
}

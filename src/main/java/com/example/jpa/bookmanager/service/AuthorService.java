package com.example.jpa.bookmanager.service;

import com.example.jpa.bookmanager.domain.Author;
import com.example.jpa.bookmanager.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional(propagation= Propagation.NESTED)
//    @Transactional(propagation= Propagation.REQUIRES_NEW)
//    @Transactional(propagation= Propagation.REQUIRES)
    public void putAuthor(){
        Author author = new Author();
        author.setName("태욱");

        authorRepository.save(author);

        throw new RuntimeException("오류가 발생하였습니다. transaction은 어떻게 될까?");


    }
}

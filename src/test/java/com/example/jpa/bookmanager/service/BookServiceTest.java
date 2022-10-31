package com.example.jpa.bookmanager.service;

import com.example.jpa.bookmanager.domain.Book;
import com.example.jpa.bookmanager.repository.AuthorRepository;
import com.example.jpa.bookmanager.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;


    //anti pattern임... try-catch에서 익셉션을 다 먹어버리게 됨.
    /*TODO : 에러가 발생하면 rollback 되서 db반영이 되지 않게 됨.
             runtimeException은 반영 내역이 rollback됨. (unchecked Exception)
             checkedException : Exception 객체를 활용한 예외. >> rollback이 되지않음. 개발자가 모든 예외에 대한 처리를 다하여야한다.
             왠만하면 uncheckedException을 사용하게 편리할것 같다....
             Java내부에서 runtimeException 가 Error만 자동으로 rollback을 해준다.    * */

    /*TODO: class 내부에서 rollback되는 익셉션일 발생했으나, 다른 메소드에서 (put)는 @Transaction이 무효화되는것처럼 동작한다.
    *       rollback X (실수를 많이 하는
    * */
    @Test
    void 트랜잭션_테스트(){
        try{
            bookService.putBookAndAuthor();
//            bookService.put();
        }catch (RuntimeException e){
            System.out.println(">>>" + e.getMessage());
        }

        /*TODO : transaction이 완료되는 시점에 commit 을 하게 되고 db에 반영하게 된다.*/
        System.out.println("books: "+ bookRepository.findAll());
        System.out.println("author: " + authorRepository.findAll());
    }

    @Test
    void isolationTest(){
        Book book = new Book();
        book.setName("wook's 책");

        bookRepository.save(book);

        bookService.get(1L);
        System.out.println(">>> " + bookRepository.findAll());

    }

    @Test
    void converterErrorTest(){
        bookService.getAll();
        //실제로 log는 logger를 이용한다.
        bookRepository.findAll().forEach(System.out::println);

    }

}
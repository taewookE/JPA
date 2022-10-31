package com.example.jpa.bookmanager.repository;

import com.example.jpa.bookmanager.domain.Book;
import com.example.jpa.bookmanager.domain.BookReviewInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookReviewInfoRepositoryTest {

    @Autowired
    private BookReviewInfoRepository bookReviewInfoRepository;

    @Autowired
    private BookRepository bookRepository;


    @Test
    void 리뷰인포테스트(){
        BookReviewInfo bookReviewInfo = new BookReviewInfo();
//        bookReviewInfo.setBookId(1L);
        bookReviewInfo.setAverageReviewScore(4.5f);
        bookReviewInfo.setReviewCount(2);
        bookReviewInfo.setBook(givenBook());

        bookReviewInfoRepository.save(bookReviewInfo);

        System.out.println(">>>" + bookReviewInfoRepository.findAll());
    }


    /*TODO:     @GeneratedValue(strategy = GenerationType.IDENTITY)의 경우
                mysql, maria DB처럼 각각의 테이블의 ID값을 auto-increment함으로써,
                별도 sequence를 가지도록 처리가 됨.

     */

    @Test
    void 연관관계테스트_RDB_Relation(){

//        givenBook();
//        givenBookReviewInfo();
//
//        Book result = bookRepository.findById(
//                bookReviewInfoRepository
//                        .findById(1L)
//                        .orElseThrow(RuntimeException::new)
//                        .getBookId()
//        ).orElseThrow(RuntimeException::new);
//
//        System.out.println("result :" + result);
    }

    @Test
    void JPA_relation_테스트(){
        givenBookReviewInfo();

        Book result = bookReviewInfoRepository
                .findById(1L)
                .orElseThrow(RuntimeException::new)
                .getBook();
        System.out.println("result >> :" + result);


        BookReviewInfo result2 = bookRepository
                .findById(1L)
                .orElseThrow(RuntimeException::new)
                .getBookReviewInfo();

        System.out.println(result2);
    }


    /*TODO: 별도의 생성로직을 만들어서 테스트에 사용해도 무방함.*/
    private Book givenBook(){
        Book book = new Book();
        book.setName("JPA book");
        book.setAuthorId(1L);
//        book.setPublisherId(1L);

        //TODO: entity.save는 저장된 entity를 바로 return 해주도록 되어있음.
        return bookRepository.save(book);


//        System.out.println(">>> :" + bookRepository.findAll());
    }

    private void givenBookReviewInfo(){
        BookReviewInfo bookReviewInfo = new BookReviewInfo();
//        bookReviewInfo.setBookId(1L);
        bookReviewInfo.setBook(givenBook());
        bookReviewInfo.setAverageReviewScore(4.5f);
        bookReviewInfo.setReviewCount(2);

        bookReviewInfoRepository.save(bookReviewInfo);

        System.out.println(">>> :" + bookReviewInfoRepository.findAll());


    }


}
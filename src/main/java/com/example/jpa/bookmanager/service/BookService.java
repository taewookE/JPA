package com.example.jpa.bookmanager.service;

import com.example.jpa.bookmanager.domain.Author;
import com.example.jpa.bookmanager.domain.Book;
import com.example.jpa.bookmanager.repository.AuthorRepository;
import com.example.jpa.bookmanager.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    private final AuthorService authorService;

    public void put(){
        this.putBookAndAuthor();
    }


    /*TODO : @Transactional이 두가지 종류의 어노테이션을 제공한다.
    *       트랜잭션은 All or Not 이다.
    *       checked Exception에도 rollback을 하기 위해서는 RollbackFor 옵션을 통해서 Exception을 설정해준다.
    *  */

    /*TODO: JPA transaction은 중요하기 때문에 반복 학습을 할것.*/
//    @Transactional(rollbackFor = Exception.class)
    @Transactional(propagation = Propagation.REQUIRED)
    public void putBookAndAuthor() {
        Book book = new Book();
        book.setName("wook's Book");

        bookRepository.save(book);

        //REQUIRED 전파 테스트. 한쪽에서만 에러가 발생하더라도 둘다 rollback됨 (둘다 Required인경우 )
        try {
            authorService.putAuthor();
        }catch (RuntimeException e){}
//        throw new RuntimeException("오류가 발생하였습니다. transaction은 어떻게 될까?");

//        Author author = new Author();
//        author.setName("taewook");
//        authorRepository.save(author);


        //전파테스트를 위해 Author별도 클래스 지정

//        throw new RuntimeException("오류가 나서 DB commit 발생하지 않음");
//        throw new Exception("오류가 나서 DB commit 발생하지 않음");

    }

//    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
//    @Transactional(isolation = Isolation.READ_COMMITTED)
//    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void get(Long id){
        System.out.println(">>> " + bookRepository.findById(id));
        System.out.println(">>> " + bookRepository.findAll());

        System.out.println(">>> " + bookRepository.findById(id));
        System.out.println(">>> " + bookRepository.findAll());

        //TODO: REPEATBLE_READ 단계에서 팬텀리드 테스트를 위해서 nativeQuery를 이용하여 테스트.
        bookRepository.update();
    }

    @Transactional
    public List<Book> getAll(){
        List<Book> books = bookRepository.findAll();
        books.forEach(System.out::println);
        return books;
    }
}

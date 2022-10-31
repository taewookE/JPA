package com.example.jpa.bookmanager.repository;

import com.example.jpa.bookmanager.domain.Book;
import com.example.jpa.bookmanager.domain.Publisher;
import com.example.jpa.bookmanager.domain.Review;
import com.example.jpa.bookmanager.domain.User;
import com.example.jpa.bookmanager.repository.dto.BookStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void bookTest(){
        Book book = new Book();
        book.setName("spring jpa");
//        book.setAuthor("taewook");

        book.setAuthorId(1L);
//        book.setPublisherId(1L);

        bookRepository.save(book);

        System.out.println(bookRepository.findAll());

    }

    @Test
    @Transactional
    void bookRelationTest(){
        givenBookAndReview();

        //일반적으로는 인증정보를 통해서 값을 가져옴.
        User user = userRepository.findByEmail("wook@gmail.com");

        System.out.println("Review : "+ user.getReviews());
        System.out.println("Book :" + user.getReviews().get(0).getBook());
        System.out.println("Publisher :" + user.getReviews().get(0).getBook().getPublisher());
    }

    /*TODO :cascade TEST
    *
    * */
    @Test
//    @Transactional
    void bookCascadeTest(){
        Book book = new Book();
        book.setName("태욱 책");
//        bookRepository.save(book);

        Publisher publisher = new Publisher();
        publisher.setName("욱 출판사");
//        publisherRepository.save(publisher);

        book.setPublisher(publisher);
        bookRepository.save(book);

        //TODO : callByReference 때문에 적상동작한다. 보통은 set을 통해 셋팅하는게 좋다. 가독성을 위해서 메서드를 추가해주는것이 좋긴하다.
//        publisher.getBooks().add(book);
//        publisher.addBook(book);
//        publisherRepository.save(publisher);

        System.out.println("books : " + bookRepository.findAll());
        System.out.println("publishers : " + publisherRepository.findAll());

        //TODO: cascadeType을 persist만 설정하면 아래 동작이 영속성 처리가 안됨.
        Book book1 = bookRepository.findById(1L).get();
        book1.getPublisher().setName("슬로우 욱");

        bookRepository.save(book1);
        System.out.println("publisher : "+ publisherRepository.findAll());

        Book book2 = bookRepository.findById(1L).get();
//        bookRepository.delete(book2);
//        bookRepository.deleteById(1L);

        /*TODO: relation 제거 테스트 , 위의 delete book2 제거*/
        Book book3 = bookRepository.findById(1L).get();
        book3.setPublisher(null);

        bookRepository.save(book3);


        //연관관계를한번에 없애긴위해서는 cascadeType 지정을 잘 설정해주면 된다. CascadeType.ALL을 처음에는 편하게 사용할 수있긴하다.
        System.out.println("books : " + bookRepository.findAll());
        System.out.println("publishers : " + publisherRepository.findAll());
        System.out.println("book3 publisher "+ bookRepository.findById(1L).get().getPublisher());

    }

    @Test
    void BookRemoveCascadeTest(){
        /*TODO: 2개의 연관관계를 가지고 있는 상태에서 cascade를 설정한 상태에서 삭제하면 publisher가 어떻게 동작하는지 테스트*/
        bookRepository.deleteById(1L);

        System.out.println("books : " + bookRepository.findAll());
        System.out.println("publisher: " + publisherRepository.findAll());

        bookRepository.findAll().forEach(book -> System.out.println(book.getPublisher()));
    }

    /*TODO: softDelete를 위해서 entity의 속성을 추가주는 경우 일일히 변경되는 별도의 메소드를 추가할 수 없게된다.
    *       해당 사항을 피하기 위해서 @Where 어노테이션을 사용하게 된다. ( Entity에 선언 )
    * */
    @Test
    void softDelete(){
        bookRepository.findAll().forEach(System.out::println);
        System.out.println(bookRepository.findById(3L));

//        bookRepository.findByCategoryIsNull().forEach(System.out::println);
//        bookRepository.findAllByDeletedFalse().forEach(System.out::println);
//        bookRepository.findByCategoryIsNullAndDeletedFalse().forEach(System.out::println);
    }

    private void givenBookAndReview(){
        givenReview(givenUser(),givenBook(givenPublisher()));
    }
    private User givenUser(){
        return userRepository.findByEmail("wook@gmail.com");
    }
    private Book givenBook(Publisher publisher){
        Book book = new Book();
        book.setName("JPA TEST");
        book.setPublisher(publisher);

        return bookRepository.save(book);
    }

    private Publisher givenPublisher(){
        Publisher publisher = new Publisher();
        publisher.setName("wook-publisher");

        return publisherRepository.save(publisher);
    }

    private void givenReview(User user, Book book){
        Review review = new Review();
        review.setTitle("내 인생을 바꾼책");
        review.setContent("너무 재밌어요");
        review.setScore(5.0f);
        review.setUser(user);
        review.setBook(book);

        reviewRepository.save(review);
    }


    /*TODO: createdAt,updatedAt이 null임
    *       Data.sql은 jpa auditing Listener를 타지 않아 찍히지 않게 된다.
    *       - 이를 해결 할 수 있는 방법은 2가지가 존재
    *          1. data.sql을 통한 테스트 시, data.sql에 직접 column을 추가해서 insert 해준다. > 하지만 새로운 테스트 케이스가 생길떄마다 createdAt, updatedAt을 추가해주어야한다.
    *          2. @Column 어노테이션의 columnDefinition을 이용한다.
    * */
    @Test
    void 쿼리테스트(){
        bookRepository.findAll().forEach(System.out::println);
        System.out.println(bookRepository.findByCategoryIsNullAndNameEqualsAndCreatedAtGreaterThanEqualAndUpdatedAtGreaterThanEqual(
                "JPA book",
                LocalDateTime.now().minusDays(1L),
                LocalDateTime.now().minusDays(1L)));

        //JPQL
        System.out.println("findByNameRecently : " +
                bookRepository.findByNameRecently(
                        "JPA book",
                        LocalDateTime.now().minusDays(1L),
                        LocalDateTime.now().minusDays(1L)
                ));


        System.out.println(bookRepository.findBookNameAndCategory());
//        bookRepository.findBookNameAndCategory().forEach(tuple -> { System.out.println(tuple.get(0) + " : " + tuple.get(1));});
        bookRepository.findBookNameAndCategory().forEach(b -> { System.out.println(b.getName() + " : " + b.getCategory());});

        bookRepository.findBookNameAndCategory(PageRequest.of(1,1)).forEach(
                bookNameAndCategory -> System.out.println(bookNameAndCategory.getName() + " : " + bookNameAndCategory.getCategory()));

        bookRepository.findBookNameAndCategory(PageRequest.of(0,1)).forEach(
                bookNameAndCategory -> System.out.println(bookNameAndCategory.getName() + " : " + bookNameAndCategory.getCategory()));
    }

    @Test
    void nativeQueryTest(){
//        bookRepository.findAll().forEach(System.out::println);
//        bookRepository.findAllCustom().forEach(System.out::println);

        //대량의 데이터 migration
        //deleteAllinBatch 같은건 성능이 잘나오지만, updates의 경우는 아래 경우 처럼 id를 각각 조회해서 update하기 때문에 성능이 떨어진다.
        List<Book> books = bookRepository.findAll();

        for(Book book: books){
            book.setCategory("IT 서적");
        }
        bookRepository.saveAll(books);
        System.out.println(bookRepository.findAll());


        System.out.println("affected rows:" + bookRepository.updateCategories());
        System.out.println(bookRepository.findAllCustom());
    }

    /*TODO: 일반적으로 제공하지 않은 query를 실행하기 위해서 nativeQuery를 사용한다.*/
    @Test
    void nativeQueryTest2(){
        System.out.println(bookRepository.showTables());
    }

    @Test
    void converterTest(){
        bookRepository.findAll().forEach(System.out::println); //숫자 코드 값으로 생기게됨

        Book book = new Book();
        book.setName("ConverterTest Book");
        book.setStatus(new BookStatus(200));

        bookRepository.save(book);
        //JPA로는 convter의 동작을 제대로 확인할 수 없으므로, nativeQuery를 사용한다.
        /*TODO: [4, 2022-10-29 19:59:18.15273, 2022-10-29 19:59:18.15273, null, null, false, ConverterTest Book, 200, null]
                실제 DB에 code 값 200으로 저장되는것을 확인해 볼 수 있다.

         * */
        System.out.println(bookRepository.findById(4L));
        System.out.println(bookRepository.findRawRecord().values());
    }
}
package com.example.jpa.bookmanager.repository;

import com.example.jpa.bookmanager.domain.Book;
import com.example.jpa.bookmanager.repository.dto.BookNameAndCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface BookRepository extends JpaRepository<Book,Long> {
    @Modifying
    @Query(value = "update book set category='none'",nativeQuery = true)
    void update();

    List<Book> findByCategoryIsNull();

    //soft delete를 위해 별도 함수를 만드는 수고를 해야할 수 있다.
    List<Book> findAllByDeletedFalse();
    List<Book> findByCategoryIsNullAndDeletedFalse();

    List<Book> findByCategoryIsNullAndNameEqualsAndCreatedAtGreaterThanEqualAndUpdatedAtGreaterThanEqual(String name, LocalDateTime createdAt, LocalDateTime updatedAt);

    /*TODO: JPQL을 통한 처리 진행. @Query사용 (JPQL) , queryMethod와 동일한 조건으로 처리된다.
    *       그래서 @where 절의 조건도 적용이된다.
    * */
//    @Query(value = "select b from Book b "
//            + "where name = ?1 and createdAt >= ?2 and updatedAt >= ?3 and category is null")
//    List<Book> findByNameRecently(String name, LocalDateTime createdAt, LocalDateTime updatedAt);

    @Query(value = "select b from Book b "
            + "where name = :name and createdAt >= :createdAt and updatedAt >= :updatedAt and category is null")
    List<Book> findByNameRecently(
            @Param("name") String name,
            @Param("createdAt")LocalDateTime createdAt,
            @Param("updatedAt") LocalDateTime updatedAt);

//    @Query(value = "select b.name as name, b.category as category from Book b")
//    List<BookNameAndCategory> findBookNameAndCategory();
//
//    @Query(value = "select b.name as name, b.category as category from Book b")
//    Page<BookNameAndCategory> findBookNameAndCategory(Pageable pageable);

    @Query(value = "select new com.example.jpa.bookmanager.repository.dto.BookNameAndCategory(b.name,b.category) from Book b")
    List<BookNameAndCategory> findBookNameAndCategory();
    @Query(value = "select new com.example.jpa.bookmanager.repository.dto.BookNameAndCategory(b.name,b.category) from Book b")
    Page<BookNameAndCategory> findBookNameAndCategory(Pageable pageable);

    /*JPQL과는 다르게 entity 속성 값을 사용하지 못하며, value에 적어두었던 query만 실행된다. (DB적용되는 SQL이 적용되고, dialect를 사용하지 않아
    특정 DB에 맞추어서 query를 작성해서, DB에 맞추어 쿼리를 작성해주는 JPA의 장점을 못살릴 수 있으나, 운영환경에서는 DB가 변경되는 상황은 잘 없어서 nativeQuery를 사용하는 것에 부담은 없다.*/
    @Query(value = "select * from Book", nativeQuery = true)
    List<Book> findAllCustom();

    /*TODO: updateQuery 적용시 @Modifying을 적용시켜주고, update시 transaction이 필요한 경우도 존재해서 @Transaction을 적용해서
    *       Transaction을 묶어주어야한다.
    *       또한, @Where 적용이 안되므로 nativeQuery선언시에는 쿼리를 잘 보고 짜야한다.
    *       interface에는 @transactional을 추천하진 않지만 Repository에서 적용하든 일관적으로 처리하는게 중요.
    *
    * */
    @Modifying
    @Transactional
    @Query(value = "update book set category = 'IT서적' ", nativeQuery = true)
    int updateCategories();

    @Query(value = "show tables", nativeQuery = true)
    List<String> showTables();

    /*TODO: 실제 DB Query값을 확인하기 위해서 nativeQuery를 사용하기 도 함*/
    @Query(value = "select * from book order by id desc limit 1", nativeQuery = true)
    Map<String,Object> findRawRecord();

}

package com.example.jpa.bookmanager.repository;

import com.example.jpa.bookmanager.domain.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /*TODO : JPQL을 이용한 N+1 해결 방법 #1
    *       NativeQuery가 아니므로 Entity의 속성으로 query를 작성하게 된다.
    * */
    @Query("select r from Review r join fetch r.comments")
    List<Review> findAllyByFetchJoin();




    /*TODO: @EntityGraph를 이용한 해결 방법*/
    @EntityGraph(attributePaths = "comments")
    @Query("select r from Review r")
    List<Review> findAllByEntityGraph();

    @EntityGraph(attributePaths = "comments")
    List<Review> findAll();
}

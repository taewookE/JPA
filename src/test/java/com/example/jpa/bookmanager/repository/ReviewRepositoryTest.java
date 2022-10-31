package com.example.jpa.bookmanager.repository;

import com.example.jpa.bookmanager.domain.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;


    @Test
    @Transactional
    void ReviewTest(){
        List<Review> reviews = reviewRepository.findAll();
//        System.out.println(reviews);


        //TODO: N+1을 해결하기 위한 customQuery 작성 + data.sql내 comment항목도 추가.
//        List<Review> reviews = reviewRepository.findAllyByFetchJoin();
//        List<Review> reviews = reviewRepository.findAllByEntityGraph();

//        System.out.println("전체를 가져옴");

//        /*TODO : LAZY와 EAGER의 차이점을 알수있는 테스트*/
//        System.out.println("reiview.get(0): " + reviews.get(0).getComments());
//        System.out.println("첫번째 리뷰의 코멘트들을 가져왔습니다.");
//
//        System.out.println(reviews.get(1).getComments());
//        System.out.println("두번쨰 리뷰의 코멘트들을 가져왔습니다.");

        reviews.forEach(System.out::println);

    }
}

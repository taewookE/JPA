package com.example.jpa.bookmanager.service;

import com.example.jpa.bookmanager.domain.User;
import com.example.jpa.bookmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class EntityManagerTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void entityManagerTest(){
        //userRepository.findAll()과 동일한 결과 값임.
        System.out.println(entityManager.createQuery("select u from User u").getResultList());
    }

    @Test
    void cacheFindTest(){

        /*TODO: @Transactional 을 적용하면 findById처리시 1차 cache가 동작하게 됨.
                  > 실제 query를 한번만 동작시키고 결과를 3번 처리하게 됨.
                1차 캐시는 MAP<ID,Entity> 형태로 관리됨. (1차 캐사 내 Entity가 존재하면 캐시처리 하고, 없으면 DB조회)
                JPA에서는 위에 보는것 처럼 Id 값으로 처리되기 때문에 ID관련된 query만 1차캐시가 동작하게 된다.
                아래 findByEmail은 1차캐시 동작이 안됨 ( query를 3번 질의하게 됨 )
                - JPA 내부적으로 id기반의 query실행이 많으므로, 성능향상을 노리는 용도로 사용됨.
                @Transaction 내에서는 최대한 DB I/O를 줄이려는 정책을 사용한다.

        * */
        System.out.println(userRepository.findById(1L).get());
        System.out.println(userRepository.findById(1L).get());
        System.out.println(userRepository.findById(1L).get());
        System.out.println(userRepository.findByEmail("wook@gmail.com"));
        System.out.println(userRepository.findByEmail("wook@gmail.com"));
        System.out.println(userRepository.findByEmail("wook@gmail.com"));

        userRepository.deleteById(1L);
    }

    //TODO: test상위에 @Transactional 때문에 하나의 트랜잭션으로 동작하게 됨
    @Test
    void cacheFindTest2(){
        User user = userRepository.findById(1L).get();
        user.setName("testttttt");
        userRepository.save(user);

        System.out.println("---------------------------");
        user.setEmail("test@test.com");
        userRepository.save(user);

        //TODO: flush에 대한 내용 한번 더 정리하기. (여기서는 rollback되는 상황에서 DB반영을 위해서 사용함)
        //TODO: 개발자가 원하는 타이밍에 영속성을 반영하기 위해서 flush를 사용하게 됨. ( 남발은 좋지 않음 ) , 영속성 cache의 장점을 활용하지 못하거나, 문제가 발생할 수 있다.
        userRepository.flush();

        //TODO: JPA 사용시, 초보자가 어려워하는 부분이 영속성 캐시부분이다... ( DB반영 시점이 컨트롤 안됨 )
    }


    @Test
    void cacheFindTest3(){
        User user = userRepository.findById(1L).get();
        user.setName("testttttt");
        userRepository.save(user);

        System.out.println("---------------------------");
        user.setEmail("test@test.com");
        userRepository.save(user);

        /*TODO: 영속성 컨텍스트와 실제 DB사이의 차이가 나는 구간이 발생한다.
        *       현재 @Transactional이 발생되어서, 쓰기지연이 발생한다.
        *       updateQuery가 없지만 1번 출력에서 영속성 캐시값으로 출력하게 되고, 이후(flush이후) update값이 처리되고 2번째는 update된
        *       db값이 호출 된다.
        *  */
        System.out.println(">>>> 1: " + userRepository.findById(1L).get());
//        userRepository.flush();
        System.out.println(">>>> 2: " + userRepository.findById(1L).get());


        /*TODO: 영속성캐시와 DB가 동기화 되는 시점
        *       1. flush() 메소드가 실행되는 경우 (개발자의 선언에 의한)
        *       2. Transaction이 종료 되는 경우- commit 시점 ( save 메소드에 @transactinal이 달려있고, 종료되면 auto flush 됨 )
        *       3. JPQL 쿼리가 실행되는 경우 ( auto Flush가 동작함 )
        * */
    }

    @Test
    void cacheFindTest4(){
        User user = userRepository.findById(1L).get();
        user.setName("testttttt");
        userRepository.save(user);

        System.out.println("---------------------------");
        user.setEmail("test@test.com");
        userRepository.save(user);

        /*TODO: 해당 상태에서는 data merge가 힘들기 때문에 영속성 컨텍스트에 있는 것을 모두 flush시키고 findall을 실행시키는게
                간단하기 때문에 내부적으로 JPQL 실행시 이런 로직으로 움직이게 된다.
         */
        System.out.println(userRepository.findAll()); //select * from user

    }
}

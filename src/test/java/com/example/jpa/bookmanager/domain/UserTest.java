package com.example.jpa.bookmanager.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

//springBoot 2.2 이후에는 jUnit5가 기본으로 지정됨.
class UserTest {

    @Test
    void test(){
        User user = new User();
        user.setEmail("taewook@gmail.com");
        user.setName("wook");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());



        User user2 = User.builder()
                        .name("wook2")
                        .email("test@test.com")
                        .build();

//        User user3 = new User(null,"wook","taewook@test.com",LocalDateTime.now(),LocalDateTime.now());

//        System.out.println(user3);
    }
}



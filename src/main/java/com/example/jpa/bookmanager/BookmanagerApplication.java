package com.example.jpa.bookmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;




/*TODO: Jpa Auditing listener를 사용하기 위해서는 @EnableJpaAuditing을 반드시 선언해주어야 정상동작한다!@!!@!@#!@#*/
@SpringBootApplication
//@EnableJpaAuditing
public class BookmanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookmanagerApplication.class, args);
    }

}

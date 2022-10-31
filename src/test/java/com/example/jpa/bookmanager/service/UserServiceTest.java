package com.example.jpa.bookmanager.service;

import com.example.jpa.bookmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void 서비스테스트(){
        //메소드 처리가 끝나면 내부 user정보는 필요가 없기때문에, gc가 처리해버려 null 로 결과가 찍히게 됨.
        userService.put();
        System.out.println(">>>"+ userRepository.findByEmail("newwook@gmail.com"));
    }
    @Test
    void em_서비스테스트(){
        //EntityManagr를 통해 managed 상태로 변경되어 영속성 관리가 되게 됨.
        userService.put2();
        System.out.println(">>>"+ userRepository.findByEmail("newwook@gmail.com"));
    }

    @Test
    void Detached_Merge_테스트(){
        userService.put3();
        System.out.println(">>>"+ userRepository.findByEmail("newwook@gmail.com"));
    }

    @Test
    void removeTest(){
        userService.remove();
        userRepository.findAll().forEach(System.out::println);

    }

}
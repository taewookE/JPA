package com.example.jpa.bookmanager.service;

import com.example.jpa.bookmanager.domain.User;
import com.example.jpa.bookmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    EntityManager entityManager;
    @Autowired
    UserRepository userRepository;

    @Transactional
    public void put(){
        User user = new User();
        user.setName("wook");
        user.setEmail("newwook@gmail.com");
    }
    @Transactional
    public void put2(){
        User user = new User();
        user.setName("wook");
        user.setEmail("newwook@gmail.com");

        /*TODO: .save를 하면 내부적으로 entityManager를 통햇 영속화 처리를 함.*/
//        userRepository.save(user);

        entityManager.persist(user);

        /*TODO: managed 상태가 되면 gc대상이 되는게 아니라 db와의 정합성을 맞추어줌.(w/ setter)
        *       save를 하지않아도, update query가 실행되게 됨...
        *       명시적으로 update를 하지 않아도... update를 해줌.(transaction 종료시)
        *         > 영속성 컨텍스트의 dirty check(변경) !!
        *       영송성컨텍스트의 entity 객체는 처음 load시 일종의 snapshot으로 가지고 있음.
        *       DB적용시 해당 시냅샷과 현재 entity 값을 비교해서 변경된 값이 있다면 코드가 없더라도
        *       DB에 반영시켜줌.
        *       *** dirtyCheck로 인해 성능이 떨어질수도 있음.
        * */
        user.setName("updateName");
    }
    @Transactional
    public void put3(){
        User user = new User();
        user.setName("wook");
        user.setEmail("newwook@gmail.com");

        entityManager.persist(user);
        entityManager.detach(user);

        //detach와 merge는 save에서 처리를 해주게 됨.
        user.setName("updateName");
        entityManager.merge(user);

        entityManager.flush();
        //clear method는 쓸일이 없을 것임.
        //굳이 사용한다면, flush를 통해서 db에 반영 이후 clear를 처리한다.
        entityManager.clear();
    }

    @Transactional
    public void remove(){
        User user1 = userRepository.findById(1L).get();
        entityManager.remove(user1);
    }
}

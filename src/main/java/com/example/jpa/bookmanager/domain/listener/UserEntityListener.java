package com.example.jpa.bookmanager.domain.listener;

import com.example.jpa.bookmanager.domain.User;
import com.example.jpa.bookmanager.domain.UserHistory;
import com.example.jpa.bookmanager.repository.UserHistoryRepository;
import com.example.jpa.bookmanager.support.BeanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

//@Component
public class UserEntityListener {

//    @Autowired
//    private UserHistoryRepository userHistoryRepository;

//    //TODO: Annotation을 두개 붙이면 같이 동작하게 된다.

    //pre를 쓰게되면 UserId가 안 쌓이게 될수 있게 된다.
//    @PrePersist
//    @PreUpdate

    @PostPersist
    @PostUpdate
    public void prePersistAndPreUpdate(Object o){
        UserHistoryRepository userHistoryRepository = BeanUtils.getBean(UserHistoryRepository.class);

        User user = (User) o;

        UserHistory userHistory = new UserHistory();
//        userHistory.setUserId(user.getId());
        userHistory.setName(user.getName());
        userHistory.setEmail(user.getEmail());

        //해당 객체를 선언하게 되면 ToString을 포함하게 되면 순환참조가 일어나면서 stackoverflow가 일어남.
        userHistory.setUser(user);
//        userHistory.setAddress(user.getAddress());
        userHistory.setHomeAddress(user.getHomeAddress());
        userHistory.setCompanyAddress(user.getCompanyAddress());

        userHistoryRepository.save(userHistory);

    }
}

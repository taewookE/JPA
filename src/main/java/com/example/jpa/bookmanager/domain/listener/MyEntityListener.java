package com.example.jpa.bookmanager.domain.listener;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

/*TODO : 해당 리스너의 경우는 기존 class내부에서 선언하듯이 this 객체를 사용하지 못하므로, Object형으로 받게 되고,
*        해당 object가 우리가 createdAt, updatedAt을 지정해놓은 객체인지 확인하기 위해서 instranceof 를 통해 확인 후
*        Object를 형변환하여 시간셋팅을 해주게된다.
* */
public class MyEntityListener {

    @PrePersist
    public void prePersist(Object o){
        if(o instanceof Auditable){
            ((Auditable) o).setCreatedAt(LocalDateTime.now());
            ((Auditable) o).setUpdatedAt(LocalDateTime.now());
        }
    }

    @PreUpdate
    public void preUpdate(Object o){
        if( o instanceof Auditable){
            ((Auditable) o).setUpdatedAt(LocalDateTime.now());
        }
    }

}

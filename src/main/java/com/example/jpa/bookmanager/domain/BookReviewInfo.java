package com.example.jpa.bookmanager.domain;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BookReviewInfo extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private Long bookId;

    //다른 entity를 직접 참조하지 못함으로 OneToOne과 같은 relation으로 처리해주어야함.
    /*TODO: @OneToOne은 optional 값이 true로 되어 있어 (null허용) 실제 query시 left outer join
    *       으로 처리가 되고, optional 값을 false로 처리하면 null이 허용되지 않아 inner join으로 query됨
    * */
//    @OneToOne
//    @OneToOne(optional = false, mappedBy = "bookReviewInfo") //TODO: mappedBy를 선언해주면 연관키를 해당테이블에서는 제거하게 됨. (method 설명은 꼭 읽어볼것)
    @OneToOne(optional = false)
    private Book book;
    //Wrappered class을 하기위해서는 null check를 해주어야 함  ex) Integer
    // primitive type을 사용하는것우 0을 셋팅하기 위해 아래처럼 사용한다.
    //
    private float averageReviewScore;
    private int reviewCount;
}

package com.example.jpa.bookmanager.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Review extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private float score;

    /*TODO: Many쪽에서 FK값을 가지게 된다.
    *       N+1에 집중하기 위해서 user, book 을
    *
    * */
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Book book;

    //TODO: Join을 정상적으로 설정하고, 중간테이블을 삭제하기위해서 @JoinColumn을 설정
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private List<Comment> comments;
}

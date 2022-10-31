package com.example.jpa.bookmanager.domain;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Publisher extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    /*TODO: JoinColumn을 통해서 JPA가 중간 테이블을 생성하는것을 방지한다.
    *       필요없어진 고어 객체를 삭제하려면 orphanRemoval = true를 삭제하면 된다.
    *       (고어 객체 ? : 연관관계가 삭제된 객체를 의미하는 듯 한다. ( setter를 이용해서 연관관계를 끊어진 단독 객체를 의미하는 듯 )
    *
    * */
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "publisher_id")
    @ToString.Exclude
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book){
        this.books.add(book);
    }
}

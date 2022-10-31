package com.example.jpa.bookmanager.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Author extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;

    /*TODO: ManyToMany에서는 중간테이블이 어쩔수 없이 생기게 된다. @JoinColumn을 통해서 처리하지 못함. FK값을 알고 있어야함
            아주 특별한 경우가 아니라면 @ManyToMany를 쓰지않고, 이를 피해가기 위해서 relation 설계를 다르게 하려고 노력함.
            특별한 Case :
            - User
            - user_products -> order entity로 설계 가 변경하게 된다.(맵핑 테이블 domain을 생성해서 oneToMany, manyToOne로 처리한다.)
            - Product


       .*/
//    @ManyToMany
    @OneToMany
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private List<BookAndAuthor> bookAndAuthors = new ArrayList<>();
//    private List<Book> books = new ArrayList<>();

//    public void addBook(Book book){
//        this.books.add(book);
//    }

//    public void addBook(Book... book){
//        Collections.addAll(this.books,book);
//    }

    public void addBookAndAuthors(BookAndAuthor... bookAndAuthors){
        Collections.addAll(this.bookAndAuthors, bookAndAuthors);
    }
}

package com.example.jpa.bookmanager.domain;

import com.example.jpa.bookmanager.domain.converter.BookStatusConverter;
import com.example.jpa.bookmanager.repository.dto.BookStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
//@EntityListeners(value = AuditingEntityListener.class)
//@DynamicUpdate
/*TODO: where 어노테이션을 통해서 query시 아래 조건을 설정해 softDelete를 활용할 수 있지만, 삭제된 값을 확인하고 싶을때는 추가로 어떻게 처리할 것인지 확인 해볼것.?
*       별도 entity를 생성해서 처리하게 되는것인지 ?
* */
@Where(clause = "deleted = false")
public class Book extends BaseEntity {
    /*TODO: 코드 주석은 clean Code에서 삭제를 하는걸 추천한다.
    *       요즘은 git에서 history 관리가 다 되기 때문에 삭제하는것이 올바른 방향이라고 한다.
    *
    * */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
//    private String author;

    private String category;
    private Long authorId;
//    private Long publisherId;
    /*TODO: 양쪽에서 oneToone을 선언해주면 select시 여러번의 left outer join을 실행시킴.
    *       양쪽 entity에서 모두 mappedBy 선언을 해주게 되면 Stackoverflow발생 하게 됨
    *       stackOverflow가 발생하는 이유 : Tostring이 순환참조를 일으킴.
    *       - relation은 단방향으로 걸거나 Tostring에서 제외한다.
    *
    * */
    @OneToOne(mappedBy = "book")
    @ToString.Exclude
    private BookReviewInfo bookReviewInfo;


    //OneToMany 에서는 중간컬럼 생성을 제거하기 위해서 JoinColumn을 선언해준다.
    @OneToMany
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    private List<Review> Reviews = new ArrayList<>();

    /*TODO: cascade option - book entity의 persist시 연관성있는 publisher도 함께 persist 전이가 일어나도록 설정*/
    @ManyToOne(cascade = {CascadeType.PERSIST , CascadeType.MERGE})
    @ToString.Exclude
    private Publisher publisher;

//    @ManyToMany
    /*TODO: 중간테이블인 BookAndAuthor를 설정하였기 때문에 아래와 같이 @OneToMany로 설정하게 되고, @JoinColumn을 설정한다.
            중간 테이블을 생성했기때문에 연관관계 정보도 BookAndAuthor Type으로 변경 한다.
    *       Author쪽도 동일
    * */
    @OneToMany
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    private List<BookAndAuthor> bookAndAuthors = new ArrayList<>();

    //softDelete관련 , primitive 값이라 false로 처리될듯.
    private boolean deleted;

//    private int status; // 판매상태x

    //RUNTIME시점의 오류를 compile단계에서 오류를 방생시켜줌. @Convert를 달아줌
//    @Convert(converter = BookStatusConverter.class)
    private BookStatus status;

//    public boolean isDisplayed(){
//        return status == 200;
//    }



//    public void addAuthor(Author author){
//        this.authors.add(author);
//    }

    /*TODO: ... 의 정확한 활용 방법에 대해서도 확인해볼것.*/
    public void addBookAndAuthors(BookAndAuthor... bookAndAuthors){
        Collections.addAll(this.bookAndAuthors, bookAndAuthors);
    }




//    @CreatedDate
//    private LocalDateTime createdAt;
//    @LastModifiedDate
//    private LocalDateTime updatedAt;

//    @PrePersist
//    public void perPersist(){
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    public void prePersist(){
//        this.updatedAt = LocalDateTime.now();
//    }

}

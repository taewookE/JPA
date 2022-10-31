package com.example.jpa.bookmanager.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
//TODO: @ToString override 하는것(callSuper = true) BaseEntity의 CreatedAt, UpdatedAt 그대로 쓸것이기 떄문이다.
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @ManyToOne
    @ToString.Exclude
    private Review review;

}

package com.example.jpa.bookmanager.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
//@EntityListeners(value = AuditingEntityListener.class)
public class UserHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO: @ManyToOne선언으로 해당 user_id는 삭제가능.
//    @Column(name = "user_id" , insertable = false, updatable = false)
//    private Long userId;
    private String name;

    private String email;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name ="city" , column = @Column(name = "home_city")),
            @AttributeOverride(name ="district", column = @Column(name = "home_district")),
            @AttributeOverride(name ="detail" , column = @Column(name = "home_address_detail")),
            @AttributeOverride(name ="zipCode" , column = @Column(name = "home_zipCode"))
    })
    private Address homeAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name ="city" , column = @Column(name = "company_city")),
            @AttributeOverride(name ="district", column = @Column(name = "company_district")),
            @AttributeOverride(name ="detail" , column = @Column(name = "company_address_detail")),
            @AttributeOverride(name ="zipCode" , column = @Column(name = "company_zipCode"))
    })
    private Address companyAddress;

//    @Embedded
//    private Address address;
//    private String city;
//
//    private String district;
//
//    private String detail;
//
//    private String zipCode;

    /*TODO: user와 userhistory관계에서 history에서도 user정보를 가져올 수 있지만, 일반적인 방법은 @OneToMany으로 상관관계를 맺어주는게 편하다.
    *       log양 조절을 위해서 Tostring.Exclude한다.
    *
    * */
    @ManyToOne()
    @ToString.Exclude
    private User user;

//    //TODO: 왜 추가가 안되지... ?? 나중에 다시 확인 해볼것. >
//    @CreatedDate
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    private LocalDateTime updatedAt;
}

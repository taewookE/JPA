package com.example.jpa.bookmanager.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
   //보통 String이 아니라 EnumType으로 구현하는것이 좋을 듯.
   private String city; //시
   private String district; //구

   //테이블생성시 해깔릴수 있어 detail 의 컬럼명을 변경
   @Column(name = "address_detail")
   private String detail; // 상세주소
   private String zipCode; //우편번호

}

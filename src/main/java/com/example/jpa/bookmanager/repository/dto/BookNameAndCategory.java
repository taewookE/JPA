package com.example.jpa.bookmanager.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

/*TODO: interface를 통해서 tuple 값을 찍어낼 수 있게 되는데 어떻게 동작을 하는것인지 정리 필요. 22.10.23*/
public class BookNameAndCategory {
    private String name;
    private String category;

}

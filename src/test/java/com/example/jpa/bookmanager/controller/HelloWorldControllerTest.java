package com.example.jpa.bookmanager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*Controller Test를 위한 단위테스트 시에는 WebMvcTest만으로도 ok.*/
@WebMvcTest
//@MockBean(JpaMetamodelMappingContext.class)
class HelloWorldControllerTest {

    //MockMvc를 하기 위해서는 Autowired를 통해서 의존성을 주입해주어야함.
    @Autowired
    private MockMvc mockMvc;

    @Test
    void helloTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World."));
    }

}
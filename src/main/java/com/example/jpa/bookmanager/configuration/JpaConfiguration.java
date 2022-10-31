package com.example.jpa.bookmanager.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/*TODO: sliceTest나 MockBean을 다르게 설정하여 ControllerTest를 설정할 수 있는데
*       아래처럼 별도 config로 빼서 사용하는것이 좋다.
*
*  */
@Configuration
@EnableJpaAuditing
public class JpaConfiguration {
}

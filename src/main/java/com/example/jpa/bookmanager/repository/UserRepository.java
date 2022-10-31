package com.example.jpa.bookmanager.repository;

import com.example.jpa.bookmanager.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/*TODO: JpaRepository상속을 위해서는 사용할 class와 PK의 Type을 선언해주면 된다.*/
public interface UserRepository extends JpaRepository<User,Long> {
//    User findByName(String name);
//    List<User> findByName(String name);
    Set<User> findByName(String name);

    //IS: Name과 파라미터와 동일하냐(eqauls)를 확인. query결과에 영향을 끼치지 않고 가독성을 높여주는데 도움을 줌
//    Set<User> findUserByNameIS(String name);
//    Set<User> findUserByName(String name);
//    Set<User> findUserByNameEquals(String name);

    /*TODO: Query subject keyword에 대해서 확인해볼것
    *       ex) findByxxxx
    *       일반적으로 findBy를 많이 사용하고 있음.
    * */

    //단일 객체에 대한 query 선언
    User findByEmail(String email);
    User getByEmail(String email);
    User readByEmail(String email);
    User queryByEmail(String email);
    User searchByEmail(String email);
    User streamByEmail(String email);
    User findUserByEmail(String email);
    User findSometingByEmail(String email);

    List<User> findFirst1ByName(String name);
    List<User> findTop1ByName(String name);
    List<User> findFirst2ByName(String name);
    List<User> findTop2ByName(String name);
    List<User> findLast1ByName(String name);

    //조건관련 쿼리메서드 테스트
    List<User> findByEmailAndName(String email,String name);
    List<User> findByEmailOrName(String email,String name);
    List<User> findByCreatedAtAfter(LocalDateTime yesterday);
    List<User> findByCreatedAtBefore(LocalDateTime yesterday);
    List<User> findByIdAfter(Long id);


    List<User> findByCreatedAtGreaterThan(LocalDateTime yesterday);
    List<User> findByCreatedAtGreaterThanEqual(LocalDateTime yesterday);
    List<User> findByCreatedAtBetween(LocalDateTime yesterday,LocalDateTime tomorrow);
    List<User> findByIdBetween(Long start,Long end);


    List<User> findByIdIsNotNull();
//    List<User> findByIdIsNotEmpty();

    //향후테스트를 위해 address를 삭제함.
//    List<User> findByAddressIsNotEmpty(); // name is not null and name is != "" 이 아님.
    List<User> findByNameIn(List<String> names);

    List<User> findByNameStartingWith(String name);
    List<User> findByNameEndingWith(String name);
    List<User> findByNameContains(String name);
    List<User> findByNameLike(String name);

//    List<User> findTop1ByName(String name);

    List<User> findTop1ByNameOrderByIdDesc(String name);
    List<User> findFirstByNameOrderByIdDescEmailAsc(String name);

    List<User> findFirstByName(String name, Sort sort);

    Page<User> findByName(String name, Pageable pageable);

    //TODO: return Type은 매핑가능하다면 어떤것으로 지정해도 괜찮음.
    @Query(value = "select * from user limit 1;",nativeQuery = true)
    Map<String,Object> findRawRecord();

    @Query(value = "select * from user", nativeQuery = true)
    List<Map<String,Object>> findAllRawRecord();


}

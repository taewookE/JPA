package com.example.jpa.bookmanager.repository;

import com.example.jpa.bookmanager.domain.Address;
import com.example.jpa.bookmanager.domain.Gender;
import com.example.jpa.bookmanager.domain.User;
import com.example.jpa.bookmanager.domain.UserHistory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;


@SpringBootTest
//@Transactional을 하면 테스트시 마다 데이터를 모두 rollback 해줌.
@Transactional
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserHistoryRepository userHistoryRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void crud(){
        //why 내 테스트에서는 오류가 나지.. ? 강의 constructor에서는 오류가 안나는데....
        //확인해본결과 spring 2.5.x version대에서는 오류가 발생하는 부분이 존재한다.
        //현재 spring version이 2.7.x version 대인데 gradle을 수정해서 2.4.4 verison으로 변경하니 정상 동작한다.
//        userRepository.save(new User());
//        System.out.println(">>>"+ userRepository.findAll());

//        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC,"name"));
//        List<Long> ids = new ArrayList<>();
//        ids.add(1L);
//        ids.add(2L);
//        ids.add(3L);

        //TODO: Lists.newArrayList는 test용 lib를 이용한것임. ID값이 1,3,5인것을 뽑아옴.
        List<User> users = userRepository.findAllById(Lists.newArrayList(1L,3L,5L));
        users.forEach(System.out::println);
//        userRepository.findAll().forEach(System.out::println);
    }

    @Test
    public void saveTest(){
        User user1 = new User("wook","woo99k@gmail.com");
        User user2 = new User("steve","steve@gmail.com");

        userRepository.saveAll(Lists.newArrayList(user1));

        //사실 findAll은 성능 이슈등으로 잘 사용하지 않긴한다.
        List<User> users = userRepository.findAll();
        users.forEach(System.out::println);
    }

    @Test
    public void saveAllTest(){
        User user1 = new User("wook","wook₩12@gmail.com");
        User user2 = new User("steve","steve@gmail.com");

        userRepository.saveAll(Lists.newArrayList(user1,user2));

        //사실 findAll은 성능 이슈등으로 잘 사용하지 않긴한다.
        List<User> users = userRepository.findAll();
        users.forEach(System.out::println);
    }

    /*
    * could not initialize proxy [com.example.jpa.bookmanager.domain.User#1] - no Session
      org.hibernate.LazyInitializationException: could not initialize proxy [com.example.jpa.bookmanager.domain.User#1] - no Session
    * 세션 오류가 발생하게 된다. - proxy 개념
    * getOne을 println까지 유지시켜주려면 @Transactional을 추가하여 세션을 유지시켜주면 된다.
    * getOne은 Entity에서 LazyFetch를 지원하고 있어서 해당 문제가 발생한다.
    * */
    @Test
    @Transactional
    void 출력테스트(){
        User user = userRepository.getOne(1L);
        System.out.println(user);
    }

    /*findById는 return 값으로 Optinal을 return하게 된다. Optional값으로 그대로 출력해도 되지만,
    * orElse로 return을 하면 User로 그대로 받아줄수 있다.
    * 이거패치 ???
    * TODO: Optional Keyword에 대해서 확인해 둘것.
    * */
    @Test
    void 출력테스트2(){
//        Optional<User> user = userRepository.findById(1L);
        User user = userRepository.findById(1L).orElse(null);
        System.out.println(user);
    }

    //flush가 query나 log상에 특이점은 확인할 수 없다.
    //TODO:flush는 쿼리를 변화시키는게 아니라 DB반영시점을 조정하는 것이라 영속성 context에 대해서 확인해볼것.
    @Test
    void flush테스트(){
//        userRepository.save(new User("taewook","taewook@gmail.com"));
//        userRepository.flush();
        userRepository.saveAndFlush(new User("taewook","taewook@gmail.com"));
        userRepository.findAll().forEach(System.out::println);
    }

    @Test
    void 카운트테스트(){
        long count = userRepository.count();
        System.out.println(count);
    }

    /*Hibernate:
    select count(*) as col_0_0_  from user user0_
    where user0_.id=?
    true
    존재유무를 확인할때는 .. count query를 통해서 확인하고 있다.
    countQueryString을 참조하고 있다.
    */
    @Test
    void 존재유무확인테스트(){
        boolean exists = userRepository.existsById(1L);
        System.out.println(exists);
    }


    //delete 실행시 select 후 delete query가 실행된다.
    //해당 Entity가 존재하는지 확인 하고 제거하는 프로세스로 진행된다.
    @Test
    void 삭제테스트(){
//        userRepository.delete(userRepository.findById(1L).orElseThrow(RuntimeException::new));
        userRepository.deleteById(1L);
    }

    //deleteAll시 select후 각 entity별로 delete query가 수행됨.
    @Test
    void 전체삭제테스트(){
        userRepository.deleteAll();
        userRepository.findAll().forEach(System.out::println);
    }

    //deleteAll시 행의 갯수만큼 query가 수행되서 성능에 이슈가 될 수 있다.
    @Test
    void 특정ID_삭제테스트(){
        userRepository.deleteAll(userRepository.findAllById(Lists.newArrayList(1L,3L)));
        userRepository.findAll().forEach(System.out::println);
    }

    //deleteInBatch로 진행하게되면 delete query가 1번만수행되게 된다. 조금더 효율적으로 삭제 처리가 가능하다.
    @Test
    void deleteBatch_테스트(){
        userRepository.deleteInBatch(userRepository.findAllById(Lists.newArrayList(1L,3L)));
        userRepository.findAll().forEach(System.out::println);

    }

    //select , 조건절 없이 바로 삭제 하는것이라... 조심해서 써야함.
    @Test
    void deleteAllInBatch_테스트(){
        userRepository.deleteAllInBatch();
        userRepository.findAll().forEach(System.out::println);
    }

    //page는 zeroBase Paging을 처리하기 때문에 현재 page 수는 0, 1 두개로 구성되어있다.
    @Test
    void 페이징테스트(){
        Page<User> users = userRepository.findAll(PageRequest.of(0,3));
        System.out.println("page: "+ users);
        System.out.println("totalElements: "+ users.getTotalElements());
        System.out.println("totalPages: "+ users.getTotalPages());
        System.out.println("numberOfElements: "+ users.getNumberOfElements());
        System.out.println("sort: "+ users.getSort());
        System.out.println("size: "+ users.getSize());

        users.getContent().forEach(System.out::println);
    }

    //queryByExample 방식 테스트 , Query Matcher
    //향후 한번더 확인 해 볼것. 여러가지 option 값을 넣고 배고 테스트 해 볼것.
    //ignore를 빼면 name과 exact 검색을 하게 됨
    @Test
    void QueryByExample_테스트(){
        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withIgnorePaths("name")
                .withMatcher("email",endsWith());

        Example<User> example = Example.of(new User("wook","gmail.com"),matcher);

        userRepository.findAll(example).forEach(System.out::println);
    }

    //String에는 좋은 성능을 지니지만 복잡한 쿼리 구성시에는 QueryDSL을 쓸것이다.
    @Test
    void QueryByExample_테스트2(){
        User user = new User();
        user.setEmail("gmail");

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("email",contains());
        Example<User> example = Example.of(user,matcher);

        userRepository.findAll(example).forEach(System.out::println);
    }

    /*TODO: save method가 insert / update 로 각각 동작하는 상황에 대해서 확인해 본다.
    *       SimpleJpaRepository class에 대해서 확인해본다.
    *       JPA의 save메소드에서는 최초 null check 이후 신규 entity면 em.persist() - insert , 아니면 em.merge() - update 처리를 한다.
    *       (em은 Entity Manager임)
    *       그리고 jpa의 query의 경우 snake case를 사용한다.
    *       가독성좋은 코드를 짜보자. (openSource 살펴보는것도 좋다. 틈틈히 openSource에 대해서도 분석해보자.)
    * */
    @Test
    void Update_테스트(){
        userRepository.save(new User("david","david@gmail.com"));
        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user.setEmail("david-update@gmail.com");

        userRepository.save(user);
    }


    /*쿼리메소드 테스트
    * 단일 객체가 아닌 경우 아래 오류가 발생 (User로 return을 받는경우)
    * query did not return a unique result: 2; nested exception is javax.persistence.NonUniqueResultException: query did not return a unique result: 2
    *
    * Optional도 return이 가능하다.
    * */
    /*TODO: findByNameList와 같은 임의의 함수명을 사용하게 되면 오류가 발생하게 된다. where조건에 해당 method를 기반으로 query를 생성하기 떄문에 Entity내 선언되어있는 Column을 기반으로 동작하는듯.*/
    @Test
    void select(){
//        System.out.println(userRepository.findByName("wook"));
//        System.out.println(userRepository.findByName("dennis"));
        System.out.println(userRepository.findAll());
    }

    /*TODO: 모든 동일한 결과값이 나오며, 코드 가독성에 맞게 하나를 골라서 사용하면 된다.
    *       Something 같은 정보로 검색해도 구현체에서 무시하고 email검색을 해버림..
    *
    * */
    @Test
    void 단일_query_메소드테스트(){
        System.out.println("findByEmail : " + userRepository.findByEmail("wook@gmail.com"));
        System.out.println("getByEmail : " + userRepository.getByEmail("wook@gmail.com"));
        System.out.println("readByEmail : " + userRepository.readByEmail("wook@gmail.com"));
        System.out.println("queryByEmail : " + userRepository.queryByEmail("wook@gmail.com"));
        System.out.println("searchByEmail : " + userRepository.searchByEmail("wook@gmail.com"));
        System.out.println("streamByEmail : " + userRepository.streamByEmail("wook@gmail.com"));
        System.out.println("findUserByEmail : " + userRepository.findUserByEmail("wook@gmail.com"));
        System.out.println("findSometingByEmail : " + userRepository.findSometingByEmail("wook@gmail.com"));
    }

    /*TODO: 아래 조건으로 검색하면 limit query 가 where 에 추가됨.*/
    @Test
    void Top_First_테스트(){
        System.out.println("findFirst1ByName: "+ userRepository.findFirst1ByName("wook"));
        System.out.println("findTop1ByName: "+ userRepository.findTop1ByName("wook"));
        //2개가 나옴.
        System.out.println("findFirst2ByName: "+ userRepository.findFirst2ByName("wook"));
        System.out.println("findTop2ByName: "+ userRepository.findTop2ByName("wook"));
    }
    //없는 쿼리메소드를 만들어서 테스트한다.
    //인식하지 않은 쿼리는 Jpa query에서 무시하게 된다. (findLast~ 가 없음)
    //orderBy query로 역순으로 정렬후 First1으로 뽑아오는게 일반적인 방법이다.
    @Test
    void lastOne테스트(){
        System.out.println("findLast1ByName: "+ userRepository.findLast1ByName("wook"));
    }
    @Test
    void 쿼리메소드_조건_테스트(){
//        System.out.println("findByEmailAndName: " + userRepository.findByEmailAndName("wook@gmail.com", "wook"));
//        System.out.println("findByEmailOrName: " + userRepository.findByEmailOrName("wook@gmail.com", "wook"));


        /*TODO: Before, After가 날짜 기준으로만 동작하는게 아니라 크거나, 작은것을 비교한다를 이해해야한다 !!
        *       GreaterThan은 After와 동일하게 동작을 하게 된다. (cf)LessThan
        *       before/after는 equals 를 포함하지 않는다.
        *       Between은 양끝단의 값을 포함하게 된다. (equals 포함)
        * */
        //특정날짜 이후 조회
//        System.out.println("findByCreatedAtAfter: " + userRepository.findByCreatedAtAfter(LocalDateTime.now().minusDays(1L)));
//        System.out.println("findByCreatedAtBefore: " + userRepository.findByCreatedAtBefore(LocalDateTime.now()));
//        System.out.println("findByIdAfter: " + userRepository.findByIdAfter(4L));
//        System.out.println("findByCreatedAtGreaterThan: " + userRepository.findByCreatedAtGreaterThan(LocalDateTime.now().minusDays(1L)));
//        System.out.println("findByCreatedAtGreaterThanEqual: " + userRepository.findByCreatedAtGreaterThanEqual(LocalDateTime.now().minusDays(1L)));
        System.out.println("findByCreatedAtBetween: " + userRepository.findByCreatedAtBetween(LocalDateTime.now().minusDays(1L),LocalDateTime.now().plusDays(1L)));
        System.out.println("findByIdBetween: " + userRepository.findByIdBetween(1L,3L));

    }

    /*TODO : 빈 값 관련 테스트 진행 , IsNotEmpty를 잘 사용하지 않음.
       두개 findByIdIsNotNull , findByIdIsNotEmpty 두개 동시에 실행하면 오류가 발생함.
       Failed to create query for method public abstract java.util.List com.example.jpa.bookmanager.repository.UserRepository.findByIdIsNotEmpty()!
       IsEmpty / IsNotEmpty can only be used on collection properties!
     */
    @Test
    void 쿼리메소드_Empty_Notnull_테스트(){
//        System.out.println("findByIdIsNotNull: " + userRepository.findByIdIsNotNull());
//        System.out.println("findByIdIsNotEmpty: " + userRepository.findByIdIsNotEmpty());
//        System.out.println("findByAddressIsNotEmpty: " + userRepository.findByAddressIsNotEmpty());
    }


    @Test
    void 쿼리메소드_in_테스트(){
        //향후에는 List를 받아 넘기는게 아니라, 다른 query의 결과를 받아서 In절을 처리하게됨.
        //일반적으로 in절 내부에 list 데이터 길이를 검토하고 사용하는것이 좋다. > 넘어가는 list의 길이가 너무 길어지면 성능이 떨어짐.
        System.out.println("findByNameIn: " + userRepository.findByNameIn(Lists.newArrayList("wook","martins")));
    }

    /*위 3개 메소드는 모두 like 검색을 진행하게 된다.*/
    @Test
    void 쿼리메소드_문자열검색_테스트(){
        System.out.println("findByNameStartingWith: " + userRepository.findByNameStartingWith("wo"));
        System.out.println("findByNameEndingWith: " + userRepository.findByNameEndingWith("ok"));
        System.out.println("findByNameContains: " + userRepository.findByNameContains("wo"));

        // %를 통해서 like검색을 잘 진행되게 됨 .
        // 실제로는 아래와 같이 문자열을 작성하게 되어야 하는데 위 3개 메소드를 통해서 코드가독성을 높여 주게 됨.
        System.out.println("findByNameLike: " + userRepository.findByNameLike("%"+"oo"+"%"));
    }

    /*TODO:findTop1xxx의 숫자 1이 생략되면 default로 1로 들어간다.
      그리고 1,2번 예제의 경우는 결과값이 여러개가 있는경우 List로 받으면 복수개로 들어온다.
      의도한 것과같이 여러개의 값이 있더라도 1개의 결과값만 뽑는경우는 순서를 역순으로 정렬후 TOP으로 가져오면 하나만 가져오게 된다.
    * */
    @Test
    void 페이징_정렬_테스트(){
        System.out.println("findTop1ByName : " + userRepository.findTop1ByName("martin"));
        System.out.println("findLast1ByName : " + userRepository.findLast1ByName("wook"));
        System.out.println("findTop1ByNameOrderByIdDesc : " + userRepository.findTop1ByNameOrderByIdDesc("wook"));
        System.out.println("findFirstByNameOrderByIdDescEmailAsc : " + userRepository.findFirstByNameOrderByIdDescEmailAsc("wook"));
        /*TODO: parameter를 통한 sorting (w/ Sort class ), Sort.by는 여러개의 정렬순서를 받을 수 있게 되어있음.
                아래 파라미터를 사용하는 것은 코드 가독성 측면에서 메리트가 큼.
                향후 어떠한 쿼리메소드를 추가하는 경우, JpaRepository가 상속받는 구현체드의 메소드 설명을 확인해서
                활용할 수 있도록 한다.
        * */
        System.out.println("findFirstByName : " + userRepository.findFirstByName("wook",Sort.by(Sort.Order.desc("id"), Sort.Order.asc("email"))));
        //아래 정렬방식에 대한 함수를 만들어서 함수 호출로 사용하는 방법도 존재.
        System.out.println("findFirstByName : " + userRepository.findFirstByName("wook",getSort()));
    }

    //네이밍 컨벤션 방식을 사용할지, 메소드를 이용한 sorting을 할것인지 사용처에 따라서 고민해보아야 한다.
    private Sort getSort(){
        return Sort.by(
                Sort.Order.desc("id"),
                Sort.Order.asc("email"),
                Sort.Order.desc("createdAt"),
                Sort.Order.asc("updatedAt")
        );
    }

    /*TODO: List paging 처리
    *   PageRequest.of() 처리와 내부 내용확인을 위한 getContent()의 활용 , getTotalElements()의 활용도 향후 다른곳에서 써볼것.
    *   Page class는 slice class를 상속받는데 slice클래스 내부 메소드들에도 한번 확인해보기.
    * */
    @Test
    void 페이징_소팅테스트(){
        System.out.println("findByNameWithPagign: "+ userRepository.findByName("wook", PageRequest.of(0,1,Sort.by(Sort.Order.desc("id")))).getContent());
    }

    @Test
    void insertAndUpdateTest(){
        User user = new User();
        user.setName("taewook");
        user.setEmail("wook2@gmail.com");

        userRepository.save(user);

        User user2 = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user2.setName("tttttttt");
        userRepository.save(user2);

        System.out.println(userRepository.findById(1L));

    }

    //TODO: Entity Enum Test
    @Test
    void enumTest(){
        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);

        //Ordinal로 만들게 되면 나중에 enum이 바뀌어 순서가 바뀌게 되면 꼬일수 있으므로 EnumType을 String으로 처리해야한다.!!!!!
        user.setGender(Gender.MALE);
        userRepository.save(user);

        userRepository.findAll().forEach(System.out::println);
        System.out.println(userRepository.findRawRecord().get("gender"));
    }

    @Test
    void ListenerTest(){
        User user = new User();
        user.setEmail("taewook2@gmail.com");
        user.setName("taewook");

        userRepository.save(user);

        User user2 = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        user2.setName("wokikikiki");

        userRepository.save(user2);

        userRepository.deleteById(4L);
    }

    //입력날짜같은 부분을 사용자 실수나, 누락되는 형태가 발생할 수 있으므로, Entity 내 셋팅을 하는방법으로 많이 쓰임.
    @Test
    void prePersistTest(){
        User user = new User();
        user.setEmail("taewook3@gmail.com");
        user.setName("wook3");
//        user.setCreatedAt(LocalDateTime.now());
//        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        System.out.println(userRepository.findByEmail("taewook3@gmail.com"));
    }

    @Test
    void preUpdateTest(){
        User user = userRepository.findById(1L).orElseThrow(RuntimeException::new);
        System.out.println("as-is : " + user);

        user.setName("updated name");
        userRepository.save(user);

        System.out.println("to-be"+ userRepository.findAll().get(0));

    }


    /*TODO: 하나의 Entity에서 여러 EntityListener를 설정할 수 있다. (history table 생성 테스트)
            그러나, UserEntityListener 선언시 아래 처럼 사용해도 null Point Exception이 발생한다.
            @Autowired
            private UserHistoryRepository userHistoryRepository;
            EntityListenr는 SpringBean을 주입받지 못한다!!!!!!!!!!!!!!!!!1
            Caused by: java.lang.NullPointerException
            그래서 별도의 class를 만들어주어야 한다.
    * */
    @Test
    void userHistoryTest(){
        User user = new User();
        user.setName("taewook-new");
        user.setEmail("taewook-new@gmail.com");

        userRepository.save(user);

        user.setName("taewook-new-update");
        userRepository.save(user);

        userHistoryRepository.findAll().forEach(System.out::println);
    }



    //TODO: 왜 userHistory가 한번만 쌓이게 되는지 확인이 필요.
    @Test
    void User_RelationTest(){
        User user = new User();
        user.setName("david");
        user.setEmail("test@gmail.com");
        user.setGender(Gender.MALE);

        userRepository.save(user);

        user.setName("david-update");
        userRepository.save(user);

        System.out.println("user-update1:"+ user);

        user.setEmail("test-update@gmail.com");
        userRepository.save(user);

        System.out.println("user-update2:"+ user);

        //list 출력시 해당 람다식을 많이 사용해볼것.
//        userHistoryRepository.findAll().forEach(System.out::println);

//        List<UserHistory> result = userHistoryRepository.findByUserId(
//                userRepository.findByEmail("test-update@gmail.com").getId()
//        );

        //TODO : Failed to lazily initialize a collection of role:
        // com.example.jpa.bookmanager.domain.User.userHistories, could not initialize proxy - no Session
        // 해당 처리를 위해서는 FetchType을 설정해두면 해결은 됨.(@OneToMany 에 fetch = FetchType.EAGER 추가)

        List<UserHistory> result = userRepository.findByEmail("test-update@gmail.com").getUserHistories();

        result.forEach(System.out::println);

        System.out.println("UserHistory.getUser(): "+ userHistoryRepository.findAll().get(0).getUser());

    }



    @Test
    void embedTest(){
        userRepository.findAll().forEach(System.out::println);

        User user = new User();
        user.setName("wook");

        /*TODO: 단순히 Address를 동일하게 적용하게 되면 Repeated column in mapping for entity: 에러가 발생하게 된다.
        *
        * */
        user.setHomeAddress(new Address("서울시","강남구","강남대로 123", "123123"));
        user.setCompanyAddress(new Address("서울시","서초구","양재 12", "56788"));


        /*TODO: address의 값이 null or 빈 값으로 들어오는 경우를 테스트
        *       둘다 db에는 null로 insert된다.
        *
        * */
        User user1 = new User();
        user1.setName("devid");
        user1.setHomeAddress(null);
        user1.setCompanyAddress(null);

        userRepository.save(user1);

        User user2 = new User();
        user2.setName("jordan");
        user2.setHomeAddress(new Address());
        user2.setCompanyAddress(new Address());

        userRepository.save(user2);

        //테스트정확성을 위해서 cache를 날리고, 다시 조회하도록 em.clear()를 해준다.
        entityManager.clear();

        userRepository.save(user);
        userRepository.findAll().forEach(System.out::println);
        userHistoryRepository.findAll().forEach(System.out::println);

        //TODO: List형태의 값을 람다식으로 찍는 방법에 대해서 확실하게 익히기.
        userRepository.findAllRawRecord().forEach(a -> System.out.println(a.values()));
    }




}
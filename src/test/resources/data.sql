insert into user (`id`,`name`,`email`,`created_at`,`updated_at`) values (1,'.wook','wook@gmail.com',now(),now());
insert into user (`id`,`name`,`email`,`created_at`,`updated_at`) values (2,'dennis','dennis@gmail.com',now(),now());
insert into user (`id`,`name`,`email`,`created_at`,`updated_at`) values (3,'sophia','sophia@gmail.com',now(),now());
insert into user (`id`,`name`,`email`,`created_at`,`updated_at`) values (4,'james','james@gmail.com',now(),now());
insert into user (`id`,`name`,`email`,`created_at`,`updated_at`) values (5,'wook','wook@another.com',now(),now());

-- 연관관계테스트
-- option + enter를 하면 다중 선택을 할 수 있게 함.
insert into publisher(`id`, `name`) values (1, '1번 책');
insert into book(`id`,`name`,`publisher_id`,`deleted`, `status`) values (1, 'JPA book',1,false,100);
insert into book(`id`,`name`,`publisher_id`,`deleted`, `status`) values (2, 'spring security',1,false,200);
insert into book(`id`,`name`,`publisher_id`,`deleted`, `status`) values (3, 'soft-delete book',1,false,100);

insert into review(`id`,`title`,`content`,`score`, `user_id`,`book_id`) values (1, '내 인생을 바꾼 책', '너무좋아요','5.0',1,1);
insert into review(`id`,`title`,`content`,`score`, `user_id`,`book_id`) values (2, '리뷰 책', '너무좋아요','3.0',2,2);

insert into comment(`id`,`comment`,`review_id`) values (1, '좋아용 1', 1);
insert into comment(`id`,`comment`,`review_id`) values (2, '별로용 1', 1);
insert into comment(`id`,`comment`,`review_id`) values (3, '별로용 2', 2);

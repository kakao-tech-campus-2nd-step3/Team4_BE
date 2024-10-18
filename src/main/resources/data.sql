INSERT INTO GYM_TB(location, name, status)
VALUES ('강원특별자치도 춘천시 강원대학길 1', '백령스포츠센터', 'APPROVAL'),
       ('강원특별자치도 춘천시 춘천로 204', '파리휘트니스', 'APPROVAL'),
       ('강원특별자치도 춘천시 춘천로 204', '감자헬스장', 'WAITING'),
       ('강원특별자치도 춘천시 춘천로 204', 'chuncheongym', 'WAITING');


INSERT INTO USER_TB(email, password, name, profile_image_url, location)
VALUES ('a@naver.com', '$2a$10$/kIrh1IIxo60.jHaISACEOpyrb6/2KpgQYcfHlOJBDuFdl9KrOrBm', '일반회원1',
        'https://nurspace-bucket.s3.ap-northeast-2.amazonaws.com/default_profile.jpg',
        '강원특별자치도 춘천시 백령로 1');

INSERT INTO TRAINER_TB(email, password, name, profile_image_url, gym_id, gender)
VALUES ('t@naver.com', '$2a$10$/kIrh1IIxo60.jHaISACEOpyrb6/2KpgQYcfHlOJBDuFdl9KrOrBm', '트레이너1',
        'https://nurspace-bucket.s3.ap-northeast-2.amazonaws.com/default_profile.jpg', 1, 'MALE');

INSERT INTO GYM_ADMIN_RELATION_TB(gym_id, trainer_id)
VALUES (1, 1);

INSERT INTO BODY_INFO_TB(user_id, inbody_image_url, create_date, deleted)
VALUES (1, 'https://nurspace-bucket.s3.ap-northeast-2.amazonaws.com/default_profile.jpg', NOW(),
        FALSE);

INSERT INTO SPORTS_TB(name, deleted)
VALUES ('PT', 'FALSE');


INSERT INTO SPORTS_TB(name, deleted)
VALUES ('PT', 'FALSE');

INSERT INTO CHATTING_ROOM_TB(user_id,trainer_id)
VALUES (1,1);
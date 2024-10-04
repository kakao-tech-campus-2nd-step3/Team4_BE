INSERT INTO GYM_TB(location, name, status)
VALUES ('강원특별자치도 춘천시 강원대학길 1','백령스포츠센터', 'WAITING'),('강원특별자치도 춘천시 춘천로 204','파리휘트니스', 'WAITING');

INSERT INTO USER_TB(email, password, name, profile_image_url, location)
VALUES ('a@naver.com', '123', '일반회원1', 'https://nurspace-bucket.s3.ap-northeast-2.amazonaws.com/default_profile.jpg', '강원특별자치도 춘천시 백령로 1');

INSERT INTO TRAINER_TB(email, password, name, profile_image_url, gym_id, gender)
VALUES ('t@naver.com', '123', '트레이너1', 'https://nurspace-bucket.s3.ap-northeast-2.amazonaws.com/default_profile.jpg', 1, 'MALE');


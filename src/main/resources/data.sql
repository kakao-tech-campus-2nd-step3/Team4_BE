/*Gym Dummy Data*/
INSERT INTO GYM_TB(location, name)
VALUES ('강원특별자치도 춘천시 강원대학길 1','백령스포츠센터'),('강원특별자치도 춘천시 춘천로 204','파리휘트니스');

/*User Dummy Data*/
INSERT INTO USER_TB(email, password, name, location)
VALUES ('user@linkfit.com','password','김회원','강원특별자치도 춘천시 강원대학길 1');

/*Trainer Dummy Data*/
INSERT INTO TRAINER_TB(email, password, name, gender)
VALUES ('trainer@linkfit.com','password','김헬창','남자');

INSERT INTO CAREER_TB(career,trainer_id)
VALUES('3대 500','1');
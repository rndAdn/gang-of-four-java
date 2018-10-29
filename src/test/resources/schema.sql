drop table game if exists;
drop table game_player if exists;
drop table player if exists;
drop table user if exists;

CREATE TABLE GAME(
    ID BIGINT NOT NULL,
    CREATED TIMESTAMP NOT NULL,
    GAME_STATUS VARCHAR(255),
    NEXT_PLAYER_ID BIGINT NOT NULL
);
ALTER TABLE GAME ADD CONSTRAINT CONSTRAINT_2 PRIMARY KEY(ID);
-- 0 +/- SELECT COUNT(*) FROM GAME;
CREATE TABLE GAME_PLAYER(
    HAND_CARD VARCHAR(255) NOT NULL,
    ORDER_TO_PLAY INTEGER NOT NULL,
    SCORE BIGINT NOT NULL,
    ID BIGINT NOT NULL,
    PLAYER_ID BIGINT,
    GAME_ID BIGINT
);
ALTER TABLE GAME_PLAYER ADD CONSTRAINT CONSTRAINT_1 PRIMARY KEY(ID);
-- 0 +/- SELECT COUNT(*) FROM GAME_PLAYER;
CREATE TABLE PLAYER(
    ID BIGINT NOT NULL

);
ALTER TABLE PLAYER ADD CONSTRAINT CONSTRAINT_8 PRIMARY KEY(ID);
-- 4 +/- SELECT COUNT(*) FROM PLAYER;
CREATE TABLE USER(
    ID BIGINT NOT NULL,
    EMAIL VARCHAR(255) NOT NULL,
    FIRST_NAME VARCHAR(255) NOT NULL,
    LAST_NAME VARCHAR(255) NOT NULL,
    PASSWORD VARCHAR(255) NOT NULL,
    PROFILE_PICTURE VARCHAR(255),
    USER_NAME VARCHAR(255) NOT NULL
);
ALTER TABLE USER ADD CONSTRAINT CONSTRAINT_27 PRIMARY KEY(ID);
-- 4 +/- SELECT COUNT(*) FROM USER;
ALTER TABLE USER ADD CONSTRAINT UK_LQJRCOBRH9JC8WPCAR64Q1BFH UNIQUE(USER_NAME);
ALTER TABLE PLAYER ADD CONSTRAINT FK24SDIR3EYDD9P2YKKYVUGPVMU FOREIGN KEY(ID) REFERENCES USER(ID) NOCHECK;
ALTER TABLE GAME ADD CONSTRAINT FK5NP8KSVCHAWU9KA5VNQW4M66U FOREIGN KEY(NEXT_PLAYER_ID) REFERENCES GAME_PLAYER(ID) NOCHECK;
ALTER TABLE GAME_PLAYER ADD CONSTRAINT FK8SO14TND5MQDJQABUGC0CYCXU FOREIGN KEY(GAME_ID) REFERENCES GAME(ID) NOCHECK;
ALTER TABLE GAME_PLAYER ADD CONSTRAINT FKTF8NTCOPUMT7O1TKDTQS45VFL FOREIGN KEY(PLAYER_ID) REFERENCES PLAYER(ID) NOCHECK;


-- Insert player
insert into USER (email, first_name, last_name, password, profile_picture, user_name, id) values ('test1@test.com', 'first_name_test1', 'last_name_test1', '$2a$10$2E3ReH0u3IEvmVGl0suzCuD96oNpAYssFWF./A6wOncVSNLBjUTxq', 'profile_picture_test1', 'test1', 1);
insert into PLAYER (id) values (1);
insert into USER (email, first_name, last_name, password, profile_picture, user_name, id) values ('test1@test.com', 'first_name_test2', 'last_name_test2', '$2a$10$B75Hyguu9puFtM7pEBl8E.UnqdnkppTlw68YgJX9DZbtX8tUlt0Qu', 'profile_picture_test2', 'test2', 2);
insert into PLAYER (id) values (2);
insert into USER (email, first_name, last_name, password, profile_picture, user_name, id) values ('test1@test.com', 'first_name_test3', 'last_name_test3', '$2a$10$N/1bYYAtEvpftmir1t03HOY4e2NBd09a8v1NxiFp62mKZ3gMeru9a', 'profile_picture_test3', 'test3', 3);
insert into PLAYER (id) values (3);
insert into USER (email, first_name, last_name, password, profile_picture, user_name, id) values ('test1@test.com', 'first_name_test4', 'last_name_test4', '$2a$10$zcsn7sgDnBgfPUWYSshLo.XpLOpKnuaWgsKf6wO5IoVymyFcroPCG', 'profile_picture_test4', 'test4', 4);
insert into PLAYER (id) values (4);


-- Insert Game
insert into game_player (game_id, hand_card, order_to_play, player_id, score, id) values (null, '0', 1, 1, 0, 1);
insert into game (created, game_status, next_player_id, id) values (parsedatetime('2018-09-17 18:47:52.069', 'yyyy-MM-dd hh:mm:ss.SS'), 'WAITS_FOR_PLAYER', 1, 1);
update game_player set game_id = 1 where id = 1;

insert into game_player (game_id, hand_card, order_to_play, player_id, score, id) values (null, '0', 1, 1, 0, 2);
insert into game (created, game_status, next_player_id, id) values (parsedatetime('2018-09-18 18:47:52.069', 'yyyy-MM-dd hh:mm:ss.SS'), 'WAITS_FOR_PLAYER', 1, 2);
update game_player set game_id = 2 where id = 2;
insert into game_player (game_id, hand_card, order_to_play, player_id, score, id) values (2, '0', 2, 2, 0, 3);
insert into game_player (game_id, hand_card, order_to_play, player_id, score, id) values (2, '0', 3, 3, 0, 4);



insert into game_player (game_id, hand_card, order_to_play, player_id, score, id) values (null, '0', 1, 1, 0, 5);
insert into game (created, game_status, next_player_id, id) values (parsedatetime('2018-09-18 18:47:52.069', 'yyyy-MM-dd hh:mm:ss.SS'), 'WAITS_FOR_PLAYER', 1, 3);
update game_player set game_id = 3 where id = 5;
insert into game_player (game_id, hand_card, order_to_play, player_id, score, id) values (3, '0', 2, 2, 0, 6);
insert into game_player (game_id, hand_card, order_to_play, player_id, score, id) values (3, '0', 3, 3, 0, 7);
insert into game_player (game_id, hand_card, order_to_play, player_id, score, id) values (3, '0', 4, 4, 0, 8);
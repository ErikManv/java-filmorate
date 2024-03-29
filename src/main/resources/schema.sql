DROP TABLE IF EXISTS FILM_TABLE CASCADE;
DROP TABLE IF EXISTS USER_TABLE CASCADE;
DROP TABLE IF EXISTS FRIENDS_TABLE CASCADE;
DROP TABLE IF EXISTS LIKE_USER_TABLE CASCADE;
DROP TABLE IF EXISTS GENRE_FILM_TABLE CASCADE;

CREATE TABLE IF NOT EXISTS GENRE_TABLE
(
    ID   INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    NAME VARCHAR(100),
    CONSTRAINT unique_genres UNIQUE (NAME)
);


CREATE TABLE IF NOT EXISTS MPA_TABLE
(
    MPA_ID INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    MPA_NAME VARCHAR(100),
    CONSTRAINT unique_ratings UNIQUE (MPA_NAME)
);

CREATE TABLE IF NOT EXISTS FILM_TABLE
(
    ID           INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    NAME         VARCHAR(100),
    DESCRIPTION  VARCHAR(100),
    RELEASE_DATE VARCHAR(100),
    GENRES     INTEGER,
    MPA    INTEGER,
    FOREIGN KEY (GENRES) REFERENCES GENRE_TABLE (ID) ON DELETE RESTRICT,
    FOREIGN KEY (MPA) REFERENCES MPA_TABLE (MPA_ID) ON DELETE RESTRICT,
    DURATION     LONG,
    RATE         INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS USER_TABLE
(
    ID       INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    NAME     VARCHAR(100),
    LOGIN    VARCHAR(100),
    EMAIL    VARCHAR(100),
    BIRTHDAY VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS LIKE_USER_TABLE
(
    FILM_ID INTEGER,
    USER_ID INTEGER,
    FOREIGN KEY (FILM_ID) REFERENCES FILM_TABLE (ID) ON DELETE CASCADE,
    FOREIGN KEY (USER_ID) REFERENCES USER_TABLE (ID) ON DELETE CASCADE,
    CONSTRAINT user_like_controller UNIQUE (FILM_ID, USER_ID)
);


CREATE TABLE IF NOT EXISTS FRIENDS_TABLE
(
    USER_ID              INTEGER,
    ANOTHER_USER_ID      INTEGER,
    USER_REQUEST         BOOLEAN,
    ANOTHER_USER_REQUEST BOOLEAN,
    STATUS_FRIENDS       BOOLEAN,
    CONSTRAINT user_friends_controller UNIQUE (USER_ID, ANOTHER_USER_ID)
);

CREATE TABLE IF NOT EXISTS GENRE_FILM_TABLE
(
    FILM_ID  INTEGER,
    GENRE_ID INTEGER,
    FOREIGN KEY (FILM_ID) REFERENCES FILM_TABLE (ID) ON DELETE CASCADE,
    FOREIGN KEY (GENRE_ID) REFERENCES GENRE_TABLE (ID) ON DELETE CASCADE,
    CONSTRAINT unique_film_genres UNIQUE (FILM_ID, GENRE_ID)
);
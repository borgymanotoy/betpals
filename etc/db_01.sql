CREATE TABLE users (
    id  BIGINT NOT NULL IDENTITY PRIMARY KEY,
    enabled     boolean,
    username    varchar,
    password    varchar
);

CREATE TABLE userprofile (
    id  BIGINT NOT NULL IDENTITY PRIMARY KEY,
    userId      bigint,
    name        varchar,
    surname     varchar,
    email       varchar,
    registrationDate    timestamp,
    lastLoginDate       timestamp,
    numberOfVisits      integer
);

CREATE TABLE authority (
    id  BIGINT NOT NULL IDENTITY PRIMARY KEY,
    user_id      bigint,
    username    varchar,
    authority   varchar
);

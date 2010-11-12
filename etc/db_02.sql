CREATE TABLE communities (
    id  BIGINT NOT NULL IDENTITY PRIMARY KEY,
    owner_id    BIGINT,
    created     timestamp,
    name        varchar,
    description varchar
);

CREATE TABLE groups (
    id  BIGINT NOT NULL IDENTITY PRIMARY KEY,
    owner_id    BIGINT,
    created     timestamp,
    name        varchar,
    description varchar
);

CREATE TABLE user_friends (
    user_id  BIGINT,
    friend_id  BIGINT
);

CREATE TABLE community_members (
    community_id  BIGINT,
    member_id  BIGINT
);

CREATE TABLE group_members (
    group_id  BIGINT,
    member_id  BIGINT
);


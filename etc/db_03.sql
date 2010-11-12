CREATE TABLE activities (
    id              BIGINT NOT NULL IDENTITY PRIMARY KEY,
    ownerId         bigint,
    ownerName       varchar,
    activityType    varchar,
    created         timestamp,
    message         varchar
);

CREATE TABLE activities_comments (
    id              BIGINT NOT NULL IDENTITY PRIMARY KEY,
    activityId      bigint,
    ownerId         bigint,
    ownerName       varchar,
    created         timestamp,
    message         varchar
);

CREATE TABLE activities_likes (
    id              BIGINT NOT NULL IDENTITY PRIMARY KEY,
    activityId      bigint,
    ownerId         bigint,
    ownerName       varchar,
    created         timestamp
);


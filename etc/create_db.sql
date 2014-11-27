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
    numberOfVisits      integer,
    address     varchar,
    city        varchar,
    postalCode  varchar,
    country     varchar,
    bio         varchar
);

CREATE TABLE authority (
    id  BIGINT NOT NULL IDENTITY PRIMARY KEY,
    user_id      bigint,
    username    varchar,
    authority   varchar
);

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

CREATE TABLE account (
    id              BIGINT NOT NULL IDENTITY PRIMARY KEY,
    ownerId         bigint,
    currency        varchar,
    balance         decimal,
    available       decimal,
    created         timestamp,
    active          boolean
);

CREATE TABLE account_transaction (
    id              BIGINT NOT NULL IDENTITY PRIMARY KEY,
    accountId         bigint,
    transactionType     varchar,
    transactionDate     timestamp,
    sourceId            bigint,
    destinationId       bigint,
    amount              decimal,
    currency            varchar,
    description         varchar
);

CREATE TABLE join_account_transaction (
    account_id      bigint,
    transaction_id  bigint
);

CREATE TABLE competition (
    id                  BIGINT NOT NULL IDENTITY PRIMARY KEY,
    ownerId             bigint,
    competitionType     varchar,
    accessType          varchar,
    currency            varchar,
    fixedStake          decimal,
    created             timestamp,
    deadline            timestamp,
    settlingDeadline    timestamp,
    name                varchar,
    description         varchar,
    accountid           bigint,
    status              varchar
);

CREATE TABLE event (
    id                  BIGINT NOT NULL IDENTITY PRIMARY KEY,
    competitionId       bigint,
    name                varchar,
    description         varchar
);

CREATE TABLE alternative (
    id                  BIGINT NOT NULL IDENTITY PRIMARY KEY,
    eventId             bigint,
    alternativeType     varchar,
    name                varchar,
    description         varchar,
    taken               boolean
);

CREATE TABLE join_competition_event (
    competition_id      bigint,
    event_id            bigint
);

CREATE TABLE join_event_alternative (
    event_id            bigint,
    alternative_id      bigint
);

CREATE TABLE bet (
    id                  BIGINT NOT NULL IDENTITY PRIMARY KEY,
    ownerId             bigint,
    accountId           bigint,
    selectionId         bigint,
    currency            varchar,
    odds                decimal,
    stake               decimal,
    profitOrLoss        decimal,
    placed              timestamp,
    settled             timestamp,
    details             varchar,
    ownername           varchar
);

CREATE TABLE invitation (
    id                  BIGINT NOT NULL IDENTITY PRIMARY KEY,
    ownerId             bigint,
    inviteeId           bigint,
    competitionId       bigint,
    deadline            timestamp,
    ownerName           varchar,
    competitionName     varchar
);

CREATE TABLE join_alternative_bet (
    alternative_id      bigint,
    bet_id            bigint
);

CREATE TABLE userrequest (
    id                  BIGINT NOT NULL IDENTITY PRIMARY KEY,
    ownerId             bigint,
    inviteeId           bigint,
    ownerName           varchar,
    inviteename         varchar
);

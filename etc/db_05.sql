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
    description         varchar
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


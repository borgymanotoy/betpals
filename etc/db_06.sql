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
    details             varchar
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

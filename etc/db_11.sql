CREATE TABLE userrequest (
    id                  BIGINT NOT NULL IDENTITY PRIMARY KEY,
    ownerId             bigint,
    inviteeId           bigint,
    ownerName           varchar
);

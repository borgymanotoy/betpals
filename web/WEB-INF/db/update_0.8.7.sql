
create table passwordrecoveryrequest (
    id  BIGINT NOT NULL IDENTITY PRIMARY KEY,
    userId      bigint,
    requestHash varchar
);

alter table alternative add column priority integer default 1;

create table passwordrecoveryrequest (
    id  BIGINT NOT NULL IDENTITY PRIMARY KEY,
    userId      bigint,
    requestHash varchar
);

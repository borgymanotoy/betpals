
create table user_log (
    id  BIGINT NOT NULL IDENTITY PRIMARY KEY,
    userId      bigint,
    created     timestamp,
    message     varchar
);

create table competition_log (
    id  BIGINT NOT NULL IDENTITY PRIMARY KEY,
    competitionId       bigint,
    created             timestamp,
    message             varchar
);

alter table invitation add column created timestamp;
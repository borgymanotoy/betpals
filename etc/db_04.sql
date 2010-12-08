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


ALTER TABLE bet ADD COLUMN ownername VARCHAR;

CREATE TABLE join_alternative_bet (
    alternative_id      bigint,
    bet_id            bigint
);

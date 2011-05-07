set autocommit true;

delete from bet;
delete from competition;
delete from event;
delete from alternative;
delete from invitation;

delete from join_alternative_bet;
delete from join_competition_event;
delete from join_event_alternative;

commit;
set autocommit false;
alter table invitation add column invitationType varchar;
update invitation set invitationType = 'USER';
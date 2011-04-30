alter table authority alter column user_id rename to userid;

insert into users values(1,true,'admin','b89e4e84ecea6c17f2a6b40c490daeb9b22b4e2c');
insert into authority values(1, 1, 'admin','ROLE_SUPERVISOR');
insert into authority values(2, 1, 'admin','ROLE_USER');
insert into userprofile values(1, 1, 'admin', 'admin', 'admin@mybetpals.com', '2011-04-30 21:44:32.184000000',null,null,null,null,null,null,null);


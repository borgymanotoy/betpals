alter table communities alter column owner_id rename to ownerid;
alter table communities add column accesstype varchar;
alter table userrequest add column requesttype varchar;
alter table userrequest add column extensionId bigint;
alter table userrequest add column extensionName varchar;
alter table activities add column extensionId bigint;
alter table activities add column extensionName varchar;

update activities set activitytype = 'USER';

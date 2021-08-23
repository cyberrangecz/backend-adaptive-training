alter table training_definition add column if not exists last_edited_by varchar(127) not null default '';
alter table training_instance add column if not exists last_edited timestamp not null default CURRENT_TIMESTAMP;
alter table training_instance add column if not exists last_edited_by varchar(127) not null default '';
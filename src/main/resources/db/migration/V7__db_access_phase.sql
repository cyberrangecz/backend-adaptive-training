create table access_phase (
    cloud_content text not null,
    local_content text not null,
    passkey varchar(255) not null,
    phase_id int8 not null,
    primary key (phase_id),
    foreign key (phase_id) references abstract_phase
);

alter table training_instance add column local_environment boolean default (false);
create table mitre_technique (
    mitre_technique_id  bigserial not null,
    technique_key varchar(64) not null unique,
    primary key (mitre_technique_id)
);

create sequence mitre_technique_seq AS bigint INCREMENT 50 MINVALUE 1;
create unique index mitre_technique_key_index
    on mitre_technique (technique_key);

create table training_phase_mitre_technique (
    training_phase_id int8 not null,
    mitre_technique_id int8 not null,
    primary key (training_phase_id, mitre_technique_id),
    foreign key (training_phase_id) references training_phase,
    foreign key (mitre_technique_id) references mitre_technique
);

create table expected_commands (
   training_phase_id int8 not null,
   command varchar(128) not null,
   primary key (training_phase_id, command),
   foreign key (training_phase_id) references training_phase
);

create index training_phase_mitre_technique_index
    on expected_commands (training_phase_id);

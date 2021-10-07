create table submission (
    submission_id bigserial not null,
    provided text not null,
    type varchar(255) not null,
    phase_id int8 not null,
    task_id int8 not null,
    training_run_id int8 not null,
    date timestamp not null,
    ip_address varchar(255) not null,
    primary key (submission_id),
    foreign key (phase_id) references abstract_phase,
    foreign key (task_id) references task,
    foreign key (training_run_id) references training_run
);

create sequence submission_seq AS bigint INCREMENT 50 MINVALUE 1;

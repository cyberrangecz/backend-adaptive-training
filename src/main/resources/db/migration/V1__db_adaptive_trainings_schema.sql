-- USER
create table "user" (
    user_id bigserial not null,
    user_ref_id int8 not null,
    primary key (user_id)
);

create table participant_task_assignment (
    participant_task_assignment_id bigserial not null,
    phase_id int8 not null,
    training_run_id int8 not null,
    task_id int8,
    primary key (participant_task_assignment_id),
    foreign key (phase_id) references abstract_phase,
    foreign key (training_run_id) references training_run,
    foreign key (task_id) references task,
);

-- TRAINING
create table training_definition (
    training_definition_id bigserial not null,
    description text,
    last_edited timestamp not null,
    estimated_duration int8,
    outcomes bytea,
    prerequisites bytea,
    show_stepper_bar boolean not null,
    state varchar(128) not null,
    title varchar(255) not null,
    primary key (training_definition_id)
);

create table training_definition_user (
    training_definition_id int8 not null,
    user_id int8 not null,
    primary key (training_definition_id, user_id),
    foreign key (training_definition_id) references training_definition,
    foreign key (user_id) references "user"
);

create table training_instance (
    training_instance_id bigserial not null,
    access_token varchar(255) not null,
    end_time timestamp not null,
    pool_id int8,
    start_time timestamp not null,
    title varchar(255) not null,
    training_definition_id int8,
    primary key (training_instance_id),
    foreign key (training_definition_id) references training_definition
);

create table training_instance_user (
    training_instance_id int8 not null,
    user_id int8 not null,
    primary key (training_instance_id, user_id),
    foreign key (training_instance_id) references training_instance,
    foreign key (user_id) references "user"
);

create table training_run (
    training_run_id bigserial not null,
    questionnaire_responses text,
    end_time timestamp not null,
    incorrect_answer_count int4 not null,
    phase_answered boolean,
    solution_taken boolean not null,
    start_time timestamp not null,
    state varchar(128) not null,
    current_phase_id int8 not null,
    current_task_id int8 null,
    user_id int8 not null,
    sandbox_instance_ref_id int8 null,
    training_instance_id int8 not null,
    previous_sandbox_instance_ref_id int8 null,
    primary key (training_instance_id),
    foreign key (training_instance_id) references training_instance,
    foreign key (user_id) references "user"
);

-- PHASES
create table abstract_phase (
    phase_id bigserial not null,
    title varchar(255) not null,
    order_in_training_definition int4 not null,
    training_definition_id int8,
    primary key (phase_id),
    foreign key (training_definition_id) references training_definition
);

-- INFO PHASE
create table info_phase (
    phase_id bigserial not null,
    content text not null,
    primary key (phase_id),
    foreign key (phase_id) references abstract_phase
);

-- TRAINING PHASE
create table training_phase (
    phase_id bigserial not null,
    estimated_duration int4 not null,
    allowed_commands int4 ,
    allowed_wrong_answers int4 not null,
    primary key (phase_id),
    foreign key (phase_id) references abstract_phase
);

create table task (
    task_id bigserial not null,
    title varchar(255) not null,
    content text not null,
    answer varchar(255) not null,
    solution varchar(1048) not null,
    incorrect_answer_limit int4,
    modify_sandbox boolean not null,
    sandbox_change_expected_duration int4 not null,
    order_in_training_phase int4 not null,
    training_phase_id int8 not null,
    primary key (task_id),
    foreign key (training_phase_id) references training_phase
);

create table decision_matrix_row (
    decision_matrix_row_id bigserial not null,
    order_in_training_phase int4 not null,
    assessment_answered double precision not null,
    keyword_used double precision not null,
    completed_in_time double precision not null,
    solution_displayed double precision not null,
    wrong_answers double precision not null,
    training_phase_id int8 not null,
    primary key (decision_matrix_row_id),
    foreign key (training_phase_id) references training_phase
);

-- QUESTIONNAIRE PHASE
create table questionnaire_phase (
    phase_id bigserial not null,
    questionnaire_type varchar(32),
    primary key (phase_id),
    foreign key (phase_id) references abstract_phase
);

create table question (
    question_id bigserial not null,
    question_type varchar(64) not null,
    order_in_questionnaire int4 not null,
    text text not null,
    questionnaire_phase_id  int8 not null,
    primary key (question_id),
    foreign key (questionnaire_phase_id) references questionnaire_phase
);

create table question_choice (
    question_choice_id bigserial not null,
    correct boolean not null,
    text text not null,
    order_in_question int4 not null,
    question_id  int8 not null,
    primary key (question_choice_id),
    foreign key (question_id) references question
);

create table question_phase_relation (
    question_phase_relation_id bigserial not null,
    success_rate int4 not null,
    related_training_phase_id int8 not null,
    order_in_questionnaire int4 not null,
    questionnaire_phase_id int8 not null,
    primary key (question_phase_relation_id),
    foreign key (related_training_phase_id) references training_phase,
    foreign key (questionnaire_phase_id) references questionnaire_phase
);

create table question_phase_relation_question (
    question_phase_relation_id int8 not null,
    question_id int8 not null,
    primary key (question_phase_relation_id, question_id),
    foreign key (question_phase_relation_id) references question_phase_relation,
    foreign key (question_id) references question
);

create table question_answer (
    question_id int8 not null,
    training_run_id int8 not null,
    primary key (question_id, training_run_id),
    foreign key (question_id) references question,
    foreign key (training_run_id) references training_run,
    unique (question_id, training_run_id)
);

create table question_answers (
    question_id int8 not null,
    training_run_id int8 not null,
    answer varchar(255) not null,
    primary key (question_id, training_run_id),
    foreign key (question_id, training_run_id) references question_answer,
    unique (question_id, training_run_id)
);


create table questions_phase_relation_result (
    questions_phase_relation_result_id bigserial not null,
    training_run_id int8 not null,
    question_phase_relation_id int8 not null,
    achieved_result double precision not null,
    primary key (questions_phase_relation_result_id),
    foreign key (question_phase_relation_id) references question_phase_relation
);

create table adaptive_questions_fulfillment (
    adaptive_questions_fulfillment_id bigserial not null,
    training_run_id int8 not null,
    training_phase_id int8 not null,
    fulfilled boolean not null,
    primary key (adaptive_questions_fulfillment_id),
    foreign key (training_phase_id) references training_phase
);

-- ACCESS TOKEN
create table access_token (
    access_token_id bigserial not null,
    access_token varchar(255) not null,
    primary key (access_token_id)
);

-- ACQUISITION LOCK
create table training_run_acquisition_lock (
    training_run_acquisition_lock_id bigserial not null,
    participant_ref_id int8 not null,
    training_instance_id int8 not null,
    creation_time timestamp not null,
    primary key (training_run_acquisition_lock_id),
    foreign key (participant_ref_id) references "user",
    foreign key (training_instance_id) references training_instance
);

alter table training_run
   add constraint FKi9smgl25av8pb1yv3fl4ycby0
   foreign key (current_task_id)
   references task;

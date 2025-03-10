-- USER
CREATE TABLE "user" (
    user_id     bigserial NOT NULL,
    user_ref_id int8      NOT NULL UNIQUE,
    PRIMARY KEY (user_id)
);

-- TRAINING
CREATE TABLE training_definition (
    training_definition_id bigserial    NOT NULL,
    title                  varchar(255) NOT NULL,
    description            text,
    created_at             timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_edited            timestamp    NOT NULL,
    last_edited_by         varchar(127) NOT NULL DEFAULT '',
    estimated_duration     int8,
    outcomes               bytea,
    prerequisites          bytea,
    state                  varchar(128) NOT NULL,
    PRIMARY KEY (training_definition_id)
);

CREATE TABLE mitre_technique (
    mitre_technique_id bigserial   NOT NULL,
    technique_key      varchar(64) NOT NULL UNIQUE,
    PRIMARY KEY (mitre_technique_id)
);

CREATE TABLE training_definition_user (
    training_definition_id int8 NOT NULL,
    user_id                int8 NOT NULL,
    PRIMARY KEY (training_definition_id, user_id),
    FOREIGN KEY (training_definition_id) REFERENCES training_definition,
    FOREIGN KEY (user_id) REFERENCES "user"
);

CREATE TABLE training_instance (
    training_instance_id   bigserial    NOT NULL,
    access_token           varchar(255) NOT NULL UNIQUE,
    end_time               timestamp    NOT NULL,
    pool_id                int8                  DEFAULT (NULL),
    sandbox_definition_id  int8                  DEFAULT (NULL),
    start_time             timestamp    NOT NULL,
    title                  varchar(255) NOT NULL,
    training_definition_id int8,
    show_stepper_bar       boolean      NOT NULL DEFAULT TRUE,
    backward_mode          boolean      NOT NULL DEFAULT (FALSE),
    local_environment      boolean      NOT NULL DEFAULT (FALSE),
    last_edited            timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_edited_by         varchar(127) NOT NULL DEFAULT '',
    PRIMARY KEY (training_instance_id),
    FOREIGN KEY (training_definition_id) REFERENCES training_definition
);

CREATE TABLE training_instance_user (
    training_instance_id int8 NOT NULL,
    user_id              int8 NOT NULL,
    PRIMARY KEY (training_instance_id, user_id),
    FOREIGN KEY (training_instance_id) REFERENCES training_instance,
    FOREIGN KEY (user_id) REFERENCES "user"
);


-- PHASES
CREATE TABLE abstract_phase (
    phase_id                     bigserial    NOT NULL,
    title                        varchar(255) NOT NULL,
    order_in_training_definition int4         NOT NULL,
    training_definition_id       int8,
    PRIMARY KEY (phase_id),
    FOREIGN KEY (training_definition_id) REFERENCES training_definition
);

-- INFO PHASE
CREATE TABLE info_phase (
    phase_id bigserial NOT NULL,
    content  text      NOT NULL,
    PRIMARY KEY (phase_id),
    FOREIGN KEY (phase_id) REFERENCES abstract_phase
);

-- ACCESS PHASE
CREATE TABLE access_phase (
    cloud_content text         NOT NULL,
    local_content text         NOT NULL,
    passkey       varchar(255) NOT NULL,
    phase_id      int8         NOT NULL,
    PRIMARY KEY (phase_id),
    FOREIGN KEY (phase_id) REFERENCES abstract_phase
);

-- TRAINING PHASE
CREATE TABLE training_phase (
    phase_id              bigserial NOT NULL,
    estimated_duration    int4      NOT NULL,
    allowed_commands      int4,
    allowed_wrong_answers int4      NOT NULL,
    PRIMARY KEY (phase_id),
    FOREIGN KEY (phase_id) REFERENCES abstract_phase
);

CREATE TABLE task (
    task_id                          bigserial     NOT NULL,
    title                            varchar(255)  NOT NULL,
    content                          text          NOT NULL,
    answer                           varchar(255)  NOT NULL,
    solution                         varchar(1048) NOT NULL,
    incorrect_answer_limit           int4,
    modify_sandbox                   boolean       NOT NULL,
    sandbox_change_expected_duration int4          NOT NULL,
    order_in_training_phase          int4          NOT NULL,
    training_phase_id                int8          NOT NULL,
    PRIMARY KEY (task_id),
    FOREIGN KEY (training_phase_id) REFERENCES training_phase
);

CREATE TABLE training_run (
    training_run_id                  bigserial    NOT NULL,
    questionnaire_responses          text,
    end_time                         timestamp    NOT NULL,
    incorrect_answer_count           int4         NOT NULL,
    phase_answered                   boolean,
    solution_taken                   boolean      NOT NULL,
    start_time                       timestamp    NOT NULL,
    state                            varchar(128) NOT NULL,
    current_phase_id                 int8         NOT NULL,
    current_task_id                  int8         NULL,
    user_id                          int8         NOT NULL,
    sandbox_instance_ref_id          varchar(36)  NULL,
    training_instance_id             int8         NOT NULL,
    previous_sandbox_instance_ref_id varchar(36)  NULL,
    sandbox_instance_allocation_id   int8,
    PRIMARY KEY (training_run_id),
    FOREIGN KEY (training_instance_id) REFERENCES training_instance,
    FOREIGN KEY (user_id) REFERENCES "user",
    FOREIGN KEY (current_task_id) REFERENCES task
);

CREATE TABLE solution_info (
    training_run_id   bigserial NOT NULL,
    training_phase_id bigserial NOT NULL,
    task_id           bigserial NOT NULL,
    solution_content  text      NOT NULL,
    FOREIGN KEY (training_run_id) REFERENCES training_run
);

CREATE TABLE training_phase_mitre_technique (
    training_phase_id  int8 NOT NULL,
    mitre_technique_id int8 NOT NULL,
    PRIMARY KEY (training_phase_id, mitre_technique_id),
    FOREIGN KEY (training_phase_id) REFERENCES training_phase,
    FOREIGN KEY (mitre_technique_id) REFERENCES mitre_technique
);

CREATE TABLE expected_commands (
    training_phase_id int8         NOT NULL,
    command           varchar(128) NOT NULL,
    PRIMARY KEY (training_phase_id, command),
    FOREIGN KEY (training_phase_id) REFERENCES training_phase
);

CREATE INDEX training_phase_mitre_technique_index
    ON expected_commands (training_phase_id);



CREATE TABLE decision_matrix_row (
    decision_matrix_row_id  bigserial        NOT NULL,
    order_in_training_phase int4             NOT NULL,
    questionnaire_answered  double precision NOT NULL,
    keyword_used            double precision NOT NULL,
    completed_in_time       double precision NOT NULL,
    solution_displayed      double precision NOT NULL,
    wrong_answers           double precision NOT NULL,
    training_phase_id       int8             NOT NULL,
    PRIMARY KEY (decision_matrix_row_id),
    FOREIGN KEY (training_phase_id) REFERENCES training_phase
);

-- QUESTIONNAIRE PHASE
CREATE TABLE questionnaire_phase (
    phase_id           bigserial NOT NULL,
    questionnaire_type varchar(32),
    PRIMARY KEY (phase_id),
    FOREIGN KEY (phase_id) REFERENCES abstract_phase
);

CREATE TABLE question (
    question_id            bigserial   NOT NULL,
    question_type          varchar(64) NOT NULL,
    order_in_questionnaire int4        NOT NULL,
    text                   text        NOT NULL,
    questionnaire_phase_id int8        NOT NULL,
    answer_required        boolean,
    PRIMARY KEY (question_id),
    FOREIGN KEY (questionnaire_phase_id) REFERENCES questionnaire_phase
);

CREATE TABLE question_choice (
    question_choice_id bigserial NOT NULL,
    correct            boolean   NOT NULL,
    text               text      NOT NULL,
    order_in_question  int4      NOT NULL,
    question_id        int8      NOT NULL,
    PRIMARY KEY (question_choice_id),
    FOREIGN KEY (question_id) REFERENCES question
);

CREATE TABLE question_phase_relation (
    question_phase_relation_id bigserial NOT NULL,
    success_rate               int4      NOT NULL,
    related_training_phase_id  int8      NOT NULL,
    order_in_questionnaire     int4      NOT NULL,
    questionnaire_phase_id     int8      NOT NULL,
    PRIMARY KEY (question_phase_relation_id),
    FOREIGN KEY (related_training_phase_id) REFERENCES training_phase,
    FOREIGN KEY (questionnaire_phase_id) REFERENCES questionnaire_phase
);

CREATE TABLE question_phase_relation_question (
    question_phase_relation_id int8 NOT NULL,
    question_id                int8 NOT NULL,
    PRIMARY KEY (question_phase_relation_id, question_id),
    FOREIGN KEY (question_phase_relation_id) REFERENCES question_phase_relation,
    FOREIGN KEY (question_id) REFERENCES question
);

CREATE TABLE question_answer (
    question_id     int8 NOT NULL,
    training_run_id int8 NOT NULL,
    PRIMARY KEY (question_id, training_run_id),
    FOREIGN KEY (question_id) REFERENCES question,
    FOREIGN KEY (training_run_id) REFERENCES training_run,
    UNIQUE (question_id, training_run_id)
);

CREATE TABLE question_answers (
    question_id     int8          NOT NULL,
    training_run_id int8          NOT NULL,
    answer          varchar(1023) NOT NULL,
    FOREIGN KEY (question_id, training_run_id) REFERENCES question_answer
);

CREATE TABLE questions_phase_relation_result (
    questions_phase_relation_result_id bigserial        NOT NULL,
    training_run_id                    int8             NOT NULL,
    question_phase_relation_id         int8             NOT NULL,
    achieved_result                    double precision NOT NULL,
    PRIMARY KEY (questions_phase_relation_result_id),
    FOREIGN KEY (question_phase_relation_id) REFERENCES question_phase_relation
);

CREATE TABLE adaptive_questions_fulfillment (
    adaptive_questions_fulfillment_id bigserial NOT NULL,
    training_run_id                   int8      NOT NULL,
    training_phase_id                 int8      NOT NULL,
    fulfilled                         boolean   NOT NULL,
    PRIMARY KEY (adaptive_questions_fulfillment_id),
    FOREIGN KEY (training_phase_id) REFERENCES training_phase
);

CREATE TABLE participant_task_assignment (
    participant_task_assignment_id bigserial NOT NULL,
    phase_id                       int8      NOT NULL,
    training_run_id                int8      NOT NULL,
    task_id                        int8,
    PRIMARY KEY (participant_task_assignment_id),
    FOREIGN KEY (phase_id) REFERENCES abstract_phase,
    FOREIGN KEY (training_run_id) REFERENCES training_run,
    FOREIGN KEY (task_id) REFERENCES task
);

CREATE TABLE submission (
    submission_id   bigserial    NOT NULL,
    provided        text         NOT NULL,
    type            varchar(255) NOT NULL,
    phase_id        int8         NOT NULL,
    task_id         int8         NOT NULL,
    training_run_id int8         NOT NULL,
    date            timestamp    NOT NULL,
    ip_address      varchar(255) NOT NULL,
    PRIMARY KEY (submission_id),
    FOREIGN KEY (phase_id) REFERENCES abstract_phase,
    FOREIGN KEY (task_id) REFERENCES task,
    FOREIGN KEY (training_run_id) REFERENCES training_run
);

-- ACCESS TOKEN
CREATE TABLE access_token (
    access_token_id bigserial    NOT NULL,
    access_token    varchar(255) NOT NULL UNIQUE,
    PRIMARY KEY (access_token_id)
);

-- ACQUISITION LOCK
CREATE TABLE training_run_acquisition_lock (
    training_run_acquisition_lock_id bigserial NOT NULL,
    participant_ref_id               bigserial NOT NULL,
    training_instance_id             bigserial NOT NULL,
    creation_time                    timestamp NOT NULL,
    PRIMARY KEY (training_run_acquisition_lock_id),
    UNIQUE (participant_ref_id, training_instance_id)
);

CREATE INDEX abstract_phase_order_in_training_definition_index
    ON abstract_phase (order_in_training_definition);

CREATE INDEX training_instance_start_time_and_end_time_index
    ON training_instance (start_time, end_time DESC);

CREATE INDEX training_definition_state_index
    ON training_definition (state);

CREATE INDEX training_run_start_time_and_end_time_index
    ON training_run (start_time, end_time DESC);

CREATE SEQUENCE phase_seq AS bigint INCREMENT 50 MINVALUE 1;
CREATE SEQUENCE task_seq AS bigint INCREMENT 50 MINVALUE 1;
CREATE SEQUENCE question_seq AS bigint INCREMENT 50 MINVALUE 1;
CREATE SEQUENCE question_choice_seq AS bigint INCREMENT 50 MINVALUE 1;
CREATE SEQUENCE question_phase_seq AS bigint INCREMENT 50 MINVALUE 1;
CREATE SEQUENCE decision_matrix_row_seq AS bigint INCREMENT 50 MINVALUE 1;
CREATE SEQUENCE adaptive_questions_fulfillment_seq AS bigint INCREMENT 50 MINVALUE 1;
CREATE SEQUENCE questions_phase_relation_result_seq AS bigint INCREMENT 50 MINVALUE 1;
CREATE SEQUENCE training_definition_seq AS bigint INCREMENT 50 MINVALUE 1;
CREATE SEQUENCE training_instance_seq AS bigint INCREMENT 50 MINVALUE 1;
CREATE SEQUENCE training_run_seq AS bigint INCREMENT 50 MINVALUE 1;
CREATE SEQUENCE access_token_seq AS bigint INCREMENT 50 MINVALUE 1;
CREATE SEQUENCE tr_acquisition_lock_seq AS bigint INCREMENT 50 MINVALUE 1;
CREATE SEQUENCE user_seq AS bigint INCREMENT 50 MINVALUE 1;
CREATE SEQUENCE participant_task_assignment_seq AS bigint INCREMENT 50 MINVALUE 1;
CREATE SEQUENCE submission_seq AS bigint INCREMENT 50 MINVALUE 1;
CREATE SEQUENCE mitre_technique_seq AS bigint INCREMENT 50 MINVALUE 1;
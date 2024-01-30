create table pnt_clockin
(
    id             serial not null constraint pnt_clockin_pk primary key,
    created_at     timestamp,
    updated_at     timestamp,
    clock_in       timestamp not null,
    user_id int not null constraint pnt_user_clockin references pnt_user
);
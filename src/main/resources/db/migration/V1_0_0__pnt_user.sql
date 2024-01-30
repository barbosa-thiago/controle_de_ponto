create table pnt_user
(
    id         serial not null constraint pnt_user_pk primary key,
    created_at timestamp,
    updated_at timestamp,
    password   varchar(120) not null,
    role       varchar(15) not null,
    username   varchar(80) not null
);

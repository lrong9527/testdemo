create table student
(
    stu_id      varchar(11) not null
        primary key,
    stu_name    varchar(11) null,
    stu_score   int         null,
    stu_balance float       null
);

create table user
(
    id       int default 0 null,
    username varchar(10)   not null
        primary key,
    password varchar(100)  null,
    role     varchar(15)   not null
);



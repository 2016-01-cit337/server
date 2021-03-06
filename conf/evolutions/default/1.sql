# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table board (
  id                            bigint not null,
  title                         varchar(255),
  user_id                       bigint,
  content                       varchar(255),
  created_date                  bigint,
  constraint pk_board primary key (id)
);
create sequence board_seq;

create table post (
  id                            bigint not null,
  board_id                      bigint,
  user_id                       bigint,
  content                       varchar(255),
  created_date                  bigint,
  constraint pk_post primary key (id)
);
create sequence post_seq;

create table user (
  id                            bigint not null,
  first_name                    varchar(255),
  last_name                     varchar(255),
  email                         varchar(255),
  password                      varchar(255),
  created_date                  bigint,
  token                         varchar(255),
  constraint pk_user primary key (id)
);
create sequence user_seq;

alter table board add constraint fk_board_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_board_user_id on board (user_id);

alter table post add constraint fk_post_board_id foreign key (board_id) references board (id) on delete restrict on update restrict;
create index ix_post_board_id on post (board_id);

alter table post add constraint fk_post_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_post_user_id on post (user_id);


# --- !Downs

alter table board drop constraint if exists fk_board_user_id;
drop index if exists ix_board_user_id;

alter table post drop constraint if exists fk_post_board_id;
drop index if exists ix_post_board_id;

alter table post drop constraint if exists fk_post_user_id;
drop index if exists ix_post_user_id;

drop table if exists board;
drop sequence if exists board_seq;

drop table if exists post;
drop sequence if exists post_seq;

drop table if exists user;
drop sequence if exists user_seq;


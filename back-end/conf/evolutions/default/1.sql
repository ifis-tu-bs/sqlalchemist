# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table avatar (
  id                            bigint auto_increment not null,
  avatar_name                   varchar(255),
  avatardesc                    varchar(255),
  avatar_filename               varchar(255),
  is_team                       boolean,
  health                        integer,
  defense                       integer,
  speed                         integer,
  jump                          integer,
  slot                          integer,
  constraint uq_avatar_avatar_name unique (avatar_name),
  constraint uq_avatar_avatar_filename unique (avatar_filename),
  constraint pk_avatar primary key (id)
);

create table columndefinition (
  id                            bigint not null,
  table_definition_id           bigint,
  column_name                   varchar(255),
  data_type                     varchar(255),
  is_primary_key                boolean,
  is_not_nullable               boolean,
  foreign_key_id                bigint,
  datagen_set                   integer,
  constraint pk_columndefinition primary key (id)
);
create sequence columndefinition_seq;

create table comment (
  id                            bigint not null,
  task_set_id                   bigint,
  task_id                       bigint,
  profile_id                    bigint,
  comment                       varchar(255),
  created_at                    timestamp,
  constraint pk_comment primary key (id)
);
create sequence comment_seq;

create table foreignkeyrelation (
  id                            bigint not null,
  task_set_id                   bigint,
  source_table                  varchar(255),
  source_column                 varchar(255),
  destination_table             varchar(255),
  destination_column            varchar(255),
  constraint pk_foreignkeyrelation primary key (id)
);
create sequence foreignkeyrelation_seq;

create table homework (
  id                            bigint not null,
  creator_id                    bigint,
  start_at                      timestamp,
  expire_at                     timestamp,
  home_work_name                varchar(255),
  constraint pk_homework primary key (id)
);
create sequence homework_seq;

create table homework_taskset (
  homework_id                   bigint not null,
  taskset_id                    bigint not null,
  constraint pk_homework_taskset primary key (homework_id,taskset_id)
);

create table inventory (
  id                            bigint not null,
  profile_id                    bigint,
  potion_id                     bigint,
  beltslot                      integer,
  constraint pk_inventory primary key (id)
);
create sequence inventory_seq;

create table loficoinflowlog (
  id                            bigint not null,
  profile_id                    bigint,
  lofi_coins_collected          integer,
  collected                     timestamp,
  constraint pk_loficoinflowlog primary key (id)
);
create sequence loficoinflowlog_seq;

create table map (
  id                            bigint not null,
  level                         integer,
  path                          varchar(255),
  boss_map                      boolean,
  constraint uq_map_path unique (path),
  constraint pk_map primary key (id)
);
create sequence map_seq;

create table potion (
  id                            bigint not null,
  potionname                    varchar(255),
  type                          integer,
  powerlevel                    integer,
  buffvalue                     integer,
  health                        integer,
  defense                       integer,
  speed                         integer,
  jump                          integer,
  slot                          integer,
  constraint uq_potion_potionname unique (potionname),
  constraint uq_potion_type_powerlevel unique (type,powerlevel),
  constraint pk_potion primary key (id)
);
create sequence potion_seq;

create table profile (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  username                      varchar(255),
  health                        integer,
  defense                       integer,
  speed                         integer,
  jump                          integer,
  slot                          integer,
  sound                         boolean,
  music                         boolean,
  tutorial_done                 boolean,
  story_done                    boolean,
  avatar_id                     bigint,
  current_story_id              bigint,
  current_scroll_id             bigint,
  depth                         integer,
  coins                         integer,
  coin_scale                    float,
  scroll_limit                  integer,
  total_score                   integer,
  played_time                   integer,
  played_runs                   integer,
  totalcoins                    integer,
  done_sql                      integer,
  solved_sql                    integer,
  quote                         integer,
  created_at                    timestamp,
  edited_at                     timestamp,
  current_task_set_id           bigint,
  current_home_work_id          bigint,
  version                       timestamp not null,
  constraint uq_profile_user_id unique (user_id),
  constraint uq_profile_username unique (username),
  constraint uq_profile_current_scroll_id unique (current_scroll_id),
  constraint pk_profile primary key (id)
);

create table profile_shopitem (
  profile_id                    bigint not null,
  shopitem_id                   bigint not null,
  constraint pk_profile_shopitem primary key (profile_id,shopitem_id)
);

create table rating (
  id                            bigint not null,
  profile_id                    bigint,
  task_set_id                   bigint,
  task_id                       bigint,
  positive_ratings              bigint,
  negative_ratings              bigint,
  edit_ratings                  bigint,
  constraint pk_rating primary key (id)
);
create sequence rating_seq;

create table scroll (
  id                            bigint not null,
  posid                         integer,
  scrollname                    varchar(255),
  type                          integer,
  potion_id                     bigint,
  health                        integer,
  defense                       integer,
  speed                         integer,
  jump                          integer,
  slot                          integer,
  constraint uq_scroll_posid unique (posid),
  constraint uq_scroll_scrollname unique (scrollname),
  constraint uq_scroll_potion_id unique (potion_id),
  constraint pk_scroll primary key (id)
);
create sequence scroll_seq;

create table scrollcollection (
  id                            bigint not null,
  profile_id                    bigint,
  scroll_id                     bigint,
  is_active                     boolean,
  added                         timestamp,
  constraint pk_scrollcollection primary key (id)
);
create sequence scrollcollection_seq;

create table shopitem (
  id                            bigint not null,
  type                          integer,
  shopitemname                  varchar(255),
  shopitem_desc                 varchar(255),
  thumbnail_url                 varchar(255),
  price                         integer,
  avatar_id                     bigint,
  constraint uq_shopitem_avatar_id unique (avatar_id),
  constraint pk_shopitem primary key (id)
);
create sequence shopitem_seq;

create table solvedsubtask (
  id                            bigint not null,
  profile_id                    bigint,
  task_id                       bigint,
  solved                        integer,
  trys                          integer,
  last_solved                   timestamp,
  constraint pk_solvedsubtask primary key (id)
);
create sequence solvedsubtask_seq;

create table storychallenge (
  id                            bigint not null,
  name                          varchar(255),
  is_tutorial                   boolean,
  level                         integer,
  next_id                       bigint,
  created_at                    timestamp,
  constraint uq_storychallenge_next_id unique (next_id),
  constraint pk_storychallenge primary key (id)
);
create sequence storychallenge_seq;

create table storychallenge_text (
  storychallenge_id             bigint not null,
  text_id                       bigint not null,
  constraint pk_storychallenge_text primary key (storychallenge_id,text_id)
);

create table storychallenge_map (
  storychallenge_id             bigint not null,
  map_id                        bigint not null,
  constraint pk_storychallenge_map primary key (storychallenge_id,map_id)
);

create table submittedhomework (
  id                            bigint not null,
  profile_id                    bigint,
  task_id                       bigint,
  home_work_id                  bigint,
  syntax_checks_done            integer,
  semantic_checks_done          integer,
  statement                     varchar(255),
  solve                         boolean,
  constraint uq_submittedhomework_profile_id_task_id_home_work_id unique (profile_id,task_id,home_work_id),
  constraint pk_submittedhomework primary key (id)
);
create sequence submittedhomework_seq;

create table tabledefinition (
  id                            bigint not null,
  task_set_id                   bigint,
  table_name                    varchar(255),
  extension                     Text,
  constraint pk_tabledefinition primary key (id)
);
create sequence tabledefinition_seq;

create table task (
  id                            bigint not null,
  task_set_id                   bigint,
  task_name                     varchar(255),
  task_text                     varchar(255),
  ref_statement                 varchar(255),
  evaluation_strategy           integer,
  points                        integer,
  required_term                 integer,
  creator_id                    bigint,
  available                     boolean,
  created_at                    timestamp,
  updated_at                    timestamp,
  available_syntax_checks       integer,
  available_semantic_checks     integer,
  constraint pk_task primary key (id)
);
create sequence task_seq;

create table taskset (
  id                            bigint not null,
  task_set_name                 varchar(255),
  is_homework                   boolean,
  creator_id                    bigint,
  available                     boolean,
  created_at                    timestamp,
  updated_at                    timestamp,
  constraint pk_taskset primary key (id)
);
create sequence taskset_seq;

create table taskset_homework (
  taskset_id                    bigint not null,
  homework_id                   bigint not null,
  constraint pk_taskset_homework primary key (taskset_id,homework_id)
);

create table text (
  id                            bigint not null,
  type                          integer,
  prerequisite                  integer,
  chronology                    integer,
  text                          Text,
  sound_url                     varchar(255),
  count_of_lines                integer,
  constraint pk_text primary key (id)
);
create sequence text_seq;

create table user (
  id                            bigint auto_increment not null,
  email                         varchar(255),
  email_verified                boolean,
  email_verify_code             varchar(255),
  user_mnr                      varchar(255),
  user_ynr                      varchar(255),
  is_student                    boolean,
  password                      varchar(255),
  user_role                     integer,
  created_at                    timestamp,
  edited_at                     timestamp,
  constraint uq_user_email unique (email),
  constraint uq_user_email_verify_code unique (email_verify_code),
  constraint uq_user_user_mnr unique (user_mnr),
  constraint uq_user_user_ynr unique (user_ynr),
  constraint pk_user primary key (id)
);

create table usersession (
  id                            bigint not null,
  sessionid                     varchar(30),
  user_id                       bigint,
  remoteaddress                 varchar(255),
  created_at                    timestamp,
  expires_at                    timestamp,
  constraint uq_usersession_sessionid unique (sessionid),
  constraint pk_usersession primary key (id)
);
create sequence usersession_seq;

alter table columndefinition add constraint fk_columndefinition_table_definition_id foreign key (table_definition_id) references tabledefinition (id) on delete restrict on update restrict;
create index ix_columndefinition_table_definition_id on columndefinition (table_definition_id);

alter table columndefinition add constraint fk_columndefinition_foreign_key_id foreign key (foreign_key_id) references columndefinition (id) on delete restrict on update restrict;
create index ix_columndefinition_foreign_key_id on columndefinition (foreign_key_id);

alter table comment add constraint fk_comment_task_set_id foreign key (task_set_id) references taskset (id) on delete restrict on update restrict;
create index ix_comment_task_set_id on comment (task_set_id);

alter table comment add constraint fk_comment_task_id foreign key (task_id) references task (id) on delete restrict on update restrict;
create index ix_comment_task_id on comment (task_id);

alter table comment add constraint fk_comment_profile_id foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_comment_profile_id on comment (profile_id);

alter table foreignkeyrelation add constraint fk_foreignkeyrelation_task_set_id foreign key (task_set_id) references taskset (id) on delete restrict on update restrict;
create index ix_foreignkeyrelation_task_set_id on foreignkeyrelation (task_set_id);

alter table homework add constraint fk_homework_creator_id foreign key (creator_id) references profile (id) on delete restrict on update restrict;
create index ix_homework_creator_id on homework (creator_id);

alter table homework_taskset add constraint fk_homework_taskset_homework foreign key (homework_id) references homework (id) on delete restrict on update restrict;
create index ix_homework_taskset_homework on homework_taskset (homework_id);

alter table homework_taskset add constraint fk_homework_taskset_taskset foreign key (taskset_id) references taskset (id) on delete restrict on update restrict;
create index ix_homework_taskset_taskset on homework_taskset (taskset_id);

alter table inventory add constraint fk_inventory_profile_id foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_inventory_profile_id on inventory (profile_id);

alter table inventory add constraint fk_inventory_potion_id foreign key (potion_id) references potion (id) on delete restrict on update restrict;
create index ix_inventory_potion_id on inventory (potion_id);

alter table loficoinflowlog add constraint fk_loficoinflowlog_profile_id foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_loficoinflowlog_profile_id on loficoinflowlog (profile_id);

alter table profile add constraint fk_profile_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table profile add constraint fk_profile_avatar_id foreign key (avatar_id) references avatar (id) on delete restrict on update restrict;
create index ix_profile_avatar_id on profile (avatar_id);

alter table profile add constraint fk_profile_current_story_id foreign key (current_story_id) references storychallenge (id) on delete restrict on update restrict;
create index ix_profile_current_story_id on profile (current_story_id);

alter table profile add constraint fk_profile_current_scroll_id foreign key (current_scroll_id) references scroll (id) on delete restrict on update restrict;

alter table profile add constraint fk_profile_current_task_set_id foreign key (current_task_set_id) references taskset (id) on delete restrict on update restrict;
create index ix_profile_current_task_set_id on profile (current_task_set_id);

alter table profile add constraint fk_profile_current_home_work_id foreign key (current_home_work_id) references homework (id) on delete restrict on update restrict;
create index ix_profile_current_home_work_id on profile (current_home_work_id);

alter table profile_shopitem add constraint fk_profile_shopitem_profile foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_profile_shopitem_profile on profile_shopitem (profile_id);

alter table profile_shopitem add constraint fk_profile_shopitem_shopitem foreign key (shopitem_id) references shopitem (id) on delete restrict on update restrict;
create index ix_profile_shopitem_shopitem on profile_shopitem (shopitem_id);

alter table rating add constraint fk_rating_profile_id foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_rating_profile_id on rating (profile_id);

alter table rating add constraint fk_rating_task_set_id foreign key (task_set_id) references taskset (id) on delete restrict on update restrict;
create index ix_rating_task_set_id on rating (task_set_id);

alter table rating add constraint fk_rating_task_id foreign key (task_id) references task (id) on delete restrict on update restrict;
create index ix_rating_task_id on rating (task_id);

alter table scroll add constraint fk_scroll_potion_id foreign key (potion_id) references potion (id) on delete restrict on update restrict;

alter table scrollcollection add constraint fk_scrollcollection_profile_id foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_scrollcollection_profile_id on scrollcollection (profile_id);

alter table scrollcollection add constraint fk_scrollcollection_scroll_id foreign key (scroll_id) references scroll (id) on delete restrict on update restrict;
create index ix_scrollcollection_scroll_id on scrollcollection (scroll_id);

alter table shopitem add constraint fk_shopitem_avatar_id foreign key (avatar_id) references avatar (id) on delete restrict on update restrict;

alter table solvedsubtask add constraint fk_solvedsubtask_profile_id foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_solvedsubtask_profile_id on solvedsubtask (profile_id);

alter table solvedsubtask add constraint fk_solvedsubtask_task_id foreign key (task_id) references task (id) on delete restrict on update restrict;
create index ix_solvedsubtask_task_id on solvedsubtask (task_id);

alter table storychallenge add constraint fk_storychallenge_next_id foreign key (next_id) references storychallenge (id) on delete restrict on update restrict;

alter table storychallenge_text add constraint fk_storychallenge_text_storychallenge foreign key (storychallenge_id) references storychallenge (id) on delete restrict on update restrict;
create index ix_storychallenge_text_storychallenge on storychallenge_text (storychallenge_id);

alter table storychallenge_text add constraint fk_storychallenge_text_text foreign key (text_id) references text (id) on delete restrict on update restrict;
create index ix_storychallenge_text_text on storychallenge_text (text_id);

alter table storychallenge_map add constraint fk_storychallenge_map_storychallenge foreign key (storychallenge_id) references storychallenge (id) on delete restrict on update restrict;
create index ix_storychallenge_map_storychallenge on storychallenge_map (storychallenge_id);

alter table storychallenge_map add constraint fk_storychallenge_map_map foreign key (map_id) references map (id) on delete restrict on update restrict;
create index ix_storychallenge_map_map on storychallenge_map (map_id);

alter table submittedhomework add constraint fk_submittedhomework_profile_id foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_submittedhomework_profile_id on submittedhomework (profile_id);

alter table submittedhomework add constraint fk_submittedhomework_task_id foreign key (task_id) references task (id) on delete restrict on update restrict;
create index ix_submittedhomework_task_id on submittedhomework (task_id);

alter table submittedhomework add constraint fk_submittedhomework_home_work_id foreign key (home_work_id) references homework (id) on delete restrict on update restrict;
create index ix_submittedhomework_home_work_id on submittedhomework (home_work_id);

alter table tabledefinition add constraint fk_tabledefinition_task_set_id foreign key (task_set_id) references taskset (id) on delete restrict on update restrict;
create index ix_tabledefinition_task_set_id on tabledefinition (task_set_id);

alter table task add constraint fk_task_task_set_id foreign key (task_set_id) references taskset (id) on delete restrict on update restrict;
create index ix_task_task_set_id on task (task_set_id);

alter table task add constraint fk_task_creator_id foreign key (creator_id) references profile (id) on delete restrict on update restrict;
create index ix_task_creator_id on task (creator_id);

alter table taskset add constraint fk_taskset_creator_id foreign key (creator_id) references profile (id) on delete restrict on update restrict;
create index ix_taskset_creator_id on taskset (creator_id);

alter table taskset_homework add constraint fk_taskset_homework_taskset foreign key (taskset_id) references taskset (id) on delete restrict on update restrict;
create index ix_taskset_homework_taskset on taskset_homework (taskset_id);

alter table taskset_homework add constraint fk_taskset_homework_homework foreign key (homework_id) references homework (id) on delete restrict on update restrict;
create index ix_taskset_homework_homework on taskset_homework (homework_id);

alter table usersession add constraint fk_usersession_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_usersession_user_id on usersession (user_id);


# --- !Downs

alter table columndefinition drop constraint if exists fk_columndefinition_table_definition_id;
drop index if exists ix_columndefinition_table_definition_id;

alter table columndefinition drop constraint if exists fk_columndefinition_foreign_key_id;
drop index if exists ix_columndefinition_foreign_key_id;

alter table comment drop constraint if exists fk_comment_task_set_id;
drop index if exists ix_comment_task_set_id;

alter table comment drop constraint if exists fk_comment_task_id;
drop index if exists ix_comment_task_id;

alter table comment drop constraint if exists fk_comment_profile_id;
drop index if exists ix_comment_profile_id;

alter table foreignkeyrelation drop constraint if exists fk_foreignkeyrelation_task_set_id;
drop index if exists ix_foreignkeyrelation_task_set_id;

alter table homework drop constraint if exists fk_homework_creator_id;
drop index if exists ix_homework_creator_id;

alter table homework_taskset drop constraint if exists fk_homework_taskset_homework;
drop index if exists ix_homework_taskset_homework;

alter table homework_taskset drop constraint if exists fk_homework_taskset_taskset;
drop index if exists ix_homework_taskset_taskset;

alter table inventory drop constraint if exists fk_inventory_profile_id;
drop index if exists ix_inventory_profile_id;

alter table inventory drop constraint if exists fk_inventory_potion_id;
drop index if exists ix_inventory_potion_id;

alter table loficoinflowlog drop constraint if exists fk_loficoinflowlog_profile_id;
drop index if exists ix_loficoinflowlog_profile_id;

alter table profile drop constraint if exists fk_profile_user_id;

alter table profile drop constraint if exists fk_profile_avatar_id;
drop index if exists ix_profile_avatar_id;

alter table profile drop constraint if exists fk_profile_current_story_id;
drop index if exists ix_profile_current_story_id;

alter table profile drop constraint if exists fk_profile_current_scroll_id;

alter table profile drop constraint if exists fk_profile_current_task_set_id;
drop index if exists ix_profile_current_task_set_id;

alter table profile drop constraint if exists fk_profile_current_home_work_id;
drop index if exists ix_profile_current_home_work_id;

alter table profile_shopitem drop constraint if exists fk_profile_shopitem_profile;
drop index if exists ix_profile_shopitem_profile;

alter table profile_shopitem drop constraint if exists fk_profile_shopitem_shopitem;
drop index if exists ix_profile_shopitem_shopitem;

alter table rating drop constraint if exists fk_rating_profile_id;
drop index if exists ix_rating_profile_id;

alter table rating drop constraint if exists fk_rating_task_set_id;
drop index if exists ix_rating_task_set_id;

alter table rating drop constraint if exists fk_rating_task_id;
drop index if exists ix_rating_task_id;

alter table scroll drop constraint if exists fk_scroll_potion_id;

alter table scrollcollection drop constraint if exists fk_scrollcollection_profile_id;
drop index if exists ix_scrollcollection_profile_id;

alter table scrollcollection drop constraint if exists fk_scrollcollection_scroll_id;
drop index if exists ix_scrollcollection_scroll_id;

alter table shopitem drop constraint if exists fk_shopitem_avatar_id;

alter table solvedsubtask drop constraint if exists fk_solvedsubtask_profile_id;
drop index if exists ix_solvedsubtask_profile_id;

alter table solvedsubtask drop constraint if exists fk_solvedsubtask_task_id;
drop index if exists ix_solvedsubtask_task_id;

alter table storychallenge drop constraint if exists fk_storychallenge_next_id;

alter table storychallenge_text drop constraint if exists fk_storychallenge_text_storychallenge;
drop index if exists ix_storychallenge_text_storychallenge;

alter table storychallenge_text drop constraint if exists fk_storychallenge_text_text;
drop index if exists ix_storychallenge_text_text;

alter table storychallenge_map drop constraint if exists fk_storychallenge_map_storychallenge;
drop index if exists ix_storychallenge_map_storychallenge;

alter table storychallenge_map drop constraint if exists fk_storychallenge_map_map;
drop index if exists ix_storychallenge_map_map;

alter table submittedhomework drop constraint if exists fk_submittedhomework_profile_id;
drop index if exists ix_submittedhomework_profile_id;

alter table submittedhomework drop constraint if exists fk_submittedhomework_task_id;
drop index if exists ix_submittedhomework_task_id;

alter table submittedhomework drop constraint if exists fk_submittedhomework_home_work_id;
drop index if exists ix_submittedhomework_home_work_id;

alter table tabledefinition drop constraint if exists fk_tabledefinition_task_set_id;
drop index if exists ix_tabledefinition_task_set_id;

alter table task drop constraint if exists fk_task_task_set_id;
drop index if exists ix_task_task_set_id;

alter table task drop constraint if exists fk_task_creator_id;
drop index if exists ix_task_creator_id;

alter table taskset drop constraint if exists fk_taskset_creator_id;
drop index if exists ix_taskset_creator_id;

alter table taskset_homework drop constraint if exists fk_taskset_homework_taskset;
drop index if exists ix_taskset_homework_taskset;

alter table taskset_homework drop constraint if exists fk_taskset_homework_homework;
drop index if exists ix_taskset_homework_homework;

alter table usersession drop constraint if exists fk_usersession_user_id;
drop index if exists ix_usersession_user_id;

drop table if exists avatar;

drop table if exists columndefinition;
drop sequence if exists columndefinition_seq;

drop table if exists comment;
drop sequence if exists comment_seq;

drop table if exists foreignkeyrelation;
drop sequence if exists foreignkeyrelation_seq;

drop table if exists homework;
drop sequence if exists homework_seq;

drop table if exists homework_taskset;

drop table if exists inventory;
drop sequence if exists inventory_seq;

drop table if exists loficoinflowlog;
drop sequence if exists loficoinflowlog_seq;

drop table if exists map;
drop sequence if exists map_seq;

drop table if exists potion;
drop sequence if exists potion_seq;

drop table if exists profile;

drop table if exists profile_shopitem;

drop table if exists rating;
drop sequence if exists rating_seq;

drop table if exists scroll;
drop sequence if exists scroll_seq;

drop table if exists scrollcollection;
drop sequence if exists scrollcollection_seq;

drop table if exists shopitem;
drop sequence if exists shopitem_seq;

drop table if exists solvedsubtask;
drop sequence if exists solvedsubtask_seq;

drop table if exists storychallenge;
drop sequence if exists storychallenge_seq;

drop table if exists storychallenge_text;

drop table if exists storychallenge_map;

drop table if exists submittedhomework;
drop sequence if exists submittedhomework_seq;

drop table if exists tabledefinition;
drop sequence if exists tabledefinition_seq;

drop table if exists task;
drop sequence if exists task_seq;

drop table if exists taskset;
drop sequence if exists taskset_seq;

drop table if exists taskset_homework;

drop table if exists text;
drop sequence if exists text_seq;

drop table if exists user;

drop table if exists usersession;
drop sequence if exists usersession_seq;


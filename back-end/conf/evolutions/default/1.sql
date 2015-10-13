# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table avatar (
  id                        bigint auto_increment not null,
  avatar_name               varchar(255),
  Avatardesc                varchar(255),
  avatar_filename           varchar(255),
  is_team                   tinyint(1) default 0,
  soundURL                  varchar(255),
  health                    integer,
  defense                   integer,
  speed                     integer,
  jump                      integer,
  slot                      integer,
  constraint uq_avatar_avatar_name unique (avatar_name),
  constraint uq_avatar_avatar_filename unique (avatar_filename),
  constraint pk_avatar primary key (id))
;

create table challenge (
  id                        bigint auto_increment not null,
  challenge_name            varchar(255),
  solve_type                integer,
  solve_type_extension      integer,
  created_at                datetime,
  type                      integer,
  modified_at               datetime,
  constraint pk_challenge primary key (id))
;

create table ColumnDefinition (
  id                        bigint auto_increment not null,
  table_definition_id       bigint,
  column_name               varchar(255),
  data_type                 varchar(255),
  is_primary_key            tinyint(1) default 0,
  is_not_nullable           tinyint(1) default 0,
  foreign_key_id            bigint,
  datagen_set               integer,
  constraint pk_ColumnDefinition primary key (id))
;

create table Comment (
  id                        bigint auto_increment not null,
  task_id                   bigint not null,
  profile_id                bigint,
  comment                   varchar(255),
  created_at                datetime,
  constraint pk_Comment primary key (id))
;

create table ForeignKeyRelation (
  id                        bigint auto_increment not null,
  task_set_id               bigint,
  source_table              varchar(255),
  source_column             varchar(255),
  destination_table         varchar(255),
  destination_column        varchar(255),
  constraint pk_ForeignKeyRelation primary key (id))
;

create table homeWork_challenge (
  id                        bigint auto_increment not null,
  challenge_name            varchar(255),
  solve_type                integer,
  solve_type_extension      integer,
  created_at                datetime,
  type                      integer,
  modified_at               datetime,
  creator_id                bigint,
  start_at                  datetime,
  expires_at                datetime,
  constraint pk_homeWork_challenge primary key (id))
;

create table Inventory (
  id                        bigint auto_increment not null,
  profile_id                bigint,
  potion_id                 bigint,
  beltSlot                  integer,
  constraint pk_Inventory primary key (id))
;

create table lofi_coin_flow_log (
  id                        bigint auto_increment not null,
  profile_id                bigint,
  lofi_coins_collected      integer,
  collected                 datetime,
  constraint pk_lofi_coin_flow_log primary key (id))
;

create table map (
  id                        bigint auto_increment not null,
  level                     integer,
  path                      varchar(255),
  boss_map                  tinyint(1) default 0,
  constraint uq_map_path unique (path),
  constraint pk_map primary key (id))
;

create table potion (
  id                        bigint auto_increment not null,
  potionName                varchar(255),
  type                      integer,
  powerLevel                integer,
  buffValue                 integer,
  health                    integer,
  defense                   integer,
  speed                     integer,
  jump                      integer,
  slot                      integer,
  constraint uq_potion_potionName unique (potionName),
  constraint uq_potion_1 unique (type,powerLevel),
  constraint pk_potion primary key (id))
;

create table profile (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  username                  varchar(255),
  health                    integer,
  defense                   integer,
  speed                     integer,
  jump                      integer,
  slot                      integer,
  sound                     tinyint(1) default 0,
  music                     tinyint(1) default 0,
  tutorial_done             tinyint(1) default 0,
  story_done                tinyint(1) default 0,
  avatar_id                 bigint,
  current_story_id          bigint,
  current_scroll_id         bigint,
  depth                     integer,
  coins                     integer,
  coin_scale                float,
  scroll_limit              integer,
  total_score               integer,
  played_time               integer,
  played_runs               integer,
  totalCoins                integer,
  done_sql                  integer,
  solved_sql                integer,
  quote                     integer,
  created_at                datetime,
  edited_at                 datetime,
  version                   datetime not null,
  constraint uq_profile_username unique (username),
  constraint pk_profile primary key (id))
;

create table rating (
  id                        bigint auto_increment not null,
  task_id                   bigint not null,
  profile_id                bigint,
  positive_ratings          bigint,
  negative_ratings          bigint,
  edit_ratings              bigint,
  constraint pk_rating primary key (id))
;

create table scroll (
  id                        bigint auto_increment not null,
  posId                     integer,
  Scrollname                varchar(255),
  type                      integer,
  potion_id                 bigint,
  health                    integer,
  defense                   integer,
  speed                     integer,
  jump                      integer,
  slot                      integer,
  constraint uq_scroll_posId unique (posId),
  constraint uq_scroll_Scrollname unique (Scrollname),
  constraint pk_scroll primary key (id))
;

create table Scroll_Collection (
  id                        bigint auto_increment not null,
  profile_id                bigint,
  scroll_id                 bigint,
  is_active                 tinyint(1) default 0,
  added                     datetime,
  constraint pk_Scroll_Collection primary key (id))
;

create table ShopItem (
  id                        bigint auto_increment not null,
  type                      integer,
  ShopItemName              varchar(255),
  ShopItem_desc             varchar(255),
  thumbnail_url             varchar(255),
  price                     integer,
  avatar_id                 bigint,
  constraint pk_ShopItem primary key (id))
;

create table solvedSubTask (
  id                        bigint auto_increment not null,
  profile_id                bigint,
  task_id                   bigint,
  solved                    integer,
  trys                      integer,
  last_solved               datetime,
  constraint pk_solvedSubTask primary key (id))
;

create table story_challenge (
  id                        bigint auto_increment not null,
  challenge_name            varchar(255),
  solve_type                integer,
  solve_type_extension      integer,
  created_at                datetime,
  type                      integer,
  modified_at               datetime,
  level                     integer,
  next_id                   bigint,
  constraint pk_story_challenge primary key (id))
;

create table submitted_homework (
  id                        bigint auto_increment not null,
  profile_id                bigint,
  task_id                   bigint,
  solved                    integer,
  trys                      integer,
  last_solved               datetime,
  home_work_id              bigint,
  statement                 varchar(255),
  solve                     tinyint(1) default 0,
  constraint uq_submitted_homework_1 unique (profile_id,task_id, home_work_id),
  constraint pk_submitted_homework primary key (id))
;

create table TableDefinition (
  id                        bigint auto_increment not null,
  task_set_id               bigint,
  table_name                varchar(255),
  extension                 Text,
  constraint pk_TableDefinition primary key (id))
;

create table Task (
  id                        bigint auto_increment not null,
  task_set_id               bigint,
  task_name                 varchar(255),
  task_text                 varchar(255),
  ref_statement             varchar(255),
  evaluationstrategy        integer,
  points                    integer,
  required_term             integer,
  creator_id                bigint,
  created_at                datetime,
  updated_at                datetime,
  constraint pk_Task primary key (id))
;

create table TaskSet (
  id                        bigint auto_increment not null,
  task_set_name             varchar(255),
  relations_formatted       varchar(255),
  is_homework               tinyint(1) default 0,
  creator_id                bigint,
  created_at                datetime,
  updated_at                datetime,
  constraint pk_TaskSet primary key (id))
;

create table texts (
  id                        bigint auto_increment not null,
  type                      integer,
  prerequisite              integer,
  chronology                integer,
  text                      Text,
  sound_url                 varchar(255),
  count_of_lines            integer,
  constraint pk_texts primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  email                     varchar(255),
  email_verified            tinyint(1) default 0,
  email_verify_code         varchar(255),
  user_mnr                  varchar(255),
  user_ynr                  varchar(255),
  is_student                tinyint(1) default 0,
  password_hash             varchar(255),
  password_reset_code       varchar(255),
  user_role                 integer,
  created_at                datetime,
  edited_at                 datetime,
  constraint uq_user_email unique (email),
  constraint uq_user_email_verify_code unique (email_verify_code),
  constraint uq_user_user_mnr unique (user_mnr),
  constraint uq_user_user_ynr unique (user_ynr),
  constraint uq_user_password_reset_code unique (password_reset_code),
  constraint pk_user primary key (id))
;

create table UserSession (
  id                        bigint auto_increment not null,
  sessionID                 varchar(30),
  user_id                   bigint,
  remoteAddress             varchar(255),
  created_at                datetime,
  expires_at                datetime,
  constraint uq_UserSession_sessionID unique (sessionID),
  constraint pk_UserSession primary key (id))
;


create table homeWork_challenge_TaskSet (
  homeWork_challenge_id          bigint not null,
  TaskSet_id                     bigint not null,
  constraint pk_homeWork_challenge_TaskSet primary key (homeWork_challenge_id, TaskSet_id))
;

create table profile_ShopItem (
  profile_id                     bigint not null,
  ShopItem_id                    bigint not null,
  constraint pk_profile_ShopItem primary key (profile_id, ShopItem_id))
;

create table story_challenge_texts (
  story_challenge_id             bigint not null,
  texts_id                       bigint not null,
  constraint pk_story_challenge_texts primary key (story_challenge_id, texts_id))
;

create table story_challenge_map (
  story_challenge_id             bigint not null,
  map_id                         bigint not null,
  constraint pk_story_challenge_map primary key (story_challenge_id, map_id))
;

create table TaskSet_rating (
  TaskSet_id                     bigint not null,
  rating_id                      bigint not null,
  constraint pk_TaskSet_rating primary key (TaskSet_id, rating_id))
;

create table TaskSet_Comment (
  TaskSet_id                     bigint not null,
  Comment_id                     bigint not null,
  constraint pk_TaskSet_Comment primary key (TaskSet_id, Comment_id))
;
alter table ColumnDefinition add constraint fk_ColumnDefinition_tableDefinition_1 foreign key (table_definition_id) references TableDefinition (id) on delete restrict on update restrict;
create index ix_ColumnDefinition_tableDefinition_1 on ColumnDefinition (table_definition_id);
alter table ColumnDefinition add constraint fk_ColumnDefinition_foreignKey_2 foreign key (foreign_key_id) references ColumnDefinition (id) on delete restrict on update restrict;
create index ix_ColumnDefinition_foreignKey_2 on ColumnDefinition (foreign_key_id);
alter table Comment add constraint fk_Comment_Task_3 foreign key (task_id) references Task (id) on delete restrict on update restrict;
create index ix_Comment_Task_3 on Comment (task_id);
alter table Comment add constraint fk_Comment_profile_4 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_Comment_profile_4 on Comment (profile_id);
alter table ForeignKeyRelation add constraint fk_ForeignKeyRelation_taskSet_5 foreign key (task_set_id) references TaskSet (id) on delete restrict on update restrict;
create index ix_ForeignKeyRelation_taskSet_5 on ForeignKeyRelation (task_set_id);
alter table homeWork_challenge add constraint fk_homeWork_challenge_creator_6 foreign key (creator_id) references profile (id) on delete restrict on update restrict;
create index ix_homeWork_challenge_creator_6 on homeWork_challenge (creator_id);
alter table Inventory add constraint fk_Inventory_profile_7 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_Inventory_profile_7 on Inventory (profile_id);
alter table Inventory add constraint fk_Inventory_potion_8 foreign key (potion_id) references potion (id) on delete restrict on update restrict;
create index ix_Inventory_potion_8 on Inventory (potion_id);
alter table lofi_coin_flow_log add constraint fk_lofi_coin_flow_log_profile_9 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_lofi_coin_flow_log_profile_9 on lofi_coin_flow_log (profile_id);
alter table profile add constraint fk_profile_user_10 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_profile_user_10 on profile (user_id);
alter table profile add constraint fk_profile_avatar_11 foreign key (avatar_id) references avatar (id) on delete restrict on update restrict;
create index ix_profile_avatar_11 on profile (avatar_id);
alter table profile add constraint fk_profile_currentStory_12 foreign key (current_story_id) references story_challenge (id) on delete restrict on update restrict;
create index ix_profile_currentStory_12 on profile (current_story_id);
alter table profile add constraint fk_profile_currentScroll_13 foreign key (current_scroll_id) references scroll (id) on delete restrict on update restrict;
create index ix_profile_currentScroll_13 on profile (current_scroll_id);
alter table rating add constraint fk_rating_Task_14 foreign key (task_id) references Task (id) on delete restrict on update restrict;
create index ix_rating_Task_14 on rating (task_id);
alter table rating add constraint fk_rating_profile_15 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_rating_profile_15 on rating (profile_id);
alter table scroll add constraint fk_scroll_potion_16 foreign key (potion_id) references potion (id) on delete restrict on update restrict;
create index ix_scroll_potion_16 on scroll (potion_id);
alter table Scroll_Collection add constraint fk_Scroll_Collection_profile_17 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_Scroll_Collection_profile_17 on Scroll_Collection (profile_id);
alter table Scroll_Collection add constraint fk_Scroll_Collection_scroll_18 foreign key (scroll_id) references scroll (id) on delete restrict on update restrict;
create index ix_Scroll_Collection_scroll_18 on Scroll_Collection (scroll_id);
alter table ShopItem add constraint fk_ShopItem_avatar_19 foreign key (avatar_id) references avatar (id) on delete restrict on update restrict;
create index ix_ShopItem_avatar_19 on ShopItem (avatar_id);
alter table solvedSubTask add constraint fk_solvedSubTask_profile_20 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_solvedSubTask_profile_20 on solvedSubTask (profile_id);
alter table solvedSubTask add constraint fk_solvedSubTask_task_21 foreign key (task_id) references Task (id) on delete restrict on update restrict;
create index ix_solvedSubTask_task_21 on solvedSubTask (task_id);
alter table story_challenge add constraint fk_story_challenge_next_22 foreign key (next_id) references story_challenge (id) on delete restrict on update restrict;
create index ix_story_challenge_next_22 on story_challenge (next_id);
alter table submitted_homework add constraint fk_submitted_homework_profile_23 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_submitted_homework_profile_23 on submitted_homework (profile_id);
alter table submitted_homework add constraint fk_submitted_homework_task_24 foreign key (task_id) references Task (id) on delete restrict on update restrict;
create index ix_submitted_homework_task_24 on submitted_homework (task_id);
alter table submitted_homework add constraint fk_submitted_homework_homeWork_25 foreign key (home_work_id) references homeWork_challenge (id) on delete restrict on update restrict;
create index ix_submitted_homework_homeWork_25 on submitted_homework (home_work_id);
alter table TableDefinition add constraint fk_TableDefinition_taskSet_26 foreign key (task_set_id) references TaskSet (id) on delete restrict on update restrict;
create index ix_TableDefinition_taskSet_26 on TableDefinition (task_set_id);
alter table Task add constraint fk_Task_taskSet_27 foreign key (task_set_id) references TaskSet (id) on delete restrict on update restrict;
create index ix_Task_taskSet_27 on Task (task_set_id);
alter table Task add constraint fk_Task_creator_28 foreign key (creator_id) references profile (id) on delete restrict on update restrict;
create index ix_Task_creator_28 on Task (creator_id);
alter table TaskSet add constraint fk_TaskSet_creator_29 foreign key (creator_id) references profile (id) on delete restrict on update restrict;
create index ix_TaskSet_creator_29 on TaskSet (creator_id);
alter table UserSession add constraint fk_UserSession_user_30 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_UserSession_user_30 on UserSession (user_id);



alter table homeWork_challenge_TaskSet add constraint fk_homeWork_challenge_TaskSet_homeWork_challenge_01 foreign key (homeWork_challenge_id) references homeWork_challenge (id) on delete restrict on update restrict;

alter table homeWork_challenge_TaskSet add constraint fk_homeWork_challenge_TaskSet_TaskSet_02 foreign key (TaskSet_id) references TaskSet (id) on delete restrict on update restrict;

alter table profile_ShopItem add constraint fk_profile_ShopItem_profile_01 foreign key (profile_id) references profile (id) on delete restrict on update restrict;

alter table profile_ShopItem add constraint fk_profile_ShopItem_ShopItem_02 foreign key (ShopItem_id) references ShopItem (id) on delete restrict on update restrict;

alter table story_challenge_texts add constraint fk_story_challenge_texts_story_challenge_01 foreign key (story_challenge_id) references story_challenge (id) on delete restrict on update restrict;

alter table story_challenge_texts add constraint fk_story_challenge_texts_texts_02 foreign key (texts_id) references texts (id) on delete restrict on update restrict;

alter table story_challenge_map add constraint fk_story_challenge_map_story_challenge_01 foreign key (story_challenge_id) references story_challenge (id) on delete restrict on update restrict;

alter table story_challenge_map add constraint fk_story_challenge_map_map_02 foreign key (map_id) references map (id) on delete restrict on update restrict;

alter table TaskSet_rating add constraint fk_TaskSet_rating_TaskSet_01 foreign key (TaskSet_id) references TaskSet (id) on delete restrict on update restrict;

alter table TaskSet_rating add constraint fk_TaskSet_rating_rating_02 foreign key (rating_id) references rating (id) on delete restrict on update restrict;

alter table TaskSet_Comment add constraint fk_TaskSet_Comment_TaskSet_01 foreign key (TaskSet_id) references TaskSet (id) on delete restrict on update restrict;

alter table TaskSet_Comment add constraint fk_TaskSet_Comment_Comment_02 foreign key (Comment_id) references Comment (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table avatar;

drop table challenge;

drop table ColumnDefinition;

drop table Comment;

drop table ForeignKeyRelation;

drop table homeWork_challenge;

drop table homeWork_challenge_TaskSet;

drop table Inventory;

drop table lofi_coin_flow_log;

drop table map;

drop table potion;

drop table profile;

drop table profile_ShopItem;

drop table rating;

drop table scroll;

drop table Scroll_Collection;

drop table ShopItem;

drop table solvedSubTask;

drop table story_challenge;

drop table story_challenge_texts;

drop table story_challenge_map;

drop table submitted_homework;

drop table TableDefinition;

drop table Task;

drop table TaskSet;

drop table TaskSet_rating;

drop table TaskSet_Comment;

drop table texts;

drop table user;

drop table UserSession;

SET FOREIGN_KEY_CHECKS=1;


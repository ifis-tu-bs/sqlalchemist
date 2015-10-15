# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table avatar (
  id                        bigint not null,
  avatar_name               varchar(255),
  Avatardesc                varchar(255),
  avatar_filename           varchar(255),
  is_team                   boolean,
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
  id                        bigint not null,
  challenge_name            varchar(255),
  solve_type                integer,
  solve_type_extension      integer,
  created_at                timestamp,
  type                      integer,
  modified_at               timestamp,
  constraint pk_challenge primary key (id))
;

create table ColumnDefinition (
  id                        bigint not null,
  table_definition_id       bigint,
  column_name               varchar(255),
  data_type                 varchar(255),
  is_primary_key            boolean,
  is_not_nullable           boolean,
  foreign_key_id            bigint,
  datagen_set               integer,
  constraint pk_ColumnDefinition primary key (id))
;

create table Comment (
  id                        bigint not null,
  task_set_id               bigint,
  task_id                   bigint,
  profile_id                bigint,
  comment                   varchar(255),
  created_at                timestamp,
  constraint pk_Comment primary key (id))
;

create table ForeignKeyRelation (
  id                        bigint not null,
  task_set_id               bigint,
  source_table              varchar(255),
  source_column             varchar(255),
  destination_table         varchar(255),
  destination_column        varchar(255),
  constraint pk_ForeignKeyRelation primary key (id))
;

create table homeWork_challenge (
  id                        bigint not null,
  challenge_name            varchar(255),
  solve_type                integer,
  solve_type_extension      integer,
  created_at                timestamp,
  type                      integer,
  modified_at               timestamp,
  creator_id                bigint,
  start_at                  timestamp,
  expires_at                timestamp,
  constraint pk_homeWork_challenge primary key (id))
;

create table Inventory (
  id                        bigint not null,
  profile_id                bigint,
  potion_id                 bigint,
  beltSlot                  integer,
  constraint pk_Inventory primary key (id))
;

create table lofi_coin_flow_log (
  id                        bigint not null,
  profile_id                bigint,
  lofi_coins_collected      integer,
  collected                 timestamp,
  constraint pk_lofi_coin_flow_log primary key (id))
;

create table map (
  id                        bigint not null,
  level                     integer,
  path                      varchar(255),
  boss_map                  boolean,
  constraint uq_map_path unique (path),
  constraint pk_map primary key (id))
;

create table potion (
  id                        bigint not null,
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
  id                        bigint not null,
  user_id                   bigint,
  username                  varchar(255),
  health                    integer,
  defense                   integer,
  speed                     integer,
  jump                      integer,
  slot                      integer,
  sound                     boolean,
  music                     boolean,
  tutorial_done             boolean,
  story_done                boolean,
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
  created_at                timestamp,
  edited_at                 timestamp,
  version                   timestamp not null,
  constraint uq_profile_username unique (username),
  constraint pk_profile primary key (id))
;

create table rating (
  id                        bigint not null,
  profile_id                bigint,
  task_set_id               bigint,
  task_id                   bigint,
  positive_ratings          bigint,
  negative_ratings          bigint,
  edit_ratings              bigint,
  constraint pk_rating primary key (id))
;

create table scroll (
  id                        bigint not null,
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
  id                        bigint not null,
  profile_id                bigint,
  scroll_id                 bigint,
  is_active                 boolean,
  added                     timestamp,
  constraint pk_Scroll_Collection primary key (id))
;

create table ShopItem (
  id                        bigint not null,
  type                      integer,
  ShopItemName              varchar(255),
  ShopItem_desc             varchar(255),
  thumbnail_url             varchar(255),
  price                     integer,
  avatar_id                 bigint,
  constraint pk_ShopItem primary key (id))
;

create table solvedSubTask (
  id                        bigint not null,
  profile_id                bigint,
  task_id                   bigint,
  solved                    integer,
  trys                      integer,
  last_solved               timestamp,
  constraint pk_solvedSubTask primary key (id))
;

create table story_challenge (
  id                        bigint not null,
  challenge_name            varchar(255),
  solve_type                integer,
  solve_type_extension      integer,
  created_at                timestamp,
  type                      integer,
  modified_at               timestamp,
  level                     integer,
  next_id                   bigint,
  constraint pk_story_challenge primary key (id))
;

create table submitted_homework (
  id                        bigint not null,
  profile_id                bigint,
  task_id                   bigint,
  solved                    integer,
  trys                      integer,
  last_solved               timestamp,
  home_work_id              bigint,
  statement                 varchar(255),
  solve                     boolean,
  constraint uq_submitted_homework_1 unique (profile_id,task_id, home_work_id),
  constraint pk_submitted_homework primary key (id))
;

create table TableDefinition (
  id                        bigint not null,
  task_set_id               bigint,
  table_name                varchar(255),
  extension                 Text,
  constraint pk_TableDefinition primary key (id))
;

create table Task (
  id                        bigint not null,
  task_set_id               bigint,
  task_name                 varchar(255),
  task_text                 varchar(255),
  ref_statement             varchar(255),
  evaluationstrategy        integer,
  points                    integer,
  required_term             integer,
  creator_id                bigint,
  created_at                timestamp,
  updated_at                timestamp,
  constraint pk_Task primary key (id))
;

create table TaskSet (
  id                        bigint not null,
  task_set_name             varchar(255),
  relations_formatted       varchar(255),
  is_homework               boolean,
  creator_id                bigint,
  created_at                timestamp,
  updated_at                timestamp,
  constraint pk_TaskSet primary key (id))
;

create table texts (
  id                        bigint not null,
  type                      integer,
  prerequisite              integer,
  chronology                integer,
  text                      Text,
  sound_url                 varchar(255),
  count_of_lines            integer,
  constraint pk_texts primary key (id))
;

create table user (
  id                        bigint not null,
  email                     varchar(255),
  email_verified            boolean,
  email_verify_code         varchar(255),
  user_mnr                  varchar(255),
  user_ynr                  varchar(255),
  is_student                boolean,
  password                  varchar(255),
  user_role                 integer,
  created_at                timestamp,
  edited_at                 timestamp,
  constraint uq_user_email unique (email),
  constraint uq_user_email_verify_code unique (email_verify_code),
  constraint uq_user_user_mnr unique (user_mnr),
  constraint uq_user_user_ynr unique (user_ynr),
  constraint pk_user primary key (id))
;

create table UserSession (
  id                        bigint not null,
  sessionID                 varchar(30),
  user_id                   bigint,
  remoteAddress             varchar(255),
  created_at                timestamp,
  expires_at                timestamp,
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
create sequence avatar_seq;

create sequence challenge_seq;

create sequence ColumnDefinition_seq;

create sequence Comment_seq;

create sequence ForeignKeyRelation_seq;

create sequence homeWork_challenge_seq;

create sequence Inventory_seq;

create sequence lofi_coin_flow_log_seq;

create sequence map_seq;

create sequence potion_seq;

create sequence profile_seq;

create sequence rating_seq;

create sequence scroll_seq;

create sequence Scroll_Collection_seq;

create sequence ShopItem_seq;

create sequence solvedSubTask_seq;

create sequence story_challenge_seq;

create sequence submitted_homework_seq;

create sequence TableDefinition_seq;

create sequence Task_seq;

create sequence TaskSet_seq;

create sequence texts_seq;

create sequence user_seq;

create sequence UserSession_seq;

alter table ColumnDefinition add constraint fk_ColumnDefinition_tableDefin_1 foreign key (table_definition_id) references TableDefinition (id) on delete restrict on update restrict;
create index ix_ColumnDefinition_tableDefin_1 on ColumnDefinition (table_definition_id);
alter table ColumnDefinition add constraint fk_ColumnDefinition_foreignKey_2 foreign key (foreign_key_id) references ColumnDefinition (id) on delete restrict on update restrict;
create index ix_ColumnDefinition_foreignKey_2 on ColumnDefinition (foreign_key_id);
alter table Comment add constraint fk_Comment_taskSet_3 foreign key (task_set_id) references TaskSet (id) on delete restrict on update restrict;
create index ix_Comment_taskSet_3 on Comment (task_set_id);
alter table Comment add constraint fk_Comment_task_4 foreign key (task_id) references Task (id) on delete restrict on update restrict;
create index ix_Comment_task_4 on Comment (task_id);
alter table Comment add constraint fk_Comment_profile_5 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_Comment_profile_5 on Comment (profile_id);
alter table ForeignKeyRelation add constraint fk_ForeignKeyRelation_taskSet_6 foreign key (task_set_id) references TaskSet (id) on delete restrict on update restrict;
create index ix_ForeignKeyRelation_taskSet_6 on ForeignKeyRelation (task_set_id);
alter table homeWork_challenge add constraint fk_homeWork_challenge_creator_7 foreign key (creator_id) references profile (id) on delete restrict on update restrict;
create index ix_homeWork_challenge_creator_7 on homeWork_challenge (creator_id);
alter table Inventory add constraint fk_Inventory_profile_8 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_Inventory_profile_8 on Inventory (profile_id);
alter table Inventory add constraint fk_Inventory_potion_9 foreign key (potion_id) references potion (id) on delete restrict on update restrict;
create index ix_Inventory_potion_9 on Inventory (potion_id);
alter table lofi_coin_flow_log add constraint fk_lofi_coin_flow_log_profile_10 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_lofi_coin_flow_log_profile_10 on lofi_coin_flow_log (profile_id);
alter table profile add constraint fk_profile_user_11 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_profile_user_11 on profile (user_id);
alter table profile add constraint fk_profile_avatar_12 foreign key (avatar_id) references avatar (id) on delete restrict on update restrict;
create index ix_profile_avatar_12 on profile (avatar_id);
alter table profile add constraint fk_profile_currentStory_13 foreign key (current_story_id) references story_challenge (id) on delete restrict on update restrict;
create index ix_profile_currentStory_13 on profile (current_story_id);
alter table profile add constraint fk_profile_currentScroll_14 foreign key (current_scroll_id) references scroll (id) on delete restrict on update restrict;
create index ix_profile_currentScroll_14 on profile (current_scroll_id);
alter table rating add constraint fk_rating_profile_15 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_rating_profile_15 on rating (profile_id);
alter table rating add constraint fk_rating_taskSet_16 foreign key (task_set_id) references TaskSet (id) on delete restrict on update restrict;
create index ix_rating_taskSet_16 on rating (task_set_id);
alter table rating add constraint fk_rating_task_17 foreign key (task_id) references Task (id) on delete restrict on update restrict;
create index ix_rating_task_17 on rating (task_id);
alter table scroll add constraint fk_scroll_potion_18 foreign key (potion_id) references potion (id) on delete restrict on update restrict;
create index ix_scroll_potion_18 on scroll (potion_id);
alter table Scroll_Collection add constraint fk_Scroll_Collection_profile_19 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_Scroll_Collection_profile_19 on Scroll_Collection (profile_id);
alter table Scroll_Collection add constraint fk_Scroll_Collection_scroll_20 foreign key (scroll_id) references scroll (id) on delete restrict on update restrict;
create index ix_Scroll_Collection_scroll_20 on Scroll_Collection (scroll_id);
alter table ShopItem add constraint fk_ShopItem_avatar_21 foreign key (avatar_id) references avatar (id) on delete restrict on update restrict;
create index ix_ShopItem_avatar_21 on ShopItem (avatar_id);
alter table solvedSubTask add constraint fk_solvedSubTask_profile_22 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_solvedSubTask_profile_22 on solvedSubTask (profile_id);
alter table solvedSubTask add constraint fk_solvedSubTask_task_23 foreign key (task_id) references Task (id) on delete restrict on update restrict;
create index ix_solvedSubTask_task_23 on solvedSubTask (task_id);
alter table story_challenge add constraint fk_story_challenge_next_24 foreign key (next_id) references story_challenge (id) on delete restrict on update restrict;
create index ix_story_challenge_next_24 on story_challenge (next_id);
alter table submitted_homework add constraint fk_submitted_homework_profile_25 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_submitted_homework_profile_25 on submitted_homework (profile_id);
alter table submitted_homework add constraint fk_submitted_homework_task_26 foreign key (task_id) references Task (id) on delete restrict on update restrict;
create index ix_submitted_homework_task_26 on submitted_homework (task_id);
alter table submitted_homework add constraint fk_submitted_homework_homeWor_27 foreign key (home_work_id) references homeWork_challenge (id) on delete restrict on update restrict;
create index ix_submitted_homework_homeWor_27 on submitted_homework (home_work_id);
alter table TableDefinition add constraint fk_TableDefinition_taskSet_28 foreign key (task_set_id) references TaskSet (id) on delete restrict on update restrict;
create index ix_TableDefinition_taskSet_28 on TableDefinition (task_set_id);
alter table Task add constraint fk_Task_taskSet_29 foreign key (task_set_id) references TaskSet (id) on delete restrict on update restrict;
create index ix_Task_taskSet_29 on Task (task_set_id);
alter table Task add constraint fk_Task_creator_30 foreign key (creator_id) references profile (id) on delete restrict on update restrict;
create index ix_Task_creator_30 on Task (creator_id);
alter table TaskSet add constraint fk_TaskSet_creator_31 foreign key (creator_id) references profile (id) on delete restrict on update restrict;
create index ix_TaskSet_creator_31 on TaskSet (creator_id);
alter table UserSession add constraint fk_UserSession_user_32 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_UserSession_user_32 on UserSession (user_id);



alter table homeWork_challenge_TaskSet add constraint fk_homeWork_challenge_TaskSet_01 foreign key (homeWork_challenge_id) references homeWork_challenge (id) on delete restrict on update restrict;

alter table homeWork_challenge_TaskSet add constraint fk_homeWork_challenge_TaskSet_02 foreign key (TaskSet_id) references TaskSet (id) on delete restrict on update restrict;

alter table profile_ShopItem add constraint fk_profile_ShopItem_profile_01 foreign key (profile_id) references profile (id) on delete restrict on update restrict;

alter table profile_ShopItem add constraint fk_profile_ShopItem_ShopItem_02 foreign key (ShopItem_id) references ShopItem (id) on delete restrict on update restrict;

alter table story_challenge_texts add constraint fk_story_challenge_texts_stor_01 foreign key (story_challenge_id) references story_challenge (id) on delete restrict on update restrict;

alter table story_challenge_texts add constraint fk_story_challenge_texts_text_02 foreign key (texts_id) references texts (id) on delete restrict on update restrict;

alter table story_challenge_map add constraint fk_story_challenge_map_story__01 foreign key (story_challenge_id) references story_challenge (id) on delete restrict on update restrict;

alter table story_challenge_map add constraint fk_story_challenge_map_map_02 foreign key (map_id) references map (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists avatar;

drop table if exists challenge;

drop table if exists ColumnDefinition;

drop table if exists Comment;

drop table if exists ForeignKeyRelation;

drop table if exists homeWork_challenge;

drop table if exists homeWork_challenge_TaskSet;

drop table if exists Inventory;

drop table if exists lofi_coin_flow_log;

drop table if exists map;

drop table if exists potion;

drop table if exists profile;

drop table if exists profile_ShopItem;

drop table if exists rating;

drop table if exists scroll;

drop table if exists Scroll_Collection;

drop table if exists ShopItem;

drop table if exists solvedSubTask;

drop table if exists story_challenge;

drop table if exists story_challenge_texts;

drop table if exists story_challenge_map;

drop table if exists submitted_homework;

drop table if exists TableDefinition;

drop table if exists Task;

drop table if exists TaskSet;

drop table if exists texts;

drop table if exists user;

drop table if exists UserSession;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists avatar_seq;

drop sequence if exists challenge_seq;

drop sequence if exists ColumnDefinition_seq;

drop sequence if exists Comment_seq;

drop sequence if exists ForeignKeyRelation_seq;

drop sequence if exists homeWork_challenge_seq;

drop sequence if exists Inventory_seq;

drop sequence if exists lofi_coin_flow_log_seq;

drop sequence if exists map_seq;

drop sequence if exists potion_seq;

drop sequence if exists profile_seq;

drop sequence if exists rating_seq;

drop sequence if exists scroll_seq;

drop sequence if exists Scroll_Collection_seq;

drop sequence if exists ShopItem_seq;

drop sequence if exists solvedSubTask_seq;

drop sequence if exists story_challenge_seq;

drop sequence if exists submitted_homework_seq;

drop sequence if exists TableDefinition_seq;

drop sequence if exists Task_seq;

drop sequence if exists TaskSet_seq;

drop sequence if exists texts_seq;

drop sequence if exists user_seq;

drop sequence if exists UserSession_seq;


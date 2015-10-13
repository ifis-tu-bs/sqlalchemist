# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table avatar (
  id                        bigint not null,
  avatar_name               varchar(255),
  Avatardesc                varchar(255),
  avatar_filename           varchar(255),
  is_team                   boolean,
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
  id                        bigint not null,
  challenge_name            varchar(255),
  solve_type                integer,
  solve_type_extension      integer,
  created_at                timestamp,
  type                      integer,
  modified_at               timestamp,
  constraint pk_challenge primary key (id))
;

create table Comment (
  id                        bigint not null,
  profile_id                bigint,
  text                      varchar(255),
  created_at                timestamp,
  constraint pk_Comment primary key (id))
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
  sub_task_id               bigint,
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

create table subTask (
  id                        bigint not null,
  creator_id                bigint,
  task_file_id              bigint,
  position                  integer,
  subTask_Points            integer,
  exercise                  varchar(255),
  ref_statement             text,
  available                 boolean,
  is_home_work              boolean,
  created_at                timestamp,
  edited_at                 timestamp,
  constraint uq_subTask_1 unique (task_file_id,position),
  constraint pk_subTask primary key (id))
;

create table submitted_homework (
  id                        bigint not null,
  profile_id                bigint,
  sub_task_id               bigint,
  solved                    integer,
  trys                      integer,
  last_solved               timestamp,
  home_work_id              bigint,
  statement                 varchar(255),
  solve                     boolean,
  constraint uq_submitted_homework_1 unique (profile_id,sub_task_id, home_work_id),
  constraint pk_submitted_homework primary key (id))
;

create table task_file (
  id                        bigint not null,
  creator_id                bigint,
  is_home_work              boolean,
  is_available              boolean,
  file_name                 varchar(255),
  relation_schema           Text,
  xmlContent                Text,
  created_at                timestamp,
  constraint uq_task_file_file_name unique (file_name),
  constraint pk_task_file primary key (id))
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
  password_hash             varchar(255),
  password_reset_code       varchar(255),
  user_role                 integer,
  created_at                timestamp,
  edited_at                 timestamp,
  constraint uq_user_email unique (email),
  constraint uq_user_email_verify_code unique (email_verify_code),
  constraint uq_user_user_mnr unique (user_mnr),
  constraint uq_user_user_ynr unique (user_ynr),
  constraint uq_user_password_reset_code unique (password_reset_code),
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


create table homeWork_challenge_task_file (
  homeWork_challenge_id          bigint not null,
  task_file_id                   bigint not null,
  constraint pk_homeWork_challenge_task_file primary key (homeWork_challenge_id, task_file_id))
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

create table subTask_Comment (
  subTask_id                     bigint not null,
  Comment_id                     bigint not null,
  constraint pk_subTask_Comment primary key (subTask_id, Comment_id))
;

create table subTask_rating (
  subTask_id                     bigint not null,
  rating_id                      bigint not null,
  constraint pk_subTask_rating primary key (subTask_id, rating_id))
;

create table task_file_rating (
  task_file_id                   bigint not null,
  rating_id                      bigint not null,
  constraint pk_task_file_rating primary key (task_file_id, rating_id))
;

create table task_file_Comment (
  task_file_id                   bigint not null,
  Comment_id                     bigint not null,
  constraint pk_task_file_Comment primary key (task_file_id, Comment_id))
;
create sequence avatar_seq;

create sequence challenge_seq;

create sequence Comment_seq;

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

create sequence subTask_seq;

create sequence submitted_homework_seq;

create sequence task_file_seq;

create sequence texts_seq;

create sequence user_seq;

create sequence UserSession_seq;

alter table Comment add constraint fk_Comment_profile_1 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_Comment_profile_1 on Comment (profile_id);
alter table homeWork_challenge add constraint fk_homeWork_challenge_creator_2 foreign key (creator_id) references profile (id) on delete restrict on update restrict;
create index ix_homeWork_challenge_creator_2 on homeWork_challenge (creator_id);
alter table Inventory add constraint fk_Inventory_profile_3 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_Inventory_profile_3 on Inventory (profile_id);
alter table Inventory add constraint fk_Inventory_potion_4 foreign key (potion_id) references potion (id) on delete restrict on update restrict;
create index ix_Inventory_potion_4 on Inventory (potion_id);
alter table lofi_coin_flow_log add constraint fk_lofi_coin_flow_log_profile_5 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_lofi_coin_flow_log_profile_5 on lofi_coin_flow_log (profile_id);
alter table profile add constraint fk_profile_user_6 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_profile_user_6 on profile (user_id);
alter table profile add constraint fk_profile_avatar_7 foreign key (avatar_id) references avatar (id) on delete restrict on update restrict;
create index ix_profile_avatar_7 on profile (avatar_id);
alter table profile add constraint fk_profile_currentStory_8 foreign key (current_story_id) references story_challenge (id) on delete restrict on update restrict;
create index ix_profile_currentStory_8 on profile (current_story_id);
alter table profile add constraint fk_profile_currentScroll_9 foreign key (current_scroll_id) references scroll (id) on delete restrict on update restrict;
create index ix_profile_currentScroll_9 on profile (current_scroll_id);
alter table rating add constraint fk_rating_profile_10 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_rating_profile_10 on rating (profile_id);
alter table scroll add constraint fk_scroll_potion_11 foreign key (potion_id) references potion (id) on delete restrict on update restrict;
create index ix_scroll_potion_11 on scroll (potion_id);
alter table Scroll_Collection add constraint fk_Scroll_Collection_profile_12 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_Scroll_Collection_profile_12 on Scroll_Collection (profile_id);
alter table Scroll_Collection add constraint fk_Scroll_Collection_scroll_13 foreign key (scroll_id) references scroll (id) on delete restrict on update restrict;
create index ix_Scroll_Collection_scroll_13 on Scroll_Collection (scroll_id);
alter table ShopItem add constraint fk_ShopItem_avatar_14 foreign key (avatar_id) references avatar (id) on delete restrict on update restrict;
create index ix_ShopItem_avatar_14 on ShopItem (avatar_id);
alter table solvedSubTask add constraint fk_solvedSubTask_profile_15 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_solvedSubTask_profile_15 on solvedSubTask (profile_id);
alter table solvedSubTask add constraint fk_solvedSubTask_subTask_16 foreign key (sub_task_id) references subTask (id) on delete restrict on update restrict;
create index ix_solvedSubTask_subTask_16 on solvedSubTask (sub_task_id);
alter table story_challenge add constraint fk_story_challenge_next_17 foreign key (next_id) references story_challenge (id) on delete restrict on update restrict;
create index ix_story_challenge_next_17 on story_challenge (next_id);
alter table subTask add constraint fk_subTask_creator_18 foreign key (creator_id) references profile (id) on delete restrict on update restrict;
create index ix_subTask_creator_18 on subTask (creator_id);
alter table subTask add constraint fk_subTask_taskFile_19 foreign key (task_file_id) references task_file (id) on delete restrict on update restrict;
create index ix_subTask_taskFile_19 on subTask (task_file_id);
alter table submitted_homework add constraint fk_submitted_homework_profile_20 foreign key (profile_id) references profile (id) on delete restrict on update restrict;
create index ix_submitted_homework_profile_20 on submitted_homework (profile_id);
alter table submitted_homework add constraint fk_submitted_homework_subTask_21 foreign key (sub_task_id) references subTask (id) on delete restrict on update restrict;
create index ix_submitted_homework_subTask_21 on submitted_homework (sub_task_id);
alter table submitted_homework add constraint fk_submitted_homework_homeWor_22 foreign key (home_work_id) references homeWork_challenge (id) on delete restrict on update restrict;
create index ix_submitted_homework_homeWor_22 on submitted_homework (home_work_id);
alter table task_file add constraint fk_task_file_creator_23 foreign key (creator_id) references profile (id) on delete restrict on update restrict;
create index ix_task_file_creator_23 on task_file (creator_id);
alter table UserSession add constraint fk_UserSession_user_24 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_UserSession_user_24 on UserSession (user_id);



alter table homeWork_challenge_task_file add constraint fk_homeWork_challenge_task_fi_01 foreign key (homeWork_challenge_id) references homeWork_challenge (id) on delete restrict on update restrict;

alter table homeWork_challenge_task_file add constraint fk_homeWork_challenge_task_fi_02 foreign key (task_file_id) references task_file (id) on delete restrict on update restrict;

alter table profile_ShopItem add constraint fk_profile_ShopItem_profile_01 foreign key (profile_id) references profile (id) on delete restrict on update restrict;

alter table profile_ShopItem add constraint fk_profile_ShopItem_ShopItem_02 foreign key (ShopItem_id) references ShopItem (id) on delete restrict on update restrict;

alter table story_challenge_texts add constraint fk_story_challenge_texts_stor_01 foreign key (story_challenge_id) references story_challenge (id) on delete restrict on update restrict;

alter table story_challenge_texts add constraint fk_story_challenge_texts_text_02 foreign key (texts_id) references texts (id) on delete restrict on update restrict;

alter table story_challenge_map add constraint fk_story_challenge_map_story__01 foreign key (story_challenge_id) references story_challenge (id) on delete restrict on update restrict;

alter table story_challenge_map add constraint fk_story_challenge_map_map_02 foreign key (map_id) references map (id) on delete restrict on update restrict;

alter table subTask_Comment add constraint fk_subTask_Comment_subTask_01 foreign key (subTask_id) references subTask (id) on delete restrict on update restrict;

alter table subTask_Comment add constraint fk_subTask_Comment_Comment_02 foreign key (Comment_id) references Comment (id) on delete restrict on update restrict;

alter table subTask_rating add constraint fk_subTask_rating_subTask_01 foreign key (subTask_id) references subTask (id) on delete restrict on update restrict;

alter table subTask_rating add constraint fk_subTask_rating_rating_02 foreign key (rating_id) references rating (id) on delete restrict on update restrict;

alter table task_file_rating add constraint fk_task_file_rating_task_file_01 foreign key (task_file_id) references task_file (id) on delete restrict on update restrict;

alter table task_file_rating add constraint fk_task_file_rating_rating_02 foreign key (rating_id) references rating (id) on delete restrict on update restrict;

alter table task_file_Comment add constraint fk_task_file_Comment_task_fil_01 foreign key (task_file_id) references task_file (id) on delete restrict on update restrict;

alter table task_file_Comment add constraint fk_task_file_Comment_Comment_02 foreign key (Comment_id) references Comment (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists avatar;

drop table if exists challenge;

drop table if exists Comment;

drop table if exists homeWork_challenge;

drop table if exists homeWork_challenge_task_file;

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

drop table if exists subTask;

drop table if exists subTask_Comment;

drop table if exists subTask_rating;

drop table if exists submitted_homework;

drop table if exists task_file;

drop table if exists task_file_rating;

drop table if exists task_file_Comment;

drop table if exists texts;

drop table if exists user;

drop table if exists UserSession;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists avatar_seq;

drop sequence if exists challenge_seq;

drop sequence if exists Comment_seq;

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

drop sequence if exists subTask_seq;

drop sequence if exists submitted_homework_seq;

drop sequence if exists task_file_seq;

drop sequence if exists texts_seq;

drop sequence if exists user_seq;

drop sequence if exists UserSession_seq;


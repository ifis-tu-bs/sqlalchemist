# --- !Ups

create table avatar (
  id                            bigint auto_increment not null,
  avatar_name                   varchar(255),
  description                   varchar(255),
  avatar_filename               varchar(255),
  is_team                       tinyint(1) default 0,
  health                        integer,
  defense                       integer,
  speed                         integer,
  jump                          integer,
  slots                         integer,
  constraint uq_avatar_avatar_name unique (avatar_name),
  constraint uq_avatar_avatar_filename unique (avatar_filename),
  constraint pk_avatar primary key (id)
);

create table columndefinition (
  id                            bigint auto_increment not null,
  table_definition_id           bigint,
  column_name                   varchar(255),
  data_type                     varchar(255),
  is_primary_key                tinyint(1) default 0,
  is_not_nullable               tinyint(1) default 0,
  foreign_key_id                bigint,
  datagen_set                   integer,
  constraint pk_columndefinition primary key (id)
);

create table comment (
  id                            bigint auto_increment not null,
  task_set_id                   bigint,
  task_id                       bigint,
  creator_id                    bigint,
  comment                       varchar(255),
  created_at                    datetime(6),
  constraint pk_comment primary key (id)
);

create table foreignkeyrelation (
  id                            bigint auto_increment not null,
  task_set_id                   bigint,
  source_table                  varchar(255),
  source_column                 varchar(255),
  destination_table             varchar(255),
  destination_column            varchar(255),
  constraint pk_foreignkeyrelation primary key (id)
);

create table homework (
  id                            bigint auto_increment not null,
  creator_id                    bigint,
  start_at                      datetime(6),
  expire_at                     datetime(6),
  home_work_name                varchar(255),
  constraint pk_homework primary key (id)
);

create table homework_taskset (
  homework_id                   bigint not null,
  taskset_id                    bigint not null,
  constraint pk_homework_taskset primary key (homework_id,taskset_id)
);

create table inventory (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  potion_id                     bigint,
  belt_slot                     integer,
  constraint pk_inventory primary key (id)
);

create table loficoinflowlog (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  lofi_coins_collected          integer,
  collected                     datetime(6),
  constraint pk_loficoinflowlog primary key (id)
);

create table map (
  id                            bigint auto_increment not null,
  level                         integer,
  path                          varchar(255),
  is_boss_map                   tinyint(1) default 0,
  constraint uq_map_path unique (path),
  constraint pk_map primary key (id)
);

create table potion (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  type                          integer,
  power_level                   integer,
  buff_value                    integer,
  health                        integer,
  defense                       integer,
  speed                         integer,
  jump                          integer,
  slots                         integer,
  constraint uq_potion_name unique (name),
  constraint uq_potion_type_power_level unique (type,power_level),
  constraint pk_potion primary key (id)
);

create table rating (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  task_set_id                   bigint,
  task_id                       bigint,
  positive_ratings              bigint,
  negative_ratings              bigint,
  edit_ratings                  bigint,
  constraint pk_rating primary key (id)
);

create table role (
  id                            bigint auto_increment not null,
  priority                      integer,
  role_name                     varchar(255) not null,
  own_task_set_create           tinyint(1) default 0,
  own_task_set_read             tinyint(1) default 0,
  own_task_set_update           tinyint(1) default 0,
  own_task_set_delete           tinyint(1) default 0,
  foreign_task_set_create       tinyint(1) default 0,
  foreign_task_set_read         tinyint(1) default 0,
  foreign_task_set_update       tinyint(1) default 0,
  foreign_task_set_delete       tinyint(1) default 0,
  own_task_create               tinyint(1) default 0,
  own_task_read                 tinyint(1) default 0,
  own_task_update               tinyint(1) default 0,
  own_task_delete               tinyint(1) default 0,
  foreign_task_create           tinyint(1) default 0,
  foreign_task_read             tinyint(1) default 0,
  foreign_task_update           tinyint(1) default 0,
  foreign_task_delete           tinyint(1) default 0,
  homework_create               tinyint(1) default 0,
  homework_read                 tinyint(1) default 0,
  homework_update               tinyint(1) default 0,
  homework_delete               tinyint(1) default 0,
  role_create                   tinyint(1) default 0,
  role_read                     tinyint(1) default 0,
  role_update                   tinyint(1) default 0,
  role_delete                   tinyint(1) default 0,
  group_create                  tinyint(1) default 0,
  group_read                    tinyint(1) default 0,
  group_update                  tinyint(1) default 0,
  group_delete                  tinyint(1) default 0,
  user_create                   tinyint(1) default 0,
  user_read                     tinyint(1) default 0,
  user_update                   tinyint(1) default 0,
  user_delete                   tinyint(1) default 0,
  votes                         integer,
  is_deletable                  tinyint(1) default 0,
  creator_id                    bigint,
  created_at                    datetime(6),
  constraint uq_role_priority unique (priority),
  constraint uq_role_role_name unique (role_name),
  constraint pk_role primary key (id)
);

create table scroll (
  id                            bigint auto_increment not null,
  pos_id                        integer,
  name                          varchar(255),
  type                          integer,
  potion_id                     bigint,
  health                        integer,
  defense                       integer,
  speed                         integer,
  jump                          integer,
  slots                         integer,
  constraint uq_scroll_pos_id unique (pos_id),
  constraint uq_scroll_name unique (name),
  constraint uq_scroll_potion_id unique (potion_id),
  constraint pk_scroll primary key (id)
);

create table scrollcollection (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  scroll_id                     bigint,
  is_active                     tinyint(1) default 0,
  added                         datetime(6),
  constraint uq_scrollcollection_user_id_scroll_id unique (user_id,scroll_id),
  constraint pk_scrollcollection primary key (id)
);

create table session (
  id                            varchar(255) not null,
  owner_id                      bigint,
  active                        tinyint(1) default 0,
  created_at                    datetime(6),
  constraint pk_session primary key (id)
);

create table shopitem (
  id                            bigint auto_increment not null,
  type                          integer,
  title                         varchar(255),
  description                   varchar(255),
  thumbnail_url                 varchar(255),
  price                         integer,
  avatar_id                     bigint,
  constraint uq_shopitem_avatar_id unique (avatar_id),
  constraint pk_shopitem primary key (id)
);

create table solvedtask (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  task_id                       bigint,
  solved                        tinyint(1) default 0,
  time_needed                   integer,
  timestamp                     datetime(6),
  constraint pk_solvedtask primary key (id)
);

create table storychallenge (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  is_tutorial                   tinyint(1) default 0,
  level                         integer,
  next_id                       bigint,
  created_at                    datetime(6),
  constraint uq_storychallenge_next_id unique (next_id),
  constraint pk_storychallenge primary key (id)
);

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
  id                            bigint auto_increment not null,
  user_id                       bigint,
  task_id                       bigint,
  home_work_id                  bigint,
  syntax_checks_done            integer,
  semantic_checks_done          integer,
  statement                     Text,
  solve                         tinyint(1) default 0,
  constraint uq_submittedhomework_user_id_task_id_home_work_id unique (user_id,task_id,home_work_id),
  constraint pk_submittedhomework primary key (id)
);

create table tabledefinition (
  id                            bigint auto_increment not null,
  task_set_id                   bigint,
  table_name                    varchar(255),
  extension                     MEDIUMTEXT,
  constraint pk_tabledefinition primary key (id)
);

create table task (
  id                            bigint auto_increment not null,
  task_set_id                   bigint,
  task_name                     varchar(255),
  task_text                     Text,
  ref_statement                 Text,
  evaluation_strategy           integer,
  points                        integer,
  required_term                 integer,
  creator_id                    bigint,
  available                     tinyint(1) default 0,
  created_at                    datetime(6),
  updated_at                    datetime(6),
  available_syntax_checks       integer,
  available_semantic_checks     integer,
  constraint pk_task primary key (id)
);

create table taskset (
  id                            bigint auto_increment not null,
  task_set_name                 varchar(255),
  is_homework                   tinyint(1) default 0,
  creator_id                    bigint,
  available                     tinyint(1) default 0,
  created_at                    datetime(6),
  updated_at                    datetime(6),
  constraint pk_taskset primary key (id)
);

create table text (
  id                            bigint auto_increment not null,
  type                          integer,
  prerequisite                  integer,
  chronology                    integer,
  text                          Text,
  sound_url                     varchar(255),
  content_rows                  integer,
  constraint pk_text primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  email                         varchar(255),
  username                      varchar(255),
  password                      varchar(255),
  role_id                       bigint not null,
  email_verify_code             varchar(255),
  mat_nr                        varchar(255),
  y_id                          varchar(255),
  sound                         tinyint(1) default 0,
  music                         tinyint(1) default 0,
  health                        integer,
  defense                       integer,
  speed                         integer,
  jump                          integer,
  slots                         integer,
  tutorial_done                 tinyint(1) default 0,
  story_done                    tinyint(1) default 0,
  avatar_id                     bigint,
  current_story_id              bigint,
  current_scroll_id             bigint,
  current_task_set_id           bigint,
  current_home_work_id          bigint,
  depth                         integer,
  coins                         integer,
  coin_scale                    float,
  total_score                   integer,
  played_time                   integer,
  played_runs                   integer,
  total_coins                   integer,
  done_sql                      integer,
  solved_sql                    integer,
  success_rate                  integer,
  is_active                     tinyint(1) default 0,
  group_id                      bigint,
  created_at                    datetime(6),
  constraint uq_user_email unique (email),
  constraint uq_user_username unique (username),
  constraint uq_user_email_verify_code unique (email_verify_code),
  constraint uq_user_mat_nr unique (mat_nr),
  constraint uq_user_y_id unique (y_id),
  constraint pk_user primary key (id)
);

create table user_shopitem (
  user_id                       bigint not null,
  shopitem_id                   bigint not null,
  constraint pk_user_shopitem primary key (user_id,shopitem_id)
);

create table user_group (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  tutor_id                      bigint,
  constraint pk_user_group primary key (id)
);

alter table columndefinition add constraint fk_columndefinition_table_definition_id foreign key (table_definition_id) references tabledefinition (id) on delete restrict on update restrict;
create index ix_columndefinition_table_definition_id on columndefinition (table_definition_id);

alter table columndefinition add constraint fk_columndefinition_foreign_key_id foreign key (foreign_key_id) references columndefinition (id) on delete restrict on update restrict;
create index ix_columndefinition_foreign_key_id on columndefinition (foreign_key_id);

alter table comment add constraint fk_comment_task_set_id foreign key (task_set_id) references taskset (id) on delete restrict on update restrict;
create index ix_comment_task_set_id on comment (task_set_id);

alter table comment add constraint fk_comment_task_id foreign key (task_id) references task (id) on delete restrict on update restrict;
create index ix_comment_task_id on comment (task_id);

alter table comment add constraint fk_comment_creator_id foreign key (creator_id) references user (id) on delete restrict on update restrict;
create index ix_comment_creator_id on comment (creator_id);

alter table foreignkeyrelation add constraint fk_foreignkeyrelation_task_set_id foreign key (task_set_id) references taskset (id) on delete restrict on update restrict;
create index ix_foreignkeyrelation_task_set_id on foreignkeyrelation (task_set_id);

alter table homework add constraint fk_homework_creator_id foreign key (creator_id) references user (id) on delete restrict on update restrict;
create index ix_homework_creator_id on homework (creator_id);

alter table homework_taskset add constraint fk_homework_taskset_homework foreign key (homework_id) references homework (id) on delete restrict on update restrict;
create index ix_homework_taskset_homework on homework_taskset (homework_id);

alter table homework_taskset add constraint fk_homework_taskset_taskset foreign key (taskset_id) references taskset (id) on delete restrict on update restrict;
create index ix_homework_taskset_taskset on homework_taskset (taskset_id);

alter table inventory add constraint fk_inventory_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_inventory_user_id on inventory (user_id);

alter table inventory add constraint fk_inventory_potion_id foreign key (potion_id) references potion (id) on delete restrict on update restrict;
create index ix_inventory_potion_id on inventory (potion_id);

alter table loficoinflowlog add constraint fk_loficoinflowlog_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_loficoinflowlog_user_id on loficoinflowlog (user_id);

alter table rating add constraint fk_rating_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_rating_user_id on rating (user_id);

alter table rating add constraint fk_rating_task_set_id foreign key (task_set_id) references taskset (id) on delete restrict on update restrict;
create index ix_rating_task_set_id on rating (task_set_id);

alter table rating add constraint fk_rating_task_id foreign key (task_id) references task (id) on delete restrict on update restrict;
create index ix_rating_task_id on rating (task_id);

alter table role add constraint fk_role_creator_id foreign key (creator_id) references user (id) on delete restrict on update restrict;
create index ix_role_creator_id on role (creator_id);

alter table scroll add constraint fk_scroll_potion_id foreign key (potion_id) references potion (id) on delete restrict on update restrict;

alter table scrollcollection add constraint fk_scrollcollection_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_scrollcollection_user_id on scrollcollection (user_id);

alter table scrollcollection add constraint fk_scrollcollection_scroll_id foreign key (scroll_id) references scroll (id) on delete restrict on update restrict;
create index ix_scrollcollection_scroll_id on scrollcollection (scroll_id);

alter table session add constraint fk_session_owner_id foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_session_owner_id on session (owner_id);

alter table shopitem add constraint fk_shopitem_avatar_id foreign key (avatar_id) references avatar (id) on delete restrict on update restrict;

alter table solvedtask add constraint fk_solvedtask_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_solvedtask_user_id on solvedtask (user_id);

alter table solvedtask add constraint fk_solvedtask_task_id foreign key (task_id) references task (id) on delete restrict on update restrict;
create index ix_solvedtask_task_id on solvedtask (task_id);

alter table storychallenge add constraint fk_storychallenge_next_id foreign key (next_id) references storychallenge (id) on delete restrict on update restrict;

alter table storychallenge_text add constraint fk_storychallenge_text_storychallenge foreign key (storychallenge_id) references storychallenge (id) on delete restrict on update restrict;
create index ix_storychallenge_text_storychallenge on storychallenge_text (storychallenge_id);

alter table storychallenge_text add constraint fk_storychallenge_text_text foreign key (text_id) references text (id) on delete restrict on update restrict;
create index ix_storychallenge_text_text on storychallenge_text (text_id);

alter table storychallenge_map add constraint fk_storychallenge_map_storychallenge foreign key (storychallenge_id) references storychallenge (id) on delete restrict on update restrict;
create index ix_storychallenge_map_storychallenge on storychallenge_map (storychallenge_id);

alter table storychallenge_map add constraint fk_storychallenge_map_map foreign key (map_id) references map (id) on delete restrict on update restrict;
create index ix_storychallenge_map_map on storychallenge_map (map_id);

alter table submittedhomework add constraint fk_submittedhomework_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_submittedhomework_user_id on submittedhomework (user_id);

alter table submittedhomework add constraint fk_submittedhomework_task_id foreign key (task_id) references task (id) on delete restrict on update restrict;
create index ix_submittedhomework_task_id on submittedhomework (task_id);

alter table submittedhomework add constraint fk_submittedhomework_home_work_id foreign key (home_work_id) references homework (id) on delete restrict on update restrict;
create index ix_submittedhomework_home_work_id on submittedhomework (home_work_id);

alter table tabledefinition add constraint fk_tabledefinition_task_set_id foreign key (task_set_id) references taskset (id) on delete restrict on update restrict;
create index ix_tabledefinition_task_set_id on tabledefinition (task_set_id);

alter table task add constraint fk_task_task_set_id foreign key (task_set_id) references taskset (id) on delete restrict on update restrict;
create index ix_task_task_set_id on task (task_set_id);

alter table task add constraint fk_task_creator_id foreign key (creator_id) references user (id) on delete restrict on update restrict;
create index ix_task_creator_id on task (creator_id);

alter table taskset add constraint fk_taskset_creator_id foreign key (creator_id) references user (id) on delete restrict on update restrict;
create index ix_taskset_creator_id on taskset (creator_id);

alter table user add constraint fk_user_role_id foreign key (role_id) references role (id) on delete restrict on update restrict;
create index ix_user_role_id on user (role_id);

alter table user add constraint fk_user_avatar_id foreign key (avatar_id) references avatar (id) on delete restrict on update restrict;
create index ix_user_avatar_id on user (avatar_id);

alter table user add constraint fk_user_current_story_id foreign key (current_story_id) references storychallenge (id) on delete restrict on update restrict;
create index ix_user_current_story_id on user (current_story_id);

alter table user add constraint fk_user_current_scroll_id foreign key (current_scroll_id) references scroll (id) on delete restrict on update restrict;
create index ix_user_current_scroll_id on user (current_scroll_id);

alter table user add constraint fk_user_current_task_set_id foreign key (current_task_set_id) references taskset (id) on delete restrict on update restrict;
create index ix_user_current_task_set_id on user (current_task_set_id);

alter table user add constraint fk_user_current_home_work_id foreign key (current_home_work_id) references homework (id) on delete restrict on update restrict;
create index ix_user_current_home_work_id on user (current_home_work_id);

alter table user add constraint fk_user_group_id foreign key (group_id) references user_group (id) on delete restrict on update restrict;
create index ix_user_group_id on user (group_id);

alter table user_shopitem add constraint fk_user_shopitem_user foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_shopitem_user on user_shopitem (user_id);

alter table user_shopitem add constraint fk_user_shopitem_shopitem foreign key (shopitem_id) references shopitem (id) on delete restrict on update restrict;
create index ix_user_shopitem_shopitem on user_shopitem (shopitem_id);

alter table user_group add constraint fk_user_group_tutor_id foreign key (tutor_id) references user (id) on delete restrict on update restrict;
create index ix_user_group_tutor_id on user_group (tutor_id);


# --- !Downs

alter table columndefinition drop foreign key fk_columndefinition_table_definition_id;
drop index ix_columndefinition_table_definition_id on columndefinition;

alter table columndefinition drop foreign key fk_columndefinition_foreign_key_id;
drop index ix_columndefinition_foreign_key_id on columndefinition;

alter table comment drop foreign key fk_comment_task_set_id;
drop index ix_comment_task_set_id on comment;

alter table comment drop foreign key fk_comment_task_id;
drop index ix_comment_task_id on comment;

alter table comment drop foreign key fk_comment_creator_id;
drop index ix_comment_creator_id on comment;

alter table foreignkeyrelation drop foreign key fk_foreignkeyrelation_task_set_id;
drop index ix_foreignkeyrelation_task_set_id on foreignkeyrelation;

alter table homework drop foreign key fk_homework_creator_id;
drop index ix_homework_creator_id on homework;

alter table homework_taskset drop foreign key fk_homework_taskset_homework;
drop index ix_homework_taskset_homework on homework_taskset;

alter table homework_taskset drop foreign key fk_homework_taskset_taskset;
drop index ix_homework_taskset_taskset on homework_taskset;

alter table inventory drop foreign key fk_inventory_user_id;
drop index ix_inventory_user_id on inventory;

alter table inventory drop foreign key fk_inventory_potion_id;
drop index ix_inventory_potion_id on inventory;

alter table loficoinflowlog drop foreign key fk_loficoinflowlog_user_id;
drop index ix_loficoinflowlog_user_id on loficoinflowlog;

alter table rating drop foreign key fk_rating_user_id;
drop index ix_rating_user_id on rating;

alter table rating drop foreign key fk_rating_task_set_id;
drop index ix_rating_task_set_id on rating;

alter table rating drop foreign key fk_rating_task_id;
drop index ix_rating_task_id on rating;

alter table role drop foreign key fk_role_creator_id;
drop index ix_role_creator_id on role;

alter table scroll drop foreign key fk_scroll_potion_id;

alter table scrollcollection drop foreign key fk_scrollcollection_user_id;
drop index ix_scrollcollection_user_id on scrollcollection;

alter table scrollcollection drop foreign key fk_scrollcollection_scroll_id;
drop index ix_scrollcollection_scroll_id on scrollcollection;

alter table session drop foreign key fk_session_owner_id;
drop index ix_session_owner_id on session;

alter table shopitem drop foreign key fk_shopitem_avatar_id;

alter table solvedtask drop foreign key fk_solvedtask_user_id;
drop index ix_solvedtask_user_id on solvedtask;

alter table solvedtask drop foreign key fk_solvedtask_task_id;
drop index ix_solvedtask_task_id on solvedtask;

alter table storychallenge drop foreign key fk_storychallenge_next_id;

alter table storychallenge_text drop foreign key fk_storychallenge_text_storychallenge;
drop index ix_storychallenge_text_storychallenge on storychallenge_text;

alter table storychallenge_text drop foreign key fk_storychallenge_text_text;
drop index ix_storychallenge_text_text on storychallenge_text;

alter table storychallenge_map drop foreign key fk_storychallenge_map_storychallenge;
drop index ix_storychallenge_map_storychallenge on storychallenge_map;

alter table storychallenge_map drop foreign key fk_storychallenge_map_map;
drop index ix_storychallenge_map_map on storychallenge_map;

alter table submittedhomework drop foreign key fk_submittedhomework_user_id;
drop index ix_submittedhomework_user_id on submittedhomework;

alter table submittedhomework drop foreign key fk_submittedhomework_task_id;
drop index ix_submittedhomework_task_id on submittedhomework;

alter table submittedhomework drop foreign key fk_submittedhomework_home_work_id;
drop index ix_submittedhomework_home_work_id on submittedhomework;

alter table tabledefinition drop foreign key fk_tabledefinition_task_set_id;
drop index ix_tabledefinition_task_set_id on tabledefinition;

alter table task drop foreign key fk_task_task_set_id;
drop index ix_task_task_set_id on task;

alter table task drop foreign key fk_task_creator_id;
drop index ix_task_creator_id on task;

alter table taskset drop foreign key fk_taskset_creator_id;
drop index ix_taskset_creator_id on taskset;

alter table user drop foreign key fk_user_role_id;
drop index ix_user_role_id on user;

alter table user drop foreign key fk_user_avatar_id;
drop index ix_user_avatar_id on user;

alter table user drop foreign key fk_user_current_story_id;
drop index ix_user_current_story_id on user;

alter table user drop foreign key fk_user_current_scroll_id;
drop index ix_user_current_scroll_id on user;

alter table user drop foreign key fk_user_current_task_set_id;
drop index ix_user_current_task_set_id on user;

alter table user drop foreign key fk_user_current_home_work_id;
drop index ix_user_current_home_work_id on user;

alter table user drop foreign key fk_user_group_id;
drop index ix_user_group_id on user;

alter table user_shopitem drop foreign key fk_user_shopitem_user;
drop index ix_user_shopitem_user on user_shopitem;

alter table user_shopitem drop foreign key fk_user_shopitem_shopitem;
drop index ix_user_shopitem_shopitem on user_shopitem;

alter table user_group drop foreign key fk_user_group_tutor_id;
drop index ix_user_group_tutor_id on user_group;

drop table if exists avatar;

drop table if exists columndefinition;

drop table if exists comment;

drop table if exists foreignkeyrelation;

drop table if exists homework;

drop table if exists homework_taskset;

drop table if exists inventory;

drop table if exists loficoinflowlog;

drop table if exists map;

drop table if exists potion;

drop table if exists rating;

drop table if exists role;

drop table if exists scroll;

drop table if exists scrollcollection;

drop table if exists session;

drop table if exists shopitem;

drop table if exists solvedtask;

drop table if exists storychallenge;

drop table if exists storychallenge_text;

drop table if exists storychallenge_map;

drop table if exists submittedhomework;

drop table if exists tabledefinition;

drop table if exists task;

drop table if exists taskset;

drop table if exists text;

drop table if exists user;

drop table if exists user_shopitem;

drop table if exists user_group;


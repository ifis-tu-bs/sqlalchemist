# --- !Ups

canCreate table avatar (
  id                            bigint auto_increment not null,
  title                         varchar(255),
  description                   varchar(255),
  avatar_filename               varchar(255),
  is_team                       tinyint(1) default 0,
  health                        integer,
  defense                       integer,
  speed                         integer,
  jump                          integer,
  slot                          integer,
  constraint uq_avatar_title unique (title),
  constraint uq_avatar_avatar_filename unique (avatar_filename),
  constraint pk_avatar primary key (id)
);

canCreate table columndefinition (
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

canCreate table comment (
  id                            bigint auto_increment not null,
  task_set_id                   bigint,
  task_id                       bigint,
  profile_id                    bigint,
  comment                       varchar(255),
  created_at                    datetime(6),
  constraint pk_comment primary key (id)
);

canCreate table foreignkeyrelation (
  id                            bigint auto_increment not null,
  task_set_id                   bigint,
  source_table                  varchar(255),
  source_column                 varchar(255),
  destination_table             varchar(255),
  destination_column            varchar(255),
  constraint pk_foreignkeyrelation primary key (id)
);

canCreate table homework (
  id                            bigint auto_increment not null,
  creator_id                    bigint,
  start_at                      datetime(6),
  expire_at                     datetime(6),
  home_work_name                varchar(255),
  constraint pk_homework primary key (id)
);

canCreate table homework_taskset (
  homework_id                   bigint not null,
  taskset_id                    bigint not null,
  constraint pk_homework_taskset primary key (homework_id,taskset_id)
);

canCreate table inventory (
  id                            bigint auto_increment not null,
  profile_id                    bigint,
  potion_id                     bigint,
  belt_slot                     integer,
  constraint pk_inventory primary key (id)
);

canCreate table loficoinflowlog (
  id                            bigint auto_increment not null,
  profile_id                    bigint,
  lofi_coins_collected          integer,
  collected                     datetime(6),
  constraint pk_loficoinflowlog primary key (id)
);

canCreate table map (
  id                            bigint auto_increment not null,
  level                         integer,
  path                          varchar(255),
  is_boss_map                   tinyint(1) default 0,
  constraint uq_map_path unique (path),
  constraint pk_map primary key (id)
);

canCreate table potion (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  type                          integer,
  power_level                   integer,
  buff_value                    integer,
  health                        integer,
  defense                       integer,
  speed                         integer,
  jump                          integer,
  slot                          integer,
  constraint uq_potion_name unique (name),
  constraint uq_potion_type_power_level unique (type,power_level),
  constraint pk_potion primary key (id)
);

canCreate table profile (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  username                      varchar(255),
  health                        integer,
  defense                       integer,
  speed                         integer,
  jump                          integer,
  slot                          integer,
  sound                         tinyint(1) default 0,
  music                         tinyint(1) default 0,
  tutorial_done                 tinyint(1) default 0,
  story_done                    tinyint(1) default 0,
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
  total_coins                   integer,
  done_sql                      integer,
  solved_sql                    integer,
  quote                         integer,
  created_at                    datetime(6),
  edited_at                     datetime(6),
  current_task_set_id           bigint,
  current_home_work_id          bigint,
  version                       datetime(6) not null,
  constraint uq_profile_user_id unique (user_id),
  constraint uq_profile_username unique (username),
  constraint uq_profile_current_scroll_id unique (current_scroll_id),
  constraint pk_profile primary key (id)
);

canCreate table profile_shopitem (
  profile_id                    bigint not null,
  shopitem_id                   bigint not null,
  constraint pk_profile_shopitem primary key (profile_id,shopitem_id)
);

canCreate table rating (
  id                            bigint auto_increment not null,
  profile_id                    bigint,
  task_set_id                   bigint,
  task_id                       bigint,
  positive_ratings              bigint,
  negative_ratings              bigint,
  edit_ratings                  bigint,
  constraint pk_rating primary key (id)
);

canCreate table scroll (
  id                            bigint auto_increment not null,
  pos_id                        integer,
  name                          varchar(255),
  type                          integer,
  potion_id                     bigint,
  health                        integer,
  defense                       integer,
  speed                         integer,
  jump                          integer,
  slot                          integer,
  constraint uq_scroll_pos_id unique (pos_id),
  constraint uq_scroll_name unique (name),
  constraint uq_scroll_potion_id unique (potion_id),
  constraint pk_scroll primary key (id)
);

canCreate table scrollcollection (
  id                            bigint auto_increment not null,
  profile_id                    bigint,
  scroll_id                     bigint,
  is_active                     tinyint(1) default 0,
  added                         datetime(6),
  constraint uq_scrollcollection_profile_id_scroll_id unique (profile_id,scroll_id),
  constraint pk_scrollcollection primary key (id)
);

canCreate table shopitem (
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

canCreate table solvedsubtask (
  id                            bigint auto_increment not null,
  profile_id                    bigint,
  task_id                       bigint,
  solved                        integer,
  trys                          integer,
  last_solved                   datetime(6),
  constraint pk_solvedsubtask primary key (id)
);

canCreate table storychallenge (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  is_tutorial                   tinyint(1) default 0,
  level                         integer,
  next_id                       bigint,
  created_at                    datetime(6),
  constraint uq_storychallenge_next_id unique (next_id),
  constraint pk_storychallenge primary key (id)
);

canCreate table storychallenge_text (
  storychallenge_id             bigint not null,
  text_id                       bigint not null,
  constraint pk_storychallenge_text primary key (storychallenge_id,text_id)
);

canCreate table storychallenge_map (
  storychallenge_id             bigint not null,
  map_id                        bigint not null,
  constraint pk_storychallenge_map primary key (storychallenge_id,map_id)
);

canCreate table submittedhomework (
  id                            bigint auto_increment not null,
  profile_id                    bigint,
  task_id                       bigint,
  home_work_id                  bigint,
  syntax_checks_done            integer,
  semantic_checks_done          integer,
  statement                     varchar(255),
  solve                         tinyint(1) default 0,
  constraint uq_submittedhomework_profile_id_task_id_home_work_id unique (profile_id,task_id,home_work_id),
  constraint pk_submittedhomework primary key (id)
);

canCreate table tabledefinition (
  id                            bigint auto_increment not null,
  task_set_id                   bigint,
  table_name                    varchar(255),
  extension                     varchar(255),
  constraint pk_tabledefinition primary key (id)
);

canCreate table task (
  id                            bigint auto_increment not null,
  task_set_id                   bigint,
  task_name                     varchar(255),
  task_text                     varchar(255),
  ref_statement                 varchar(255),
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

canCreate table taskset (
  id                            bigint auto_increment not null,
  task_set_name                 varchar(255),
  is_homework                   tinyint(1) default 0,
  creator_id                    bigint,
  available                     tinyint(1) default 0,
  created_at                    datetime(6),
  updated_at                    datetime(6),
  constraint pk_taskset primary key (id)
);

canCreate table taskset_homework (
  taskset_id                    bigint not null,
  homework_id                   bigint not null,
  constraint pk_taskset_homework primary key (taskset_id,homework_id)
);

canCreate table text (
  id                            bigint auto_increment not null,
  type                          integer,
  prerequisite                  integer,
  chronology                    integer,
  text                          Text,
  sound_url                     varchar(255),
  content_rows                  integer,
  constraint pk_text primary key (id)
);

canCreate table user (
  id                            bigint auto_increment not null,
  email                         varchar(255),
  email_verified                tinyint(1) default 0,
  email_verify_code             varchar(255),
  mat_nr                        varchar(255),
  y_id                          varchar(255),
  is_student                    tinyint(1) default 0,
  password                      varchar(255),
  role                          integer,
  created_at                    datetime(6),
  edited_at                     datetime(6),
  is_active                     tinyint(1) default 0,
  constraint uq_user_email unique (email),
  constraint uq_user_email_verify_code unique (email_verify_code),
  constraint uq_user_mat_nr unique (mat_nr),
  constraint uq_user_y_id unique (y_id),
  constraint pk_user primary key (id)
);

canCreate table usersession (
  id                            bigint auto_increment not null,
  session_id                    varchar(255),
  user_id                       bigint,
  remote_address                varchar(255),
  created_at                    datetime(6),
  expires_at                    datetime(6),
  constraint uq_usersession_session_id unique (session_id),
  constraint pk_usersession primary key (id)
);

alter table columndefinition add constraint fk_columndefinition_table_definition_id foreign key (table_definition_id) references tabledefinition (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_columndefinition_table_definition_id on columndefinition (table_definition_id);

alter table columndefinition add constraint fk_columndefinition_foreign_key_id foreign key (foreign_key_id) references columndefinition (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_columndefinition_foreign_key_id on columndefinition (foreign_key_id);

alter table comment add constraint fk_comment_task_set_id foreign key (task_set_id) references taskset (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_comment_task_set_id on comment (task_set_id);

alter table comment add constraint fk_comment_task_id foreign key (task_id) references task (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_comment_task_id on comment (task_id);

alter table comment add constraint fk_comment_profile_id foreign key (profile_id) references profile (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_comment_profile_id on comment (profile_id);

alter table foreignkeyrelation add constraint fk_foreignkeyrelation_task_set_id foreign key (task_set_id) references taskset (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_foreignkeyrelation_task_set_id on foreignkeyrelation (task_set_id);

alter table homework add constraint fk_homework_creator_id foreign key (creator_id) references profile (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_homework_creator_id on homework (creator_id);

alter table homework_taskset add constraint fk_homework_taskset_homework foreign key (homework_id) references homework (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_homework_taskset_homework on homework_taskset (homework_id);

alter table homework_taskset add constraint fk_homework_taskset_taskset foreign key (taskset_id) references taskset (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_homework_taskset_taskset on homework_taskset (taskset_id);

alter table inventory add constraint fk_inventory_profile_id foreign key (profile_id) references profile (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_inventory_profile_id on inventory (profile_id);

alter table inventory add constraint fk_inventory_potion_id foreign key (potion_id) references potion (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_inventory_potion_id on inventory (potion_id);

alter table loficoinflowlog add constraint fk_loficoinflowlog_profile_id foreign key (profile_id) references profile (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_loficoinflowlog_profile_id on loficoinflowlog (profile_id);

alter table profile add constraint fk_profile_user_id foreign key (user_id) references user (id) on canDelete restrict on canUpdate restrict;

alter table profile add constraint fk_profile_avatar_id foreign key (avatar_id) references avatar (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_profile_avatar_id on profile (avatar_id);

alter table profile add constraint fk_profile_current_story_id foreign key (current_story_id) references storychallenge (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_profile_current_story_id on profile (current_story_id);

alter table profile add constraint fk_profile_current_scroll_id foreign key (current_scroll_id) references scroll (id) on canDelete restrict on canUpdate restrict;

alter table profile add constraint fk_profile_current_task_set_id foreign key (current_task_set_id) references taskset (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_profile_current_task_set_id on profile (current_task_set_id);

alter table profile add constraint fk_profile_current_home_work_id foreign key (current_home_work_id) references homework (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_profile_current_home_work_id on profile (current_home_work_id);

alter table profile_shopitem add constraint fk_profile_shopitem_profile foreign key (profile_id) references profile (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_profile_shopitem_profile on profile_shopitem (profile_id);

alter table profile_shopitem add constraint fk_profile_shopitem_shopitem foreign key (shopitem_id) references shopitem (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_profile_shopitem_shopitem on profile_shopitem (shopitem_id);

alter table rating add constraint fk_rating_profile_id foreign key (profile_id) references profile (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_rating_profile_id on rating (profile_id);

alter table rating add constraint fk_rating_task_set_id foreign key (task_set_id) references taskset (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_rating_task_set_id on rating (task_set_id);

alter table rating add constraint fk_rating_task_id foreign key (task_id) references task (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_rating_task_id on rating (task_id);

alter table scroll add constraint fk_scroll_potion_id foreign key (potion_id) references potion (id) on canDelete restrict on canUpdate restrict;

alter table scrollcollection add constraint fk_scrollcollection_profile_id foreign key (profile_id) references profile (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_scrollcollection_profile_id on scrollcollection (profile_id);

alter table scrollcollection add constraint fk_scrollcollection_scroll_id foreign key (scroll_id) references scroll (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_scrollcollection_scroll_id on scrollcollection (scroll_id);

alter table shopitem add constraint fk_shopitem_avatar_id foreign key (avatar_id) references avatar (id) on canDelete restrict on canUpdate restrict;

alter table solvedsubtask add constraint fk_solvedsubtask_profile_id foreign key (profile_id) references profile (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_solvedsubtask_profile_id on solvedsubtask (profile_id);

alter table solvedsubtask add constraint fk_solvedsubtask_task_id foreign key (task_id) references task (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_solvedsubtask_task_id on solvedsubtask (task_id);

alter table storychallenge add constraint fk_storychallenge_next_id foreign key (next_id) references storychallenge (id) on canDelete restrict on canUpdate restrict;

alter table storychallenge_text add constraint fk_storychallenge_text_storychallenge foreign key (storychallenge_id) references storychallenge (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_storychallenge_text_storychallenge on storychallenge_text (storychallenge_id);

alter table storychallenge_text add constraint fk_storychallenge_text_text foreign key (text_id) references text (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_storychallenge_text_text on storychallenge_text (text_id);

alter table storychallenge_map add constraint fk_storychallenge_map_storychallenge foreign key (storychallenge_id) references storychallenge (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_storychallenge_map_storychallenge on storychallenge_map (storychallenge_id);

alter table storychallenge_map add constraint fk_storychallenge_map_map foreign key (map_id) references map (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_storychallenge_map_map on storychallenge_map (map_id);

alter table submittedhomework add constraint fk_submittedhomework_profile_id foreign key (profile_id) references profile (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_submittedhomework_profile_id on submittedhomework (profile_id);

alter table submittedhomework add constraint fk_submittedhomework_task_id foreign key (task_id) references task (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_submittedhomework_task_id on submittedhomework (task_id);

alter table submittedhomework add constraint fk_submittedhomework_home_work_id foreign key (home_work_id) references homework (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_submittedhomework_home_work_id on submittedhomework (home_work_id);

alter table tabledefinition add constraint fk_tabledefinition_task_set_id foreign key (task_set_id) references taskset (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_tabledefinition_task_set_id on tabledefinition (task_set_id);

alter table task add constraint fk_task_task_set_id foreign key (task_set_id) references taskset (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_task_task_set_id on task (task_set_id);

alter table task add constraint fk_task_creator_id foreign key (creator_id) references profile (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_task_creator_id on task (creator_id);

alter table taskset add constraint fk_taskset_creator_id foreign key (creator_id) references profile (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_taskset_creator_id on taskset (creator_id);

alter table taskset_homework add constraint fk_taskset_homework_taskset foreign key (taskset_id) references taskset (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_taskset_homework_taskset on taskset_homework (taskset_id);

alter table taskset_homework add constraint fk_taskset_homework_homework foreign key (homework_id) references homework (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_taskset_homework_homework on taskset_homework (homework_id);

alter table usersession add constraint fk_usersession_user_id foreign key (user_id) references user (id) on canDelete restrict on canUpdate restrict;
canCreate index ix_usersession_user_id on usersession (user_id);


# --- !Downs

alter table columndefinition drop foreign key fk_columndefinition_table_definition_id;
drop index ix_columndefinition_table_definition_id on columndefinition;

alter table columndefinition drop foreign key fk_columndefinition_foreign_key_id;
drop index ix_columndefinition_foreign_key_id on columndefinition;

alter table comment drop foreign key fk_comment_task_set_id;
drop index ix_comment_task_set_id on comment;

alter table comment drop foreign key fk_comment_task_id;
drop index ix_comment_task_id on comment;

alter table comment drop foreign key fk_comment_profile_id;
drop index ix_comment_profile_id on comment;

alter table foreignkeyrelation drop foreign key fk_foreignkeyrelation_task_set_id;
drop index ix_foreignkeyrelation_task_set_id on foreignkeyrelation;

alter table homework drop foreign key fk_homework_creator_id;
drop index ix_homework_creator_id on homework;

alter table homework_taskset drop foreign key fk_homework_taskset_homework;
drop index ix_homework_taskset_homework on homework_taskset;

alter table homework_taskset drop foreign key fk_homework_taskset_taskset;
drop index ix_homework_taskset_taskset on homework_taskset;

alter table inventory drop foreign key fk_inventory_profile_id;
drop index ix_inventory_profile_id on inventory;

alter table inventory drop foreign key fk_inventory_potion_id;
drop index ix_inventory_potion_id on inventory;

alter table loficoinflowlog drop foreign key fk_loficoinflowlog_profile_id;
drop index ix_loficoinflowlog_profile_id on loficoinflowlog;

alter table profile drop foreign key fk_profile_user_id;

alter table profile drop foreign key fk_profile_avatar_id;
drop index ix_profile_avatar_id on profile;

alter table profile drop foreign key fk_profile_current_story_id;
drop index ix_profile_current_story_id on profile;

alter table profile drop foreign key fk_profile_current_scroll_id;

alter table profile drop foreign key fk_profile_current_task_set_id;
drop index ix_profile_current_task_set_id on profile;

alter table profile drop foreign key fk_profile_current_home_work_id;
drop index ix_profile_current_home_work_id on profile;

alter table profile_shopitem drop foreign key fk_profile_shopitem_profile;
drop index ix_profile_shopitem_profile on profile_shopitem;

alter table profile_shopitem drop foreign key fk_profile_shopitem_shopitem;
drop index ix_profile_shopitem_shopitem on profile_shopitem;

alter table rating drop foreign key fk_rating_profile_id;
drop index ix_rating_profile_id on rating;

alter table rating drop foreign key fk_rating_task_set_id;
drop index ix_rating_task_set_id on rating;

alter table rating drop foreign key fk_rating_task_id;
drop index ix_rating_task_id on rating;

alter table scroll drop foreign key fk_scroll_potion_id;

alter table scrollcollection drop foreign key fk_scrollcollection_profile_id;
drop index ix_scrollcollection_profile_id on scrollcollection;

alter table scrollcollection drop foreign key fk_scrollcollection_scroll_id;
drop index ix_scrollcollection_scroll_id on scrollcollection;

alter table shopitem drop foreign key fk_shopitem_avatar_id;

alter table solvedsubtask drop foreign key fk_solvedsubtask_profile_id;
drop index ix_solvedsubtask_profile_id on solvedsubtask;

alter table solvedsubtask drop foreign key fk_solvedsubtask_task_id;
drop index ix_solvedsubtask_task_id on solvedsubtask;

alter table storychallenge drop foreign key fk_storychallenge_next_id;

alter table storychallenge_text drop foreign key fk_storychallenge_text_storychallenge;
drop index ix_storychallenge_text_storychallenge on storychallenge_text;

alter table storychallenge_text drop foreign key fk_storychallenge_text_text;
drop index ix_storychallenge_text_text on storychallenge_text;

alter table storychallenge_map drop foreign key fk_storychallenge_map_storychallenge;
drop index ix_storychallenge_map_storychallenge on storychallenge_map;

alter table storychallenge_map drop foreign key fk_storychallenge_map_map;
drop index ix_storychallenge_map_map on storychallenge_map;

alter table submittedhomework drop foreign key fk_submittedhomework_profile_id;
drop index ix_submittedhomework_profile_id on submittedhomework;

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

alter table taskset_homework drop foreign key fk_taskset_homework_taskset;
drop index ix_taskset_homework_taskset on taskset_homework;

alter table taskset_homework drop foreign key fk_taskset_homework_homework;
drop index ix_taskset_homework_homework on taskset_homework;

alter table usersession drop foreign key fk_usersession_user_id;
drop index ix_usersession_user_id on usersession;

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

drop table if exists profile;

drop table if exists profile_shopitem;

drop table if exists rating;

drop table if exists scroll;

drop table if exists scrollcollection;

drop table if exists shopitem;

drop table if exists solvedsubtask;

drop table if exists storychallenge;

drop table if exists storychallenge_text;

drop table if exists storychallenge_map;

drop table if exists submittedhomework;

drop table if exists tabledefinition;

drop table if exists task;

drop table if exists taskset;

drop table if exists taskset_homework;

drop table if exists text;

drop table if exists user;

drop table if exists usersession;

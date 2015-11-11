# --- !Ups
ALTER TABLE profile
ADD COLUMN current_task_set_id bigint;
alter table profile add constraint current_task_set_id foreign key (current_task_set_id) references taskset (id);

# --- !Downs
ALTER TABLE profile
DROP COLUMN current_task_set_id;
alter table profile drop foreign key fk_profile_current_task_set_id;

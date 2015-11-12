# --- !Ups
ALTER TABLE taskset DROP COLUMN relations_formatted;
ALTER TABLE profile ADD COLUMN current_task_set_id bigint;
ALTER TABLE profile ADD CONSTRAINT fk_profile_current_task_set_id FOREIGN KEY (current_task_set_id) REFERENCES taskset (id);
ALTER TABLE taskset ADD COLUMN available tinyint(1) default 0;
ALTER TABLE task ADD COLUMN available tinyint(1) default 0;
ALTER TABLE profile ADD COLUMN current_home_work_id bigint;
ALTER TABLE profile ADD CONSTRAINT fk_profile_current_home_work_id FOREIGN KEY (current_home_work_id) REFERENCES homework (id);


# --- !Downs
ALTER TABLE taskset ADD COLUMN relations_formatted TEXT;
ALTER TABLE profile DROP FOREIGN KEY fk_profile_current_task_set_id;
ALTER TABLE profile DROP COLUMN current_task_set_id;
ALTER TABLE taskset DROP COLUMN available;
ALTER TABLE task DROP COLUMN available;
ALTER TABLE profile DROP FOREIGN KEY fk_profile_current_home_work_id;
ALTER TABLE profile DROP COLUMN current_home_work_id;


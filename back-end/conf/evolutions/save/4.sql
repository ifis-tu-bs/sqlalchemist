# --- !Ups
ALTER TABLE taskset ADD COLUMN available tinyint(1) default 0;
ALTER TABLE task ADD COLUMN available tinyint(1) default 0;


# --- !Downs
ALTER TABLE taskset DROP COLUMN available;
ALTER TABLE task DROP COLUMN available;

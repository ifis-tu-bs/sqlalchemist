# --- !Ups

ALTER TABLE tabledefinition
 MODIFY COLUMN extension MEDIUMTEXT;

ALTER TABLE session
 ADD active TINYINT;

ALTER TABLE user
 DROP COLUMN scroll_limit;

# --- !Downs

ALTER TABLE session
 DROP COLUMN active;

ALTER TABLE tabledefinition
 MODIFY COLUMN extension TEXT;

ALTER TABLE user
 ADD scroll_limit integer;

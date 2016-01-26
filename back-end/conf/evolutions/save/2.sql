# --- !Ups

ALTER TABLE foreignkeyrelation ADD combined_key_id integer;


# --- !Downs

ALTER TABLE foreignkeyrelation DROP COLUMN combined_key_id;
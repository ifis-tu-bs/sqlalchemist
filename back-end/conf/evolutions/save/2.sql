# --- !Ups
ALTER TABLE taskset
DROP COLUMN relations_formatted;

# --- !Downs
ALTER TABLE taskset
ADD COLUMN relations_formatted TEXT;

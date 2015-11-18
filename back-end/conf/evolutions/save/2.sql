# --- !Ups

ALTER TABLE submittedhomework MODIFY statement TEXT;
ALTER TABLE tabledefinition MODIFY extension TEXT;

# --- !Downs

ALTER TABLE submittedhomework MODIFY  statement varchar(255);
ALTER TABLE tabledefinition MODIFY extension varchar(255);

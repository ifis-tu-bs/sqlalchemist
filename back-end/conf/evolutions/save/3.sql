# --- !Ups

ALTER TABLE solvedtask ADD statement Text;
ALTER TABLE solvedtask ADD sql_status varchar(255);
ALTER TABLE solvedtask ADD mode varchar(255);

# --- !Downs

ALTER TABLE solvedtask DROP COLUMN statement;
ALTER TABLE solvedtask DROP COLUMN sql_status;
ALTER TABLE solvedtask DROP COLUMN mode;


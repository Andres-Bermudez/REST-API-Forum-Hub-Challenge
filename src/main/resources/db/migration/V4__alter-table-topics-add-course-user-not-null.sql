UPDATE topics
SET id_course = 1;

UPDATE topics
SET id_user = 1;

ALTER TABLE topics
ALTER COLUMN id_course SET NOT NULL;

ALTER TABLE topics
ALTER COLUMN id_user SET NOT NULL;



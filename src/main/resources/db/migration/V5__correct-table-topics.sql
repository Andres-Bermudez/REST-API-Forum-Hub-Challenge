ALTER TABLE topics
DROP COLUMN id_course;

ALTER TABLE topics
DROP COLUMN id_user;

UPDATE topics
SET course_id = 1;

UPDATE topics
SET user_id = 1;

ALTER TABLE topics
ALTER COLUMN course_id SET NOT NULL;

ALTER TABLE topics
ALTER COLUMN user_id SET NOT NULL;

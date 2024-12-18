ALTER TABLE topics
ADD COLUMN active_status BOOLEAN;

UPDATE topics
SET active_status = TRUE;

ALTER TABLE topics
ALTER COLUMN active_status SET NOT NULL;

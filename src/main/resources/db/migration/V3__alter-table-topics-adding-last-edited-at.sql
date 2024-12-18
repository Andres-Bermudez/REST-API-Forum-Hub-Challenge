ALTER TABLE topics
ADD COLUMN last_edited_at TIMESTAMP;

UPDATE topics
SET last_edited_at = created_date;

ALTER TABLE topics
ALTER COLUMN last_edited_at SET NOT NULL;

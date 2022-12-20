CREATE TABLE tasks
(
	id			SERIAL			primary key,
	created_at	TIMESTAMP		not null default current_timestamp,
	updated_at	TIMESTAMP		null,
	due_date	DATE			not null,
	resolved_at	DATE			null,
	title		VARCHAR(80)		not null,
	description	TEXT			null,
	priority	CHAR(2)			not null, -- hi/md/lo
	status		CHAR(4)			not null  -- wait/work/done
);

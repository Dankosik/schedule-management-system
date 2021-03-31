alter table lessons
    drop column duration,
    add column duration bigint default 5400000000000;



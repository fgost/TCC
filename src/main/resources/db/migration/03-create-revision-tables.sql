 --liquibase formatted sql

--changeset fghost:3
CREATE TABLE IF NOT EXISTS revinfo (
    rev integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    revtstmp bigint DEFAULT NULL,
    user_id integer NOT NULL,
    CONSTRAINT revinfo_pk PRIMARY KEY (rev)
 );

CREATE TABLE IF NOT EXISTS users_audit (
	id integer NOT NULL,
	rev integer NOT NULL,
	revtype int2 NULL,
	code varchar(255) NULL,
	email varchar(255) NULL,
	last_name varchar(255) NULL,
	name varchar(255) NULL,
	password varchar(255) NULL,
	CONSTRAINT users_audit_pk PRIMARY KEY (rev, id),
	CONSTRAINT fk_rev FOREIGN KEY (rev) REFERENCES revinfo(rev)
);

ALTER TABLE IF EXISTS revinfo OWNER to "user-carview-api-java";
ALTER TABLE IF EXISTS users_audit OWNER to "user-carview-api-java";

CREATE INDEX IF NOT EXISTS ix_revinfo_rev
    ON revinfo USING btree
    (rev ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS ix_users_audit_id
    ON users_audit USING btree
    (id ASC NULLS LAST)
    TABLESPACE pg_default;

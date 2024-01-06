--liquibase formatted sql

--changeset fghost:9
CREATE TABLE IF NOT EXISTS auto_maker (
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    code character varying(36) NOT NULL,
    name character varying(36) NOT NULL,
    CONSTRAINT pk_auto_maker PRIMARY KEY(id)
);

COMMENT ON TABLE auto_maker IS 'This table provides basic information about the auto makers.';
COMMENT ON COLUMN auto_maker.id IS 'Column responsible for information about the auto maker ID.';
COMMENT ON COLUMN auto_maker.code IS 'Column responsible for information about the auto maker code, or external ID.';
COMMENT ON COLUMN auto_maker.name IS 'Column responsible for setting the auto maker name.';
COMMENT ON CONSTRAINT pk_auto_maker ON auto_maker IS 'Constraint responsible for guaranteeing the uniqueness of information in the primary key of the auto maker.';

ALTER TABLE IF EXISTS auto_maker OWNER to "user-carview-api-java";

CREATE INDEX IF NOT EXISTS ix_auto_maker_id
    ON auto_maker USING btree
        (id ASC NULLS LAST)
        TABLESPACE pg_default;

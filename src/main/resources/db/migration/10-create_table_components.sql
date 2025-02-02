--liquibase formatted sql

--changeset fghost:1
CREATE TABLE IF NOT EXISTS auto_components (
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    code character varying(36) NOT NULL,
    component_name character varying(100) NOT NULL,
    CONSTRAINT pk_components PRIMARY KEY(id)
);

COMMENT ON TABLE auto_components IS 'This table provides basic information about auto components.';
COMMENT ON COLUMN auto_components.id IS 'Column responsible for auto components ID information.';
COMMENT ON COLUMN auto_components.code IS 'Column responsible for information about the auto components code, or external ID.';
COMMENT ON COLUMN auto_components.component_name IS 'Column responsible for auto components name information.';

ALTER TABLE IF EXISTS auto_components OWNER to "user-carview-api-java";

CREATE INDEX IF NOT EXISTS ix_auto_components_id
    ON auto_components USING btree
    (id ASC NULLS LAST)
    TABLESPACE pg_default;

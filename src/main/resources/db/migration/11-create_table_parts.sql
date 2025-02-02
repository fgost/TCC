--liquibase formatted sql

--changeset fghost:1
CREATE TABLE IF NOT EXISTS parts (
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    code character varying(36) NOT NULL,
    part_name character varying(100),
    component smallint,
    CONSTRAINT component_id_fk FOREIGN KEY (component) REFERENCES auto_components (id),
    CONSTRAINT pk_parts PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS parts_components (
       part_id integer NOT NULL,
       component_id integer NOT NULL,
       CONSTRAINT component_id_fk_components FOREIGN KEY (component_id) REFERENCES auto_components (id),
       CONSTRAINT part_id_fk_parts FOREIGN KEY (part_id) REFERENCES parts (id)
);

COMMENT ON TABLE parts IS 'This table provides basic information about auto part.';
COMMENT ON COLUMN parts.id IS 'Column responsible for auto part ID information.';
COMMENT ON COLUMN parts.code IS 'Column responsible for information about the auto part code, or external ID.';
COMMENT ON COLUMN parts.part_name IS 'Column responsible for auto part name information.';
COMMENT ON CONSTRAINT pk_parts ON parts IS 'Constraint responsible for guaranteeing the uniqueness of information in the primary key of the part.';

ALTER TABLE IF EXISTS parts OWNER to "user-carview-api-java";
ALTER TABLE IF EXISTS parts_components OWNER to "user-carview-api-java";

CREATE INDEX IF NOT EXISTS ix_parts_id
    ON parts USING btree
    (id ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS ix_parts_components_id
    ON parts_components USING btree
        (part_id ASC NULLS LAST)
        TABLESPACE pg_default;

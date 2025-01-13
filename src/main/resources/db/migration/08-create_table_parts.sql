--liquibase formatted sql

--changeset fghost:5
CREATE TABLE IF NOT EXISTS parts (
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    code character varying(36) NOT NULL,
    name character varying(36) NOT NULL, --*
    description character varying(36),
    serial_number character varying(36),
    manufacturer character varying(36),
    model character varying(36),
    installation_date character varying(36) NOT NULL, --*
    life_span integer NOT NULL, --*
    cost double precision NOT NULL, --*
    status smallint NOT NULL, --*
    type smallint NOT NULL, --*
    car smallint NOT NULL, --*
    detailedMaintenance smallint,
    CONSTRAINT car_id_fk FOREIGN KEY (car) REFERENCES cars (id),
    CONSTRAINT pk_parts PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS types_parts (
    type_id integer NOT NULL,
    part_id integer NOT NULL,
    CONSTRAINT part_id_fk_parts FOREIGN KEY (part_id) REFERENCES parts (id),
    CONSTRAINT type_id_fk_types FOREIGN KEY (type_id) REFERENCES types (id)
 );

 CREATE TABLE IF NOT EXISTS cars_parts (
     car_id integer NOT NULL,
     part_id integer NOT NULL,
     CONSTRAINT part_id_fk_parts FOREIGN KEY (part_id) REFERENCES parts (id),
     CONSTRAINT car_id_fk_types FOREIGN KEY (car_id) REFERENCES cars (id)
  );

COMMENT ON TABLE parts IS 'This table provides basic information about the part types.';
COMMENT ON COLUMN parts.id IS 'Column responsible for information about the part type ID.';
COMMENT ON COLUMN parts.code IS 'Column responsible for information about the part type code, or external ID.';
COMMENT ON COLUMN parts.name IS 'Column responsible for setting the part name.';
COMMENT ON COLUMN parts.description IS 'Column responsible for setting the summary description of the part.';
COMMENT ON COLUMN parts.serial_number IS 'Column responsible for setting the part serial number.';
COMMENT ON COLUMN parts.manufacturer IS 'Column responsible for setting the part manufacturer.';
COMMENT ON COLUMN parts.installation_date IS 'Column responsible for setting the part installation date.';
COMMENT ON COLUMN parts.life_span IS 'Column responsible for setting the part lifespan.';
COMMENT ON COLUMN parts.cost IS 'Column responsible for setting the part cost.';
COMMENT ON COLUMN parts.status IS 'Column responsible for setting the part status.';
COMMENT ON COLUMN parts.type IS 'Column responsible for setting the part type.';
COMMENT ON CONSTRAINT pk_parts ON parts IS 'Constraint responsible for guaranteeing the uniqueness of information in the primary key of the part.';

COMMENT ON TABLE types_parts IS 'This table provides basic information about the relationship between type and part.';
COMMENT ON COLUMN types_parts.type_id IS 'Column responsible for type identification information.';
COMMENT ON COLUMN types_parts.part_id IS 'Column responsible for part identification information.';
COMMENT ON CONSTRAINT type_id_fk_types ON types_parts IS 'Constraint that refer to the foreign key relationship to types table.';
COMMENT ON CONSTRAINT part_id_fk_parts ON types_parts IS 'Constraint that refer to the foreign key relationship to parts table.';

ALTER TABLE IF EXISTS parts OWNER to "user-carview-api-java";
ALTER TABLE IF EXISTS types_parts OWNER to "user-carview-api-java";

CREATE INDEX IF NOT EXISTS ix_part_id
    ON parts USING btree
        (id ASC NULLS LAST)
        TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS ix_types_parts_id
    ON types_parts USING btree
        (type_id ASC NULLS LAST)
        TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS ix_cars_parts_id
    ON cars_parts USING btree
        (car_id ASC NULLS LAST)
        TABLESPACE pg_default;

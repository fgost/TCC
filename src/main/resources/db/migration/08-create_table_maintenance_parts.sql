--liquibase formatted sql

--changeset fghost:5
CREATE TABLE IF NOT EXISTS maintenance_parts (
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    code character varying(36) NOT NULL,
    part smallint NOT NULL, --*
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
    life_span_type smallint NOT NULL,
    detailedMaintenance smallint,
    limiteParaAlerta double precision,
    limiteParaUrgencia double precision,
    CONSTRAINT car_id_fk FOREIGN KEY (car) REFERENCES cars (id),
    CONSTRAINT part_id_fk FOREIGN KEY (part) REFERENCES parts (id),
    CONSTRAINT pk_maintenance_parts PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS types_maintenance_parts (
    type_id integer NOT NULL,
    maintenance_part_id integer NOT NULL,
    CONSTRAINT maintenance_part_id_fk_maintenance_parts FOREIGN KEY (maintenance_part_id) REFERENCES maintenance_parts (id),
    CONSTRAINT type_id_fk_types FOREIGN KEY (type_id) REFERENCES types (id)
 );

 CREATE TABLE IF NOT EXISTS cars_maintenance_parts (
     car_id integer NOT NULL,
     maintenance_part_id integer NOT NULL,
     CONSTRAINT maintenance_part_id_fk_maintenance_parts FOREIGN KEY (maintenance_part_id) REFERENCES maintenance_parts (id),
     CONSTRAINT car_id_fk_cars FOREIGN KEY (car_id) REFERENCES cars (id)
  );

  CREATE TABLE IF NOT EXISTS parts_maintenance_parts (
       part_id integer NOT NULL,
       maintenance_part_id integer NOT NULL,
       CONSTRAINT maintenance_part_id_fk_maintenance_parts FOREIGN KEY (maintenance_part_id) REFERENCES maintenance_parts (id),
       CONSTRAINT part_id_fk_parts FOREIGN KEY (part_id) REFERENCES parts (id)
    );

COMMENT ON TABLE maintenance_parts IS 'This table provides basic information about the maintenance part types.';
COMMENT ON COLUMN maintenance_parts.id IS 'Column responsible for information about the maintenance part type ID.';
COMMENT ON COLUMN maintenance_parts.code IS 'Column responsible for information about the maintenance part type code, or external ID.';
COMMENT ON COLUMN maintenance_parts.part IS 'Column responsible for setting the maintenance part name.';
COMMENT ON COLUMN maintenance_parts.description IS 'Column responsible for setting the summary description of the maintenance part.';
COMMENT ON COLUMN maintenance_parts.serial_number IS 'Column responsible for setting the maintenance part serial number.';
COMMENT ON COLUMN maintenance_parts.manufacturer IS 'Column responsible for setting the maintenance part manufacturer.';
COMMENT ON COLUMN maintenance_parts.installation_date IS 'Column responsible for setting the maintenance part installation date.';
COMMENT ON COLUMN maintenance_parts.life_span IS 'Column responsible for setting the maintenance part lifespan.';
COMMENT ON COLUMN maintenance_parts.cost IS 'Column responsible for setting the maintenance part cost.';
COMMENT ON COLUMN maintenance_parts.status IS 'Column responsible for setting the maintenance part status.';
COMMENT ON COLUMN maintenance_parts.type IS 'Column responsible for setting the maintenance part type.';
COMMENT ON CONSTRAINT pk_maintenance_parts ON maintenance_parts IS 'Constraint responsible for guaranteeing the uniqueness of information in the primary key of the maintenance part.';

COMMENT ON TABLE types_maintenance_parts IS 'This table provides basic information about the relationship between type and maintenance part.';
COMMENT ON COLUMN types_maintenance_parts.type_id IS 'Column responsible for type identification information.';
COMMENT ON COLUMN types_maintenance_parts.maintenance_part_id IS 'Column responsible for maintenance part identification information.';
COMMENT ON CONSTRAINT type_id_fk_types ON types_maintenance_parts IS 'Constraint that refer to the foreign key relationship to types table.';
COMMENT ON CONSTRAINT maintenance_part_id_fk_maintenance_parts ON types_maintenance_parts IS 'Constraint that refer to the foreign key relationship to parts table.';

COMMENT ON TABLE cars_maintenance_parts IS 'This table provides basic information about the relationship between car and maintenance part.';
COMMENT ON COLUMN cars_maintenance_parts.car_id IS 'Column responsible for car identification information.';
COMMENT ON COLUMN cars_maintenance_parts.maintenance_part_id IS 'Column responsible for maintenance part identification information.';
COMMENT ON CONSTRAINT car_id_fk_cars ON cars_maintenance_parts IS 'Constraint that refer to the foreign key relationship to cars table.';
COMMENT ON CONSTRAINT maintenance_part_id_fk_maintenance_parts ON cars_maintenance_parts IS 'Constraint that refer to the foreign key relationship to maintenance parts table.';

COMMENT ON TABLE parts_maintenance_parts IS 'This table provides basic information about the relationship between car and maintenance part.';
COMMENT ON COLUMN parts_maintenance_parts.part_id IS 'Column responsible for part identification information.';
COMMENT ON COLUMN parts_maintenance_parts.maintenance_part_id IS 'Column responsible for maintenance part identification information.';
COMMENT ON CONSTRAINT part_id_fk_parts ON parts_maintenance_parts IS 'Constraint that refer to the foreign key relationship to parts table.';
COMMENT ON CONSTRAINT maintenance_part_id_fk_maintenance_parts ON parts_maintenance_parts IS 'Constraint that refer to the foreign key relationship to maintenance parts table.';

ALTER TABLE IF EXISTS maintenance_parts OWNER to "user-carview-api-java";
ALTER TABLE IF EXISTS types_maintenance_parts OWNER to "user-carview-api-java";
ALTER TABLE IF EXISTS cars_maintenance_parts OWNER to "user-carview-api-java";
ALTER TABLE IF EXISTS parts_maintenance_parts OWNER to "user-carview-api-java";

CREATE INDEX IF NOT EXISTS ix_maintenance_part_id
    ON maintenance_parts USING btree
        (id ASC NULLS LAST)
        TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS ix_types_maintenance_parts_id
    ON types_maintenance_parts USING btree
        (type_id ASC NULLS LAST)
        TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS ix_cars_maintenance_parts_id
    ON cars_maintenance_parts USING btree
        (car_id ASC NULLS LAST)
        TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS ix_parts_maintenance_parts_id
    ON parts_maintenance_parts USING btree
        (part_id ASC NULLS LAST)
        TABLESPACE pg_default;

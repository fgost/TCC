--liquibase formatted sql

--changeset fghost:5
CREATE TABLE IF NOT EXISTS types (
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    code character varying(36) not null,
    type_name smallint NOT NULL,

    CONSTRAINT pk_types PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS categories_types (
    category_id integer NOT NULL,
    type_id integer NOT NULL,
    CONSTRAINT category_id_fk_categories FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT type_id_fk_types FOREIGN KEY (type_id) REFERENCES types (id)
 );

COMMENT ON TABLE types IS 'This table provide basic informations about the types.';
COMMENT ON COLUMN types.id IS 'Column responsible about informations about type`s ID.';
COMMENT ON COLUMN types.code IS 'Column responsible about informations about type`s code, or external ID.';
COMMENT ON COLUMN types.type_name IS 'Column responsible about set the type.';
 COMMENT ON CONSTRAINT pk_types ON types IS 'Constraint responsible to guarantee the unique information on primary key of type registry.';

COMMENT ON TABLE categories_types IS 'This table provides basic information about the relationship between category and type.';
COMMENT ON COLUMN categories_types.type_id IS 'Column responsible for category identification information.';
COMMENT ON COLUMN categories_types.category_id IS 'Column responsible for type identification information.';
COMMENT ON CONSTRAINT category_id_fk_categories ON categories_types IS 'Constraint refer the foreign key relationship to categories table.';
COMMENT ON CONSTRAINT type_id_fk_types ON categories_types IS 'Constraint refer the foreign key relationship to types table.';

ALTER TABLE IF EXISTS categories OWNER to "user-carview-api-java";
ALTER TABLE IF EXISTS categories_cars OWNER to "user-carview-api-java";

CREATE INDEX IF NOT EXISTS ix_type_id
    ON types USING btree
        (id ASC NULLS LAST)
        TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS ix_categories_types_id
    ON categories_types USING btree
        (category_id ASC NULLS LAST)
        TABLESPACE pg_default;

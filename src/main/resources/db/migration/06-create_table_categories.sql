--liquibase formatted sql

--changeset fghost:5
CREATE TABLE IF NOT EXISTS categories (
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    code character varying(36) not null,
    category smallint NOT NULL,

    CONSTRAINT pk_categories PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS categories_cars (
    category_id integer NOT NULL,
    car_id integer NOT NULL,
    CONSTRAINT category_id_fk_categories FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT car_id_fk_cars FOREIGN KEY (car_id) REFERENCES cars (id)
 );

COMMENT ON TABLE categories IS 'This table provide basic informations about the categories.';
COMMENT ON COLUMN categories.id IS 'Column responsible about informations about category`s ID.';
COMMENT ON COLUMN categories.code IS 'Column responsible about informations about category`s code, or external ID.';
COMMENT ON COLUMN categories.category IS 'Column responsible about model informations of category.';
 COMMENT ON CONSTRAINT pk_categories ON categories IS 'Constraint responsible to guarantee the unike information on primary key of category registry.';

COMMENT ON TABLE categories_cars IS 'This table provides basic information about the relationship between category and car.';
COMMENT ON COLUMN categories_cars.category_id IS 'Column responsible for category identification information.';
COMMENT ON COLUMN categories_cars.car_id IS 'Column responsible for car identification information.';
COMMENT ON CONSTRAINT category_id_fk_categories ON categories_cars IS 'Constraint refer the foreign key relationship to categories table.';
COMMENT ON CONSTRAINT car_id_fk_cars ON categories_cars IS 'Constraint refer the foreign key relationship to cars table.';

ALTER TABLE IF EXISTS categories OWNER to "user-carview-api-java";
ALTER TABLE IF EXISTS categories_cars OWNER to "user-carview-api-java";

CREATE INDEX IF NOT EXISTS ix_category_id
    ON categories USING btree
        (id ASC NULLS LAST)
        TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS ix_categories_cars_id
    ON categories_cars USING btree
        (category_id ASC NULLS LAST)
        TABLESPACE pg_default;

--liquibase formatted sql

--changeset fghost:5
CREATE TABLE IF NOT EXISTS cars (
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    code character varying(36) not null,
    car_model character varying(100) NOT NULL,
    auto_maker character varying(100) NOT NULL,
    year character varying(100) NOT NULL,
    color character varying(100) NOT NULL,
    licence_plate character varying(100) NOT NULL UNIQUE,
    motor character varying(100) NOT NULL,
    type smallint NOT NULL,
    mileage double precision NOT NULL,
    usuario smallint NOT NULL,
    CONSTRAINT user_id_fk_user FOREIGN KEY (usuario) REFERENCES users (id),
    CONSTRAINT pk_cars PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS users_cars (
    user_id integer NOT NULL,
    cars_id integer NOT NULL,
    CONSTRAINT user_id_fk_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT car_id_fk_car FOREIGN KEY (cars_id) REFERENCES cars (id)
 );

 COMMENT ON TABLE cars IS 'This table provide basic informations about the cars.';
 COMMENT ON COLUMN cars.id IS 'Column responsible about informations about car`s ID.';
 COMMENT ON COLUMN cars.code IS 'Column responsible about informations about car`s code, or external ID.';
 COMMENT ON COLUMN cars.car_model IS 'Column responsible about model informations of car.';
 COMMENT ON COLUMN cars.auto_maker IS 'Column responsible about informations of maker of car.';
 COMMENT ON COLUMN cars.year IS 'Column responsible about informations of year of car.';
 COMMENT ON COLUMN cars.color IS 'Column responsible about informations of color of car.';
 COMMENT ON COLUMN cars.type IS 'Column responsible about set the type of car.';
 COMMENT ON CONSTRAINT pk_cars ON cars IS 'Constraint responsible to guarantee the unike information on primary key of car registry.';

 COMMENT ON TABLE users_cars IS 'This table provides basic information about the relationship between profiles and access group.';
 COMMENT ON COLUMN users_cars.user_id IS 'Column responsible for user identification information.';
 COMMENT ON COLUMN users_cars.cars_id IS 'Column responsible for car identification information.';
 COMMENT ON CONSTRAINT user_id_fk_user ON users_cars IS 'Constraint refer the foreign key relationship to users table.';
 COMMENT ON CONSTRAINT car_id_fk_car ON users_cars IS 'Constraint refer the foreign key relationship to cars group table.';

ALTER TABLE IF EXISTS cars OWNER to "user-carview-api-java";
ALTER TABLE IF EXISTS users_cars OWNER to "user-carview-api-java";

 CREATE INDEX IF NOT EXISTS ix_cars_id
     ON cars USING btree
     (id ASC NULLS LAST)
     TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS ix_users_cars_id
    ON users_cars USING btree
    (user_id ASC NULLS LAST)
    TABLESPACE pg_default;

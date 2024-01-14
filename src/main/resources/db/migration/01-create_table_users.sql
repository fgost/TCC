--liquibase formatted sql

--changeset fghost:1
CREATE TABLE IF NOT EXISTS users (
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    code character varying(36) NOT NULL,
    name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    email character varying(100) NOT NULL,
    password character varying(100) NOT NULL,
    last_update_mileage integer NOT NULL,
    last_ask_for_update_mileage integer NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_users PRIMARY KEY(id),
    CONSTRAINT users_uq_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS users_preferences (
    user_id integer NOT NULL,
    preference_key character varying(100) NOT NULL,
    preference_value character varying(100) NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users (id)
 );

CREATE TABLE IF NOT EXISTS user_photo (
    user_id integer NOT NULL,
    file_name character varying(100) NOT NULL,
    url character varying(200) NOT NULL,
    size INTEGER DEFAULT 0 NOT NULL,
    content_type character varying(100) NOT NULL,
    CONSTRAINT user_photo_pk_user PRIMARY KEY(user_id),
    CONSTRAINT user_photo_fk_user FOREIGN KEY (user_id) REFERENCES users (id)
 );

COMMENT ON TABLE users IS 'This table provides basic information about users.';
COMMENT ON COLUMN users.id IS 'Column responsible for user ID information.';
COMMENT ON COLUMN users.code IS 'Column responsible for information about the user code, or external ID.';
COMMENT ON COLUMN users.name IS 'Column responsible for username information.';
COMMENT ON COLUMN users.last_name IS 'Column responsible for user last name information.';
COMMENT ON COLUMN users.password IS 'Column responsible for user password information.';
COMMENT ON CONSTRAINT pk_users ON users IS 'Constraint responsible for guaranteeing unique information in the primary key of the user registration.';
COMMENT ON CONSTRAINT users_uq_email ON users IS 'Constraint responsible for ensuring unique information in the user`s email registration.';

COMMENT ON TABLE users_preferences IS 'This table provides basic information about user preferences.';
COMMENT ON COLUMN users_preferences.user_id IS 'Column responsible for the information about the id of the users.';
COMMENT ON COLUMN users_preferences.preference_key IS 'Column responsible for user preference key information.';
COMMENT ON COLUMN users_preferences.preference_value IS 'Column responsible for user preference value information.';
COMMENT ON CONSTRAINT fk_user ON users_preferences IS 'Constraint refer the foreign key relationship to users table.';

COMMENT ON TABLE user_photo IS 'This table provides basic information about users` photo.';
COMMENT ON COLUMN user_photo.user_id IS 'Column responsible for user photo ID information.';
COMMENT ON COLUMN user_photo.file_name IS 'Column responsible for the information about the file name of the photo.';
COMMENT ON COLUMN user_photo.url IS 'Column responsible for the photo url information.';
COMMENT ON COLUMN user_photo.size IS 'Column responsible for the size information of the photos.';
COMMENT ON COLUMN user_photo.content_type IS 'Column responsible for the information of the type of content of the photos.';
COMMENT ON CONSTRAINT user_photo_pk_user ON user_photo IS 'Constraint responsible for ensuring unique information in the primary key of user photo registration.';
COMMENT ON CONSTRAINT user_photo_fk_user ON user_photo IS 'Constraint refer the foreign key relationship to users table.';

ALTER TABLE IF EXISTS users OWNER to "user-carview-api-java";
ALTER TABLE IF EXISTS users_preferences OWNER to "user-carview-api-java";
ALTER TABLE IF EXISTS user_photo OWNER to "user-carview-api-java";

CREATE INDEX IF NOT EXISTS ix_users_id
    ON users USING btree
    (id ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS ix_users_id
    ON users_preferences USING btree
    (user_id ASC NULLS LAST)
    TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS ix_users_id
    ON user_photo USING btree
    (user_id ASC NULLS LAST)
    TABLESPACE pg_default;

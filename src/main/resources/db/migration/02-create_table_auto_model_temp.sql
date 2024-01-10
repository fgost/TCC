CREATE TABLE IF NOT EXISTS auto_model_temp (
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    code character varying(36) NOT NULL,
    auto_model_temp character varying(36) NOT NULL,
    auto_maker smallint NOT NULL,
    CONSTRAINT auto_maker_id_fk_auto_maker FOREIGN KEY (auto_maker) REFERENCES auto_maker (id),
    CONSTRAINT pk_auto_model_temp PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS auto_makers_auto_model_temp (
    auto_maker_id integer NOT NULL,
    auto_model_temp_id integer NOT NULL,
    CONSTRAINT auto_maker_id_fk_auto_maker FOREIGN KEY (auto_maker_id) REFERENCES auto_maker (id),
    CONSTRAINT auto_model_temp_id_fk_auto_model_temp FOREIGN KEY (auto_model_temp_id) REFERENCES auto_model_temp (id)
 );

COMMENT ON TABLE auto_model_temp IS 'This table provides basic information about the auto makers.';
COMMENT ON COLUMN auto_model_temp.id IS 'Column responsible for information about the auto maker ID.';
COMMENT ON COLUMN auto_model_temp.code IS 'Column responsible for information about the auto maker code, or external ID.';
COMMENT ON COLUMN auto_model_temp.auto_model_temp IS 'Column responsible for setting the auto maker name.';
COMMENT ON CONSTRAINT pk_auto_model_temp ON auto_model_temp IS 'Constraint responsible for guaranteeing the uniqueness of information in the primary key of the auto maker.';

ALTER TABLE IF EXISTS auto_model_temp OWNER to "user-carview-api-java";
ALTER TABLE IF EXISTS auto_makers_auto_model_temp OWNER to "user-carview-api-java";

CREATE INDEX IF NOT EXISTS ix_auto_model_temp_id
    ON auto_model_temp USING btree
        (id ASC NULLS LAST)
        TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS ix_auto_makers_auto_model_temp_id
    ON auto_makers_auto_model_temp USING btree
    (auto_maker_id ASC NULLS LAST)
    TABLESPACE pg_default;

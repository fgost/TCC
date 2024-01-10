CREATE TABLE IF NOT EXISTS auto_model (
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 ),
    code character varying(36) NOT NULL,
    auto_model character varying(36) NOT NULL,
    auto_maker smallint NOT NULL,
    CONSTRAINT auto_maker_id_fk_auto_maker FOREIGN KEY (auto_maker) REFERENCES auto_maker (id),
    CONSTRAINT pk_auto_model PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS auto_makers_auto_models (
    auto_maker_id integer NOT NULL,
    auto_model_id integer NOT NULL,
    CONSTRAINT auto_maker_id_fk_auto_maker FOREIGN KEY (auto_maker_id) REFERENCES auto_maker (id),
    CONSTRAINT auto_model_id_fk_auto_model FOREIGN KEY (auto_model_id) REFERENCES auto_model (id)
 );

COMMENT ON TABLE auto_model IS 'This table provides basic information about the auto makers.';
COMMENT ON COLUMN auto_model.id IS 'Column responsible for information about the auto maker ID.';
COMMENT ON COLUMN auto_model.code IS 'Column responsible for information about the auto maker code, or external ID.';
COMMENT ON COLUMN auto_model.auto_model IS 'Column responsible for setting the auto maker name.';
COMMENT ON CONSTRAINT pk_auto_model ON auto_model IS 'Constraint responsible for guaranteeing the uniqueness of information in the primary key of the auto maker.';

ALTER TABLE IF EXISTS auto_model OWNER to "user-carview-api-java";
ALTER TABLE IF EXISTS auto_makers_auto_models OWNER to "user-carview-api-java";

CREATE INDEX IF NOT EXISTS ix_auto_model_id
    ON auto_model USING btree
        (id ASC NULLS LAST)
        TABLESPACE pg_default;

CREATE INDEX IF NOT EXISTS ix_auto_makers_auto_models_id
    ON auto_makers_auto_models USING btree
    (auto_maker_id ASC NULLS LAST)
    TABLESPACE pg_default;

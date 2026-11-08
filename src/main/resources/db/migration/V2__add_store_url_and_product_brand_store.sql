-- =====================================================================
-- Cesta Básica - Price Table
-- Flyway V2: fields and table defined with the usage flowchart (2026-11-04)
-- =====================================================================

ALTER TABLE store ADD COLUMN store_url text;

COMMENT ON COLUMN store.store_url IS 'Endereço do site do mercado, usado na busca no site. Ex: https://tauste.com.br/sorocaba1';

-- A survey type is identified only by its name. Fails if two types share a name: fix them first.
ALTER TABLE survey_type DROP COLUMN code;
ALTER TABLE survey_type ADD CONSTRAINT uq_survey_type_name UNIQUE (name);

CREATE TABLE product_brand_store (
    id                uuid    NOT NULL,
    product_brand_id  uuid    NOT NULL,
    store_id          uuid    NOT NULL,
    search_name       text,
    active            boolean NOT NULL DEFAULT true,

    CONSTRAINT pk_product_brand_store   PRIMARY KEY (id),
    CONSTRAINT uq_pbs_product_brand_store UNIQUE (product_brand_id, store_id),
    CONSTRAINT fk_pbs_product_brand     FOREIGN KEY (product_brand_id) REFERENCES product_brand (id),
    CONSTRAINT fk_pbs_store             FOREIGN KEY (store_id)         REFERENCES store (id),
    CONSTRAINT ck_pbs_search_name       CHECK (search_name IS NULL OR length(btrim(search_name)) > 0)
);

CREATE INDEX ix_pbs_store ON product_brand_store (store_id);

COMMENT ON TABLE  product_brand_store             IS 'Item da cesta em um mercado. Sem vínculo, o item não é pesquisado nesse mercado';
COMMENT ON COLUMN product_brand_store.search_name IS 'Como este mercado chama o item, já com a marca. Ex: Arroz Camil tipo 1 5kg. Nulo = o mercado não tem o item (a pessoa confirma "não possui") e ele não é pesquisado lá';

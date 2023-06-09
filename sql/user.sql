-- Table: public.user

-- DROP TABLE IF EXISTS public."user";

CREATE TABLE IF NOT EXISTS public."user"
(
    id_token integer NOT NULL DEFAULT nextval('user'::regclass),
    password character varying(255) COLLATE pg_catalog."default",
    registered_at timestamp without time zone,
    removed_at timestamp without time zone,
    role character varying(255) COLLATE pg_catalog."default",
    updated_at timestamp without time zone,
    user_id character varying(255) COLLATE pg_catalog."default",
    user_name character varying(255) COLLATE pg_catalog."default",
    phone_number character varying(255) COLLATE pg_catalog."default",
    birth character varying(255) COLLATE pg_catalog."default",
    gender character varying(255) COLLATE pg_catalog."default",
    address character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT user_pkey PRIMARY KEY (id_token),
    CONSTRAINT uk_lqjrcobrh9jc8wpcar64q1bfh UNIQUE (user_name)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public."user"
    OWNER to sohthnduwbipay;
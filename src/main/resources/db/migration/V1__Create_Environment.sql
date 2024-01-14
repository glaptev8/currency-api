create table currencies
(
    id          bigserial
        primary key,
    created_at  timestamp default now() not null,
    modified_at timestamp,
    code        varchar(3)              not null
        constraint code_unique
        unique,
    iso_code    integer                 not null
        constraint iso_code_unique
        unique,
    description varchar(64)             not null,
    active      boolean   default true,
    scale       integer                 not null,
    symbol      varchar(2)
);

create index currencies_active_uidx
    on currencies (active);

create table rate_providers
(
    provider_code      varchar(3)              not null
        primary key,
    created_at         timestamp default now() not null,
    modified_at        timestamp,
    provider_name      varchar(28)             not null
        unique,
    description        varchar(255),
    priority           integer                 not null,
    active             boolean   default true,
    default_multiplier numeric   default 1.0   not null
);


create index rate_providers_code_uidx
    on rate_providers (provider_code);

create table conversion_rates
(
    id               bigserial
        primary key,
    created_at       timestamp default now() not null,
    modified_at      timestamp,
    source_code      varchar(3)              not null
        references currencies (code),
    destination_code varchar(3)              not null
        references currencies (code),
    rate_begin_time  timestamp default now() not null,
    rate_end_time    timestamp               not null,
    rate             numeric                 not null,
    provider_code    varchar(3)
        references rate_providers,
    multiplier       numeric                 not null,
    system_rate      numeric                 not null
);

create table rate_correction_coefficients
(
    id               bigserial
        primary key,
    created_at       timestamp default now() not null,
    modified_at      timestamp,
    archived         boolean   default false,
    source_code      varchar                 not null,
    destination_code varchar                 not null,
    multiplier       numeric                 not null,
    provider_code    varchar(3)
        references rate_providers,
    creator          varchar(255),
    modifier         varchar(255),
    date_from        date,
    date_to          date,
    profile_type     varchar(50)
);

create table shedlock
(
    name       varchar(64)  not null
        primary key,
    lock_until timestamp    not null,
    locked_at  timestamp    not null,
    locked_by  varchar(255) not null
);

INSERT INTO currencies (created_at, modified_at, code, iso_code, description, active, scale, symbol) VALUES ('2024-01-13 22:34:32.875865', null, 'EUR', 978, 'dasdas', true, 1, null);
INSERT INTO currencies (created_at, modified_at, code, iso_code, description, active, scale, symbol) VALUES ('2024-01-13 22:34:32.875865', null, 'USD', 3166, 'SADSAD', true, 1, null);
INSERT INTO currencies (created_at, modified_at, code, iso_code, description, active, scale, symbol) VALUES ('2024-01-13 22:34:32.875865', null, 'RUB', 643, 'dsad', true, 1, null);

INSERT INTO rate_providers (provider_code, created_at, modified_at, provider_name, description, priority, active, default_multiplier) VALUES ('QWE', '2024-01-13 22:35:56.272852', null, 'CURRENCY_API', 'sadsad', 1, true, 1);

INSERT INTO rate_correction_coefficients (created_at, modified_at, archived, source_code, destination_code, multiplier, provider_code, creator, modifier, date_from, date_to, profile_type) VALUES ('2024-01-13 22:36:44.945657', null, false, 'RUB', 'USD', 1, 'QWE', null, null, null, null, null);
INSERT INTO rate_correction_coefficients (created_at, modified_at, archived, source_code, destination_code, multiplier, provider_code, creator, modifier, date_from, date_to, profile_type) VALUES ('2024-01-13 23:25:41.495615', null, false, 'USD', 'RUB', 1, 'QWE', null, null, null, null, null);
INSERT INTO rate_correction_coefficients (created_at, modified_at, archived, source_code, destination_code, multiplier, provider_code, creator, modifier, date_from, date_to, profile_type) VALUES ('2024-01-13 23:25:41.495615', null, false, 'RUB', 'EUR', 1, 'QWE', null, null, null, null, null);
INSERT INTO rate_correction_coefficients (created_at, modified_at, archived, source_code, destination_code, multiplier, provider_code, creator, modifier, date_from, date_to, profile_type) VALUES ('2024-01-13 23:25:41.495615', null, false, 'EUR', 'RUB', 1, 'QWE', null, null, null, null, null);
INSERT INTO rate_correction_coefficients (created_at, modified_at, archived, source_code, destination_code, multiplier, provider_code, creator, modifier, date_from, date_to, profile_type) VALUES ('2024-01-13 23:25:41.495615', null, false, 'USD', 'EUR', 1, 'QWE', null, null, null, null, null);
INSERT INTO rate_correction_coefficients (created_at, modified_at, archived, source_code, destination_code, multiplier, provider_code, creator, modifier, date_from, date_to, profile_type) VALUES ('2024-01-13 23:25:41.495615', null, false, 'EUR', 'USD', 1, 'QWE', null, null, null, null, null);



-- 加入者情報
CREATE SEQUENCE IF NOT EXISTS t_member_seq AS BIGINT START WITH 1 INCREMENT BY 1 NO CYCLE NO CACHE; 

CREATE TABLE IF NOT EXISTS T_MEMBER (
    member_id       BIGINT, 
    mail            VARCHAR(255) NOT NULL,
    name            VARCHAR(31) NOT NULL,
    address         VARCHAR(127) NOT NULL,
    start_date      DATE NOT NULL,
    end_date        DATE,
    payment_method  TINYINT NOT NULL,   
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (member_id)
);

-- 料金情報
CREATE SEQUENCE IF NOT EXISTS t_charge_seq AS BIGINT START WITH 1 INCREMENT BY 1 NO CYCLE NO CACHE; 

CREATE TABLE IF NOT EXISTS T_CHARGE (
    charge_id       BIGINT, 
    name            VARCHAR(127) NOT NULL,
    amount          NUMERIC(9,0) NOT nULL,
    start_date      DATE NOT NULL,
    end_date        DATE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (charge_id)
);

-- 請求データ情報
CREATE TABLE IF NOT EXISTS T_BILLING_STATUS (
    billing_ym      DATE PRIMARY KEY,
    is_commit       BOOLEAN NOT NULL DEFAULT FALSE
);

-- 請求データ
CREATE TABLE IF NOT EXISTS T_BILLING_DATA (
    billing_ym      DATE NOT NULL,
    member_id       BIGINT NOT NULL,
    mail            VARCHAR(255) NOT nULL,
    name            VARCHAR(31) NOT NULL,
    address         VARCHAR(127) NOT nULL,
    start_date      DATE NOT NULL,
    end_date        DATE,    
    payment_method  INTEGER NOT NULL,
    amount          NUMERIC(10,0) NOT NULL,
    tax_ratio       NUMERIC(5,2) NOT NULL,
    total           NUMERIC(10,0) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (billing_ym, member_id),
    FOREIGN KEY (billing_ym) 
        REFERENCES T_BILLING_STATUS(billing_ym)
);

-- 請求詳細データ
CREATE TABLE IF NOT EXISTS T_BILLING_DETAIL_DATA (
    billing_ym      DATE NOT NULL,
    member_id       BIGINT NOT NULL,
    charge_id       BIGINT NOT NULL,
    name            VARCHAR(127) NOT NULL,
    amount          NUMERIC(9,0) NOT NULL,
    start_date      DATE NOT NULL,
    end_date        DATE,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (billing_ym, member_id, charge_id),
    FOREIGN KEY (billing_ym, member_id) 
        REFERENCES T_BILLING_DATA(billing_ym, member_id)
);
CREATE TABLE IF NOT EXISTS T_BILLING_STATUS (
    billing_ym DATE PRIMARY KEY,
    is_commit BOOLEAN NOT NULL DEFAULT FALSE
);


CREATE TABLE IF NOT EXISTS T_BILLING_DATA (
    billing_ym DATE NOT NULL,
    member_id INTEGER NOT NULL,
    mail VARCHAR(256) NOT nULL,
    name VARCHAR(32) NOT NULL,
    address VARCHAR(128) NOT nULL,
    start_date DATE NOT NULL,
    end_date DATE,    
    payment_method INTEGER NOT NULL,
    amount NUMBER(10,0) NOT NULL,
    tax_ratio NUMBER(5,2) NOT NULL,
    total NUMBER(10,0) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (billing_ym, member_id),
    FOREIGN KEY (billing_ym) 
        REFERENCES T_BILLING_STATUS(billing_ym)
);


CREATE TABLE IF NOT EXISTS T_BILLING_DETAIL_DATA (
    billing_ym DATE NOT NULL,
    member_id INTEGER NOT NULL,
    charge_id INTEGER NOT NULL,
    name VARCHAR(64) NOT NULL,
    amount NUMBER(10,0) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (billing_ym, member_id, charge_id),
    FOREIGN KEY (billing_ym, member_id) 
        REFERENCES T_BILLING_DATA(billing_ym, member_id)
);
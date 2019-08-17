CREATE TABLE listings (
    code VARCHAR NOT NULL,
    make VARCHAR NOT NULL,
    model VARCHAR NOT NULL,
    power INTEGER,
    kw INTEGER,
    year INTEGER NOT NULL,
    color VARCHAR NOT NULL,
    price INTEGER NOT NULL,
    dealer_id VARCHAR NOT NULL,
    PRIMARY KEY(code, dealer_id)
);
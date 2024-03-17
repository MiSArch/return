CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE OrderEntity (
    id UUID PRIMARY KEY UNIQUE,
    userId UUID NOT NULL
);

CREATE TABLE ReturnEntity (
    id UUID PRIMARY KEY UNIQUE DEFAULT uuid_generate_v4(),
    reason VARCHAR(255) NOT NULL,
    orderId UUID NOT NULL,
    refundedAmount BIGINT NOT NULL,
    createdAt TIMESTAMPTZ NOT NULL,
    FOREIGN KEY (orderId) REFERENCES OrderEntity(id)
);

CREATE TABLE OrderItemEntity (
    id UUID PRIMARY KEY UNIQUE,
    returnedWithId UUID NULL,
    sentWithId UUID NULL,
    orderId UUID NOT NULL,
    compensatableAmount BIGINT NULL,
    productVariantVersionId UUID NULL,
    FOREIGN KEY (orderId) REFERENCES OrderEntity(id)
);

CREATE TABLE ProductVariantVersion (
    id UUID PRIMARY KEY UNIQUE,
    canBeReturnedForDays INT NULL
);

CREATE TABLE ShipmentEntity (
    id UUID PRIMARY KEY UNIQUE,
    deliveredAt TIMESTAMPTZ NULL
);
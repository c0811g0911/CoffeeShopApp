-- liquibase formatted sql

-- changeset c0811:1710401324816-1
CREATE SEQUENCE IF NOT EXISTS primary_sequence START WITH 10000 INCREMENT BY 1;

-- changeset c0811:1710401324816-2
CREATE TABLE IF NOT EXISTS customers
(
    id         BIGINT       NOT NULL,
    name       VARCHAR(255) NOT NULL,
    address    TEXT         NOT NULL,
    phone      VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    user_name  VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_customers PRIMARY KEY (id)
);

-- changeset c0811:1710401324816-3
CREATE TABLE IF NOT EXISTS menu_items
(
    id         BIGINT         NOT NULL,
    name       VARCHAR(255)   NOT NULL,
    price      DECIMAL(10, 2) NOT NULL,
    currency   VARCHAR(255)   NOT NULL,
    menu_id    BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_menu_items PRIMARY KEY (id)
);

-- changeset c0811:1710401324816-4
CREATE TABLE IF NOT EXISTS menus
(
    id         BIGINT NOT NULL,
    status     VARCHAR(255),
    shop_id    BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_menus PRIMARY KEY (id)
);

-- changeset c0811:1710401324816-5
CREATE TABLE IF NOT EXISTS orders
(
    id           BIGINT       NOT NULL,
    quantity     INTEGER      NOT NULL,
    status       VARCHAR(255) NOT NULL,
    menu_item_id BIGINT,
    customer_id  BIGINT,
    queue_id     BIGINT,
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    updated_at   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

-- changeset c0811:1710401324816-6
CREATE TABLE IF NOT EXISTS queues
(
    id             BIGINT       NOT NULL,
    name           VARCHAR(255) NOT NULL,
    max_queue_size INTEGER      NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE,
    updated_at     TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_queues PRIMARY KEY (id)
);

-- changeset c0811:1710401324816-7
CREATE TABLE IF NOT EXISTS shops
(
    id          BIGINT           NOT NULL,
    name        VARCHAR(255),
    description TEXT,
    address     TEXT,
    phone       VARCHAR(255),
    user_name   VARCHAR(255)     NOT NULL,
    password    VARCHAR(255)     NOT NULL,
    longitude   DOUBLE PRECISION NOT NULL,
    latitude    DOUBLE PRECISION NOT NULL,
    queue_id    BIGINT,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    open_time   VARCHAR(255)     NOT NULL,
    closed_time VARCHAR(255)     NOT NULL,
    CONSTRAINT pk_shops PRIMARY KEY (id)
);

-- changeset c0811:1710401324816-8
ALTER TABLE menus DROP CONSTRAINT IF EXISTS uc_menus_shop;
ALTER TABLE menus
    ADD CONSTRAINT uc_menus_shop UNIQUE (shop_id);

-- changeset c0811:1710401324816-9
ALTER TABLE shops DROP CONSTRAINT IF EXISTS uc_shops_queue;
ALTER TABLE shops
    ADD CONSTRAINT uc_shops_queue UNIQUE (queue_id);

-- changeset c0811:1710401324816-10
ALTER TABLE shops DROP CONSTRAINT IF EXISTS uc_shops_user_name;
ALTER TABLE shops
    ADD CONSTRAINT uc_shops_user_name UNIQUE (user_name);

-- changeset c0811:1710401324816-11
ALTER TABLE menus DROP CONSTRAINT IF EXISTS FK_MENUS_ON_SHOP;
ALTER TABLE menus
    ADD CONSTRAINT FK_MENUS_ON_SHOP FOREIGN KEY (shop_id) REFERENCES shops (id);

-- changeset c0811:1710401324816-12
ALTER TABLE menu_items DROP CONSTRAINT IF EXISTS FK_MENU_ITEMS_ON_MENU;
ALTER TABLE menu_items
    ADD CONSTRAINT FK_MENU_ITEMS_ON_MENU FOREIGN KEY (menu_id) REFERENCES menus (id);

-- changeset c0811:1710401324816-13
ALTER TABLE orders DROP CONSTRAINT IF EXISTS FK_ORDERS_ON_CUSTOMER;
ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customers (id);

-- changeset c0811:1710401324816-14
ALTER TABLE orders DROP CONSTRAINT IF EXISTS FK_ORDERS_ON_MENU_ITEM;
ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_MENU_ITEM FOREIGN KEY (menu_item_id) REFERENCES menu_items (id);

-- changeset c0811:1710401324816-15
ALTER TABLE orders DROP CONSTRAINT IF EXISTS FK_ORDERS_ON_QUEUE;
ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_QUEUE FOREIGN KEY (queue_id) REFERENCES queues (id);

-- changeset c0811:1710401324816-16
ALTER TABLE shops DROP CONSTRAINT IF EXISTS FK_SHOPS_ON_QUEUE;
ALTER TABLE shops
    ADD CONSTRAINT FK_SHOPS_ON_QUEUE FOREIGN KEY (queue_id) REFERENCES queues (id);



INSERT INTO customers(id,"name",address,phone,email,user_name, password, created_at, updated_at)  VALUES
                                                                          (1,'ERIC', 'Hanoi VietNam', '90090900','eric@gmail.com', 'eric', '12345678', NOW(), NOW()),
                                                                          (2,'ERIC1', 'Hanoi VietNam', '90090900','eric1@gmail.com', 'eric1', '12345678', NOW(), NOW()),
                                                                          (3,'ERIC2', 'Hanoi VietNam', '90090900','eric2@gmail.com', 'eric2', '12345678', NOW(), NOW());



INSERT INTO shops(id, "name",description,address,phone,user_name, password,longitude,latitude, open_time, closed_time, created_at, updated_at)  VALUES
                                                                                                                             (1,'SHOP_ERIC','Eric shop', 'Hanoi VietNam', '90090900', 'eric', '12345678',111.11,222.22, '08:00','22:00', NOW(), NOW()),
                                                                                                                             (2,'SHOP_ERIC1','Eric shop1', 'Hanoi VietNam', '90090900', 'eric1', '12345678', 332.22, 212.11,'08:00','22:00', NOW(), NOW());


INSERT INTO menus(id,status,shop_id, created_at, updated_at)  VALUES
                                            (1, 'ACTIVE',1, NOW(), NOW()),
                                            (2, 'ACTIVE',2, NOW(), NOW());


INSERT INTO menu_items(id,"name",price,currency, menu_id, created_at, updated_at)  VALUES
                                                                 (1, 'Black Coffee',1, 'USD', 1, NOW(), NOW()),
                                                                 (2, 'Milk Coffee',1, 'USD', 1, NOW(), NOW()),
                                                                 (3, 'Orange Juice',1, 'USD', 1, NOW(), NOW()),
                                                                 (4, 'Black Coffee',1, 'USD', 2, NOW(), NOW()),
                                                                 (5, 'Milk Coffee',1, 'USD', 2, NOW(), NOW()),
                                                                 (6, 'Orange Juice',1, 'USD', 2, NOW(), NOW());







CREATE TABLE IF NOT EXISTS product_category (
    category_id int auto_increment not null primary key,
    name        varchar(100), 
    enabled     boolean
);

CREATE TABLE IF NOT EXISTS product(
    product_id  int auto_increment not null primary key,
    category_id int,
    name        varchar(100),
    sale_price  decimal(10,2)
);
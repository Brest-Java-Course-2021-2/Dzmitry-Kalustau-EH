DROP TABLE IF EXISTS category;

DROP TABLE IF EXISTS expense;

CREATE TABLE category
(
  category_id INT NOT NULL AUTO_INCREMENT,
  category_name VARCHAR(20) UNIQUE,
  CONSTRAINT category_pk PRIMARY KEY (category_id)
);

CREATE TABLE expense
(
  expense_id INT NOT NULL AUTO_INCREMENT,
  date DATE NOT NULL,
  category_id INT NOT NULL,
  price DECIMAL NOT NULL,
    CONSTRAINT expense_pk PRIMARY KEY (expense_id),
    CONSTRAINT expense_category_fk FOREIGN KEY (category_id) REFERENCES category (category_id)
);
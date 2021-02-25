connect 'jdbc:derby://localhost:1527/tienditaLibrosOnlineBD;user=app;password=app';

DROP TABLE customer_order;
DROP TABLE product;
DROP TABLE category;
DROP TABLE customer;

------------------------------- 
--     CUSTOMER
-------------------------------
CREATE  TABLE  customer 
(
  id INT  NOT NULL GENERATED ALWAYS AS IDENTITY 
     (START WITH 1 ,INCREMENT BY 1) 
     CONSTRAINT CUSTOMER_PK PRIMARY KEY,
  name VARCHAR(45) NOT NULL ,
  email VARCHAR(45) NOT NULL ,
  phone VARCHAR(45) NOT NULL ,
  address VARCHAR(45) NOT NULL ,
  credit DECIMAL(6,2) NOT NULL,
  city_region VARCHAR(2) NOT NULL ,
  cc_number VARCHAR(19) NOT NULL
  );

INSERT INTO customer (name,email,phone,address,credit,city_region,cc_number) VALUES 
('Arlet Diaz','arlet@gmail.com','5512345678','Rio Hondo 1', 50.6, '1','123'),
('Damian Perez','damo@gmail.com','5512345678','Rio Hondo 2', 30.6, '1','123'),
('Ruben Romero','rubs@gmail.com','5512345678','Rio Hondo 3', 20.6, '1','123'),
('Juan Carlos Sigler','juano@gmail.com','5512345678','Rio Hondo 4', 20.6, '1','123'),
('Alexis','alexis@gmail.com','5512345678','Rio Hondo 5', 10.6, '1','123'),
('Ivana','ivana@gmail.com','5512345678','Rio Hondo 6', 50.6, '1','123');


------------------------------- 
--     CATEGORY
-------------------------------
CREATE  TABLE  category 
(
  id INT  NOT NULL GENERATED ALWAYS AS IDENTITY 
     (START WITH 1 ,INCREMENT BY 1) 
     CONSTRAINT CATEGORY_PK PRIMARY KEY,
  name VARCHAR(45) NOT NULL 
);

INSERT INTO category (name) VALUES 
('scifi'),
('love'),
('mistery'),
('fantasy');

------------------------------- 
--     PRODUCTS
-------------------------------

CREATE  TABLE  product 
(
  id BIGINT  NOT NULL GENERATED ALWAYS AS IDENTITY 
     (START WITH 8507864512910 ,INCREMENT BY 1), 
  name VARCHAR(45) NOT NULL ,
  author VARCHAR(45) NOT NULL,
  price DECIMAL(5,2) NOT NULL ,
  description VARCHAR(50),
  last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  category_id INT  NOT NULL ,
  stock INT NOT NULL,
  reserved INT NOT NULL,
  PRIMARY KEY (id) ,
  CONSTRAINT fk_product_category
    FOREIGN KEY (category_id )
    REFERENCES category (id )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

INSERT INTO product (name,author,price,description,last_update,category_id,stock, reserved) VALUES
('Harry Potter 4', 'J. K. Rowling' , 11.70, 'El Caliz de Fuego',TIMESTAMP('2020-10-06 09:00:00'), 4, 9,0),
('Harry Potter 3', 'J. K. Rowling' , 12.39, 'El Prisionero de Azkaban',TIMESTAMP('2020-10-06 09:00:00'), 4, 7,0),
('Harry Potter 2', 'J. K. Rowling' , 11.09, 'La Camara de los Secretos', TIMESTAMP('2020-10-06 09:00:00'),4, 8,0),
('Harry Potter', 'J. K. Rowling' , 11.76, 'La Piedra Filosofal', TIMESTAMP('2020-10-06 09:00:00'),4, 11,0),
('Eso', 'Stephen King' , 2.29, 'El payaso', TIMESTAMP('2020-10-06 09:00:00'),3, 8,0),
('Inferno', 'Dan Brown' , 13.49, 'La tercera parte de la saga', TIMESTAMP('2020-10-06 09:00:00'),3,20,0),
('El Código Da Vinci', 'Dan Brown' , 12.59, 'La primera parte de la saga', TIMESTAMP('2020-10-06 09:00:00'),3,15,0),
('Angeles y Demonios', 'Dan Brown' , 13.55, 'La segunda parte de la saga', TIMESTAMP('2020-10-06 09:00:00'),3,17,0),
('Fahrenheit 451', 'Ray Bradbury', 11.89, '1953', TIMESTAMP('2020-10-06 09:00:00'),1,5,0),
('El juego de Ender', 'Orson Scott' , 11.19, 'Pelicula inspirada en el libro', TIMESTAMP('2020-10-06 09:00:00'),1,30,0),
('Cronicas marcianas', 'Ray Bradbury' , 11.19, '1950', TIMESTAMP('2020-10-06 09:00:00'),1,2,0),
('Un Mundo Feliz', 'Aldous Huxley' , 19.15, '1932', TIMESTAMP('2020-10-06 09:00:00'),1,1,0),
('Ana Karenina', 'Leon Tolstoi', 12.39, '1877', TIMESTAMP('2020-10-06 09:00:00'),2,8,0),
('Bajo la misma estrella', 'John Green', 21.49, '2012', TIMESTAMP('2020-10-06 09:00:00'),2,40,0);

------------------------------- 
--     CUSTOMER_ORDER
-------------------------------
CREATE  TABLE  customer_order 
(
  id INT  NOT NULL GENERATED ALWAYS AS IDENTITY 
     (START WITH 1 ,INCREMENT BY 1) 
     CONSTRAINT CUSTOMER_ORDER_PK PRIMARY KEY,
  amount DECIMAL(6,2) NOT NULL ,
  date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  quantity SMALLINT  NOT NULL DEFAULT 1,
  delivery_system VARCHAR(20),
  delivery_date TIMESTAMP DEFAULT NULL,
  status VARCHAR(10) NOT NULL DEFAULT '',
  product_id BIGINT  NOT NULL,
  customer_id INT  NOT NULL
  CONSTRAINT CUST_ORD_FK REFERENCES customer,

  CONSTRAINT fk_product
    FOREIGN KEY (product_id)
    REFERENCES product (id )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);


disconnect;

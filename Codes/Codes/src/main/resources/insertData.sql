-- Active: 1729766811477@@localhost@3306@moravianWine
insert into region(name,country)
values('Kyjovsko','Česká republika');


insert into winery(name, address, website, phone)
values ('Vino Vylet','Orechovka, Mistrin 69604','vinovylet.cz','448559887');

INSERT INTO wine (name, type, year, price, description, stock_quantity,winery_id , region_id)
VALUES
  ('Merlot Classic', '1', 2023, 250.00, 'Hladký a bohatý Merlot s chutěmi třešní a čokolády.', 50, 1, 1),
  ('Chardonnay Reserve', '2', 2023, 220.00, 'Plné tělo Chardonnay s tóny vanilky a másla.', 30, 1, 1),
  ('Rosé Delight', '3', 2023, 180.00, 'Osvěžující Rosé s náznaky jahod a malin.', 20, 1, 1),
  ('Cabernet Sauvignon', '1', 2023, 270.00, 'Silný Cabernet s tmavým ovocem a dlouhým dozvukem.', 40, 1, 1),
  ('Sauvignon Blanc', '2', 2023, 200.00, 'Křupavé Sauvignon Blanc s citrusovými a jablečnými tóny.', 25, 1, 1),
  ('Pinot Noir', '1', 2023, 260.00, 'Elegantní Pinot Noir s červeným ovocem a zemitými podtóny.', 15, 1, 1),
  ('Riesling', '2', 2023, 210.00, 'Sladký Riesling s květinovými aromaty a chutí peckového ovoce.', 35, 1, 1),
  ('Zinfandel', '1', 2023, 290.00, 'Bohatý Zinfandel s džemovitým ovocem a kořeněnými tóny.', 45, 1, 1),
  ('Prosecco Sparkling', '3', 2023, 150.00, 'Šumivé Prosecco s náznaky jablka a hrušky.', 60, 1, 1),
  ('Syrah', '1', 2023, 275.00, 'Plné tělo Syrah s chutí ostružin a pepře.', 55, 1, 1);



INSERT into cart(totalprice) values (0);

insert into wineInCart(cart_id, wine_id, quantity)
VALUE (1,1,21),
    (1,2,10),
    (1,6,5);

INSERT INTO users (user_id, username,firstName,lastName, email, password_hash,role) VALUES
                                                           (1, 'user1','Martin','Rihak' ,'user1@example.com', 'password1','1'),
                                                           (2, 'user2', 'Ondrej','Vyler','user2@example.com', 'password2','0'),
                                                           (3, 'user3','Martin' ,'Mechura','user3@example.com', 'password3','2');
insert into review(rating, comment,user_id, wine_id) VALUE (5,'Nic moc',2,7);


insert into orders(0,'P',1,);

ALTER TABLE wine AUTO_INCREMENT = 11;
insert into cart(totalprice) values (0);

insert into orders(total_price, status, user_id, cart_id)
values (0,'F',1,2);


INSERT INTO item_in_order(quantity, price_per_unit, order_id, wine_id)
values (0,120,2,1),
       (0,150,2,3),
       (0,120,2,6);
-- Class Table Inheritance
CREATE TABLE sys_admins(
    user_id INT NOT NULL auto_increment,
    privileges VARCHAR(255), -- specifické pro administrátory, např. seznam práv
    oddeleni VARCHAR(50),
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
-- Concrete Table Inheritance
CREATE TABLE reg_users (
   user_id INT NOT NULL auto_increment,
   loyalty_points INT DEFAULT 0, -- např. věrnostní body
   PRIMARY KEY (user_id),
   FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
DROP TABLE reg_users;
DROP TABLE sys_admins;
INSERT INTO reg_users(user_id)
value ('1');

SELECT u.*,r.loyalty_points
from users u
join reg_users r on u.user_id = r.user_id
where u.user_id = 1

CREATE TABLE favorite_wines (
                                user_id INT NOT NULL,
                                wine_id INT NOT NULL,
                                added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                PRIMARY KEY (user_id, wine_id),
                                FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                                FOREIGN KEY (wine_id) REFERENCES wine(wine_id) ON DELETE CASCADE
);
/*
CREATE TABLE sys_admins_concrete (
    user_id INT AUTO_INCREMENT NOT NULL,
    username VARCHAR(50) NOT NULL,
    firstName VARCHAR(30) NOT NULL,
    lastName VARCHAR(30) NOT NULL,
    email VARCHAR(70) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role CHAR DEFAULT 'A' NOT NULL CHECK (role = 'A'),
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    address VARCHAR(70),
    country VARCHAR(30),
    privileges VARCHAR(255),
    PRIMARY KEY (user_id)
);
CREATE TABLE reg_users (
       user_id INT AUTO_INCREMENT NOT NULL,
       username VARCHAR(50) NOT NULL,
       firstName VARCHAR(30) NOT NULL,
       lastName VARCHAR(30) NOT NULL,
       email VARCHAR(70) NOT NULL,
       password_hash VARCHAR(255) NOT NULL,
       role CHAR DEFAULT 'U' NOT NULL CHECK (role = 'U'),
       create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
       address VARCHAR(70),
       country VARCHAR(30),
       loyalty_points INT DEFAULT 0,
       preferences JSON,
       PRIMARY KEY (user_id)
);
*/
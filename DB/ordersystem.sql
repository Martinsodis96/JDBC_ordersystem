DROP DATABASE IF EXISTS ordersystem;
CREATE DATABASE ordersystem;

USE ordersystem;

CREATE TABLE issue (
	id INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(45) NOT NULL,
	description VARCHAR(255) DEFAULT NULL,
    is_active bit(1) DEFAULT b'1',
    
    PRIMARY KEY( id )
);

-- Mock Data
insert into issue (title, description, is_active) values ('Violet', 'Phasellus sit amet erat. Nulla tempus. Vivamus in felis eu sapien cursus vestibulum.', 1);
insert into issue (title, description, is_active) values ('Teal', 'Curabitur at ipsum ac tellus semper interdum. Mauris ullamcorper purus sit amet nulla. Quisque arcu libero, rutrum ac, lobortis vel, dapibus at, diam.', 1);
insert into issue (title, description, is_active) values ('Aquamarine', 'Aliquam quis turpis eget elit sodales scelerisque. Mauris sit amet eros. Suspendisse accumsan tortor quis turpis.', 1);
insert into issue (title, description, is_active) values ('Red', 'Phasellus in felis. Donec semper sapien a libero. Nam dui.', 1);
insert into issue (title, description, is_active) values ('Purple', 'Phasellus in felis. Donec semper sapien a libero. Nam dui.', 1);
insert into issue (title, description, is_active) values ('Puce', 'Nulla ut erat id mauris vulputate elementum. Nullam varius. Nulla facilisi.', 1);
insert into issue (title, description, is_active) values ('Indigo', 'Phasellus sit amet erat. Nulla tempus. Vivamus in felis eu sapien cursus vestibulum.', 1);
insert into issue (title, description, is_active) values ('Fuscia', 'Cras non velit nec nisi vulputate nonummy. Maecenas tincidunt lacus at velit. Vivamus vel nulla eget eros elementum pellentesque.', 1);

CREATE TABLE team (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(45) UNIQUE NOT NULL,
    is_active bit(1) DEFAULT b'1',
    
    PRIMARY KEY (id)
);

-- Mock Data
insert into team (name, is_active) values ('Schiller and Sons', 1);
insert into team (name, is_active) values ('Toy, Hermiston and Hamill', 1);
insert into team (name, is_active) values ('Kshlerin-Farrell', 1);
insert into team (name, is_active) values ('Heathcote-Stamm', 1);
insert into team (name, is_active) values ('Mayert Inc', 1);
insert into team (name, is_active) values ('Pagac-Mosciski', 1);
insert into team (name, is_active) values ('Blanda, Raynor and Kertzmann', 1);
insert into team (name, is_active) values ('Williamson-Johnston', 1);

CREATE TABLE user (
	id INT NOT NULL AUTO_INCREMENT,
	username VARCHAR(45) UNIQUE DEFAULT NULL,
    firstname VARCHAR(45) DEFAULT NULL,
    lastname VARCHAR(45) DEFAULT NULL,
    team_id INT DEFAULT NULL,
    is_active bit(1) DEFAULT b'1',
    
    FOREIGN KEY( team_id ) REFERENCES team( id ),

    PRIMARY KEY (id)
);

-- Mock Data
insert into user (firstname, lastname, username, team_id, is_active) values ('Betty', 'Lopez', 'blopez0', 6, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Nicole', 'Ramirez', 'nramirez1', 4, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Juan', 'Scott', 'jscott2', 2, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Sandra', 'Bishop', 'sbishop3', 7, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Teresa', 'Wells', 'twells4', 5, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Bruce', 'Powell', 'bpowell5', 5, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Jonathan', 'Romero', 'jromero6', 2, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Linda', 'Fernandez', 'lfernandez7', 5, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Michelle', 'Matthews', 'mmatthews8', 3, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Pamela', 'Evans', 'pevans9', 3, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Barbara', 'Myers', 'bmyersa', 7, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Harry', 'Peters', 'hpetersb', 5, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Bruce', 'Dean', 'bdeanc', 6, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Howard', 'Wheeler', 'hwheelerd', 7, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Eric', 'Payne', 'epaynee', 8, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Samuel', 'Graham', 'sgrahamf', 1, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Billy', 'George', 'bgeorgeg', 1, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Julie', 'Smith', 'jsmithh', 5, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Ralph', 'Graham', 'rgrahami', 2, 1);
insert into user (firstname, lastname, username, team_id, is_active) values ('Ashley', 'Gonzales', 'agonzalesj', 5, 1);

CREATE TABLE workitem (
	id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(45) UNIQUE DEFAULT NULL,
    status enum('UNSTARTED','STARTED','DONE') DEFAULT 'UNSTARTED',
    issue_id INT DEFAULT NULL,
    user_id INT DEFAULT NULL,
    is_active bit(1) DEFAULT b'1',
    
    FOREIGN KEY( issue_id ) REFERENCES issue( id ),
    FOREIGN KEY( user_id ) REFERENCES user( id ),
    
    PRIMARY KEY( id )
);

-- Mock Data
insert into workitem (name, status, issue_id, user_id, is_active) values ('Aerified', 'DONE', 1, 16, 1);
insert into workitem (name, status, issue_id, user_id, is_active) values ('Regrant', 'DONE', 4, 9, 1);
insert into workitem (name, status, issue_id, user_id, is_active) values ('Greenlam', 'DONE', 7, 10, 1);
insert into workitem (name, status, issue_id, user_id, is_active) values ('Transcof', 'UNSTARTED', 8, 16, 1);
insert into workitem (name, status, issue_id, user_id, is_active) values ('Otcom', 'DONE', 6, 7, 1);
insert into workitem (name, status, issue_id, user_id, is_active) values ('Keylex', 'DONE', 1, 2, 1);
insert into workitem (name, status, issue_id, user_id, is_active) values ('Solarbreeze', 'UNSTARTED', 8, 14, 1);
insert into workitem (name, status, issue_id, user_id, is_active) values ('Sonsing', 'UNSTARTED', 1, 15, 1);
insert into workitem (name, status, issue_id, user_id, is_active) values ('Veribet', 'STARTED', 7, 3, 1);
insert into workitem (name, status, issue_id, user_id, is_active) values ('Lotstring', 'STARTED', 8, 18, 1);
insert into workitem (name, status, issue_id, user_id, is_active) values ('Andalax', 'STARTED', 3, 12, 1);
insert into workitem (name, status, issue_id, user_id, is_active) values ('It', 'UNSTARTED', 5, 8, 1);
insert into workitem (name, status, issue_id, user_id, is_active) values ('Zoolab', 'DONE', 2, 10, 1);
insert into workitem (name, status, issue_id, user_id, is_active) values ('Fix San', 'STARTED', 3, 3, 1);
insert into workitem (name, status, issue_id, user_id, is_active) values ('Cookley', 'STARTED', 6, 19, 1);
insert into workitem (name, status, issue_id, user_id, is_active) values ('Trippledex', 'STARTED', 5, 15, 1);
insert into workitem (name, status, issue_id, user_id, is_active) values ('Quo Lux', 'STARTED', 1, 13, 1);
insert into workitem (name, status, issue_id, user_id, is_active) values ('Zathin', 'STARTED', 4, 14, 1);


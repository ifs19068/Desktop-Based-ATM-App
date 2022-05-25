CREATE DATABASE db_atm;

USE db_atm;

CREATE TABLE atm_user(
	username nvarchar(45) not null unique,
	fullname nvarchar(45)  default 'Unkown',
	accountNo nvarchar(45) not null unique,
	passwd nvarchar(45) not null,
	balance bigint not null default 0
);

INSERT INTO atm_user(username,fullname,accountNo,passwd) 
VALUES 
('alfrendo','Afrendo','DEL132','silalahi'),
('elisa','Elisa','DEL234','tinambunan'),
('rewina','Rewina','DEL345','pakpahan'),
('tasya','Tasya','DEL456','gurning'),
('jhonatan','Jhonatan','DEL567','sitorus');




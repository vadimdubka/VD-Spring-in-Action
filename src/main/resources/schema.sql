drop table if exists Spittle;
drop table if exists Spitter;

create table Spitter (
	id identity,
	username varchar(20) unique not null,
	password varchar(20) not null,
	first_name varchar(30),
	last_name varchar(30),
	fullName varchar(100),
	email varchar(30) not null,
	updateByEmail boolean
);

create table Spittle (
	id integer identity primary key,
	message varchar(140) not null,
	created_at timestamp,
	latitude double,
	longitude double,
	spitter integer,
	postedTime datetime,
  foreign key (spitter) references spitter(id)
);
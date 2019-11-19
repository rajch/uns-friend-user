drop database if exists `userdb`;
create database `userdb`;
use userdb;
create table `user`(
`username` varchar(100) NOT NULL,
`userid` varchar(100) NOT NULL,
`emailid` varchar(100) NOT NULL,
`password` varchar(100) NOT NULL,
PRIMARY KEY(`userid`)
);

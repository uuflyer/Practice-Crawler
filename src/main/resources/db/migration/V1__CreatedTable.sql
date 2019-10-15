create table NEWS
(
id bigint primary key auto_increment,
title text ,
url varchar (2000),
content text ,
created_at timestamp ,
update_at timestamp
);

create table LINK_TO_BE_PROCESSED(link varchar (2000));
create table LINK_ALREADY_PROCESSED(link varchar (2000));

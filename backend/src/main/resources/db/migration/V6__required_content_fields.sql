update products set status = 'DRAFT' where status is null;
update products set sort_order = 0 where sort_order is null;
update news_articles set status = 'DRAFT' where status is null;
update slides set status = 'ACTIVE' where status is null;
update slides set sort_order = 0 where sort_order is null;
update bookings set status = 'NEW' where status is null;

alter table products
  modify status varchar(32) not null default 'DRAFT',
  modify sort_order int not null default 0;

alter table news_articles modify status varchar(32) not null default 'DRAFT';

alter table slides
  modify image_url varchar(500) not null,
  modify status varchar(32) not null default 'ACTIVE',
  modify sort_order int not null default 0;

alter table bookings modify status varchar(32) not null default 'NEW';

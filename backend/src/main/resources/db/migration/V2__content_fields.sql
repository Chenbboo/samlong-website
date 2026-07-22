alter table products
  add column badge varchar(64) null after image_url,
  add column range_text varchar(64) null after badge,
  add column power_text varchar(64) null after range_text,
  add column speed_text varchar(64) null after power_text;

alter table news_articles add column category varchar(64) null after image_url;
alter table slides add column eyebrow varchar(255) null after subtitle;

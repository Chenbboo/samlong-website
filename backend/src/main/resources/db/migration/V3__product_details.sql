alter table products
  add column highlights text null after content,
  add column battery_text varchar(100) null after speed_text,
  add column charge_time_text varchar(100) null after battery_text,
  add column warranty_text varchar(100) null after charge_time_text,
  add column weight_text varchar(100) null after warranty_text,
  add column featured bit not null default b'0' after weight_text;

create table product_images(
  product_id bigint not null,
  sort_order int not null,
  image_url varchar(500) not null,
  primary key(product_id,sort_order),
  constraint fk_product_images_product foreign key(product_id) references products(id) on delete cascade
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_unicode_ci;

create table product_colors(
  product_id bigint not null,
  sort_order int not null,
  color_name varchar(100),
  hex_code varchar(32),
  primary key(product_id,sort_order),
  constraint fk_product_colors_product foreign key(product_id) references products(id) on delete cascade
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_unicode_ci;

update products set featured=b'1' where sort_order<2;
update products set battery_text='Lithium-ion',charge_time_text='4–6 giờ',warranty_text='3 năm',weight_text='110 kg',highlights='Thiết kế thể thao hiện đại\nVận hành êm ái, tiết kiệm năng lượng\nPin thông minh và an toàn' where id>0;
insert into product_images(product_id,sort_order,image_url) select id,0,image_url from products where image_url is not null and image_url<>'';
insert into product_colors(product_id,sort_order,color_name,hex_code) select id,0,'Đỏ','#d61f2c' from products;
insert into product_colors(product_id,sort_order,color_name,hex_code) select id,1,'Đen','#111111' from products;
insert into product_colors(product_id,sort_order,color_name,hex_code) select id,2,'Trắng','#f2f2f2' from products;

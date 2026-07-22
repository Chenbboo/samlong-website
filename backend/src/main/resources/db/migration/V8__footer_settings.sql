create table footer_settings (
  id bigint not null primary key,
  company_name varchar(255) not null,
  tax_code varchar(64),
  address varchar(500),
  hotline varchar(64),
  email varchar(255),
  copyright_text varchar(255) not null
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_unicode_ci;

create table footer_links (
  id bigint not null auto_increment primary key,
  footer_id bigint not null,
  section varchar(32) not null,
  label varchar(128) not null,
  url varchar(500) not null,
  icon varchar(32),
  sort_order int not null default 0,
  constraint fk_footer_links_footer foreign key (footer_id) references footer_settings(id) on delete cascade,
  index idx_footer_links_section_sort (footer_id, section, sort_order)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_unicode_ci;

insert into footer_settings(id,company_name,tax_code,address,hotline,email,copyright_text) values
(1,'Công ty CP Xe điện SAM Việt Nam','0110234567','593 Nguyễn Duy Trinh, Bình Trưng, Hồ Chí Minh','1800 1234','support@samlong.vn','© 2026 SAM.LOONG. Bảo lưu mọi quyền.');

insert into footer_links(footer_id,section,label,url,icon,sort_order) values
(1,'PRODUCTS','F25 Thể thao','/products/1',null,0),
(1,'PRODUCTS','DanDan','/products/2',null,1),
(1,'ABOUT','Câu chuyện','/#top',null,0),
(1,'ABOUT','Công nghệ','/#technology',null,1),
(1,'ABOUT','Tuyển dụng','/news',null,2),
(1,'ABOUT','Đối tác','/#support',null,3),
(1,'SUPPORT','Chính sách bảo hành','/#support',null,0),
(1,'SUPPORT','Dịch vụ sửa chữa','/#support',null,1),
(1,'SUPPORT','Hướng dẫn sử dụng','/#support',null,2),
(1,'SUPPORT','FAQ','/#support',null,3),
(1,'SUPPORT','Liên hệ','/#booking',null,4),
(1,'SOCIAL','Facebook','#','facebook',0),
(1,'SOCIAL','YouTube','#','youtube',1),
(1,'SOCIAL','Instagram','#','instagram',2);

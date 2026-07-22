create table audit_logs(
  id bigint not null auto_increment primary key,
  username varchar(64) not null,
  action varchar(32) not null,
  resource_type varchar(32) not null,
  resource_id bigint null,
  summary varchar(500) null,
  created_at datetime not null default current_timestamp,
  index idx_audit_created_at(created_at),
  index idx_audit_resource(resource_type,resource_id)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_unicode_ci;

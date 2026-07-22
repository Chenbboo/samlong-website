alter table bookings
  add column notes text null after status,
  add column follow_up text null after notes,
  add column unread bit not null default b'1' after follow_up,
  add column updated_at datetime not null default current_timestamp after created_at,
  add index idx_bookings_status(status),
  add index idx_bookings_phone_created(phone,created_at),
  add index idx_bookings_city(city);

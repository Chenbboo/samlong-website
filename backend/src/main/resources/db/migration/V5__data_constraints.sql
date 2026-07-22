update news_articles set published_at = current_timestamp where published_at is null;
alter table news_articles modify published_at datetime not null;

alter table products
  add constraint chk_products_price check (price is null or price >= 0),
  add constraint chk_products_sort_order check (sort_order is null or sort_order >= 0),
  add constraint chk_products_status check (status in ('ACTIVE', 'DRAFT'));

alter table news_articles add constraint chk_news_status check (status in ('ACTIVE', 'DRAFT'));

alter table slides
  add constraint chk_slides_sort_order check (sort_order is null or sort_order >= 0),
  add constraint chk_slides_status check (status in ('ACTIVE', 'DRAFT'));

alter table bookings
  add constraint chk_bookings_status check (status in ('NEW', 'CONTACTED', 'VISITED', 'COMPLETED', 'CANCELLED'));

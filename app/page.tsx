import { HeroCarousel } from "./HeroCarousel";

const products = [
  { name: "F25 SPORT", range: "120 km", power: "1.500 W", speed: "50 km/h" },
  { name: "F25 URBAN", range: "110 km", power: "1.500 W", speed: "48 km/h" },
  { name: "F25 PRO", range: "135 km", power: "1.800 W", speed: "55 km/h" },
];

const news = [
  ["SẢN PHẨM", "12/07/2026", "Xe điện F25 Sport ra mắt — làn sóng mới trong phân khúc xe điện thể thao Việt Nam"],
  ["CÔNG NGHỆ", "08/07/2026", "Pin lithium thế hệ mới — sạc 80% chỉ trong 45 phút, tầm xa 180km"],
  ["CỘNG ĐỒNG", "03/07/2026", "SAMLOONG đồng hành cùng 500.000 khách hàng — hành trình xanh tiếp tục"],
  ["KHUYẾN MÃI", "28/06/2026", "Ưu đãi lên tới 5 triệu đồng — chương trình tri ân khách hàng tháng 7"],
];

function Logo() {
  return <a className="logo" href="#top" aria-label="Sam.Loong homepage"><span>S</span><b>Sam.Loong</b></a>;
}

function ProductCard({ product, index }: { product: typeof products[number]; index: number }) {
  return (
    <article className="product-card">
      <div className={`product-photo crop-${index}`}><span>MỚI</span></div>
      <div className="product-body">
        <h3>XE MÁY ĐIỆN {product.name}</h3>
        <strong>18.500.000 đ</strong>
        <div className="specs"><span>{product.range}</span><span>{product.power}</span><span>{product.speed}</span></div>
        <a href="#booking">Xem chi tiết</a>
      </div>
    </article>
  );
}

export default function Home() {
  return (
    <main id="top">
      <header className="site-header">
        <Logo />
        <nav aria-label="Main navigation">
          <a href="#models">Xe máy điện <small>⌄</small></a>
          <a href="#technology">Công nghệ</a>
          <a href="#support">Hỗ trợ &amp; Bảo hành</a>
          <a href="#news">Tin tức</a>
          <a href="#booking">Cửa hàng</a>
        </nav>
        <a className="test-drive" href="#booking">ĐĂNG KÝ LÁI THỬ</a>
        <details className="mobile-menu"><summary aria-label="Open menu">☰</summary><div>
          <a href="#models">Xe máy điện</a><a href="#technology">Công nghệ</a><a href="#news">Tin tức</a><a href="#booking">Đăng ký lái thử</a>
        </div></details>
      </header>

      <HeroCarousel />

      <section className="featured section" id="models">
        <div className="section-kicker">RA MẮT 2026</div>
        <h2>SẢN PHẨM NỔI BẬT</h2>
        <a className="see-all" href="#all-models">Xem tất cả&nbsp; ›</a>
        <div className="feature-grid">
          <article className="feature-card sport"><span>SẢN PHẨM MỚI</span><div><h3>F25 SPORT</h3><p>Từ <b>42.900.000 đ</b></p><a href="#booking">TÌM HIỂU THÊM</a></div></article>
          <article className="feature-card dandan"><span>BÁN CHẠY</span><div><h3>XE ĐIỆN DANDAN</h3><p>Từ <b>42.900.000 đ</b></p><a href="#booking">TÌM HIỂU THÊM</a></div></article>
        </div>
      </section>

      <section className="catalog section" id="all-models">
        <h2>XE MÁY ĐIỆN</h2>
        <div className="product-grid">{products.map((p, i) => <ProductCard key={p.name} product={p} index={i} />)}</div>
        <h2 className="second-title">XE GẮN MÁY ĐIỆN</h2>
        <div className="product-grid">{products.map((p, i) => <ProductCard key={`m-${p.name}`} product={{...p, name: i === 0 ? "M25 CITY" : i === 1 ? "M25 PLUS" : "M25 MAX"}} index={i} />)}</div>
      </section>

      <section className="technology" id="technology">
        <div><span>CÔNG NGHỆ SAM.LOONG</span><h2>NĂNG LƯỢNG MỚI.<br/>CHUYỂN ĐỘNG MỚI.</h2><p>Hệ truyền động điện hiệu suất cao, vận hành êm ái và hệ thống pin thông minh cho mọi hành trình đô thị.</p><a href="#booking">KHÁM PHÁ CÔNG NGHỆ</a></div>
      </section>

      <section className="news section" id="news">
        <h2>TIN TỨC</h2>
        <div className="news-grid">{news.map((item, i) => <article key={item[2]}><div className={`news-photo news-${i}`} /><div className="news-copy"><p><span>{item[0]}</span> {item[1]}</p><h3>{item[2]}</h3></div></article>)}</div>
      </section>

      <section className="booking" id="booking">
        <div className="booking-copy"><span>HOÀN TOÀN MIỄN PHÍ</span><h2>ĐẶT LỊCH TRẢI NGHIỆM</h2><p>Trải nghiệm tại showroom gần nhất. Đội ngũ tư vấn sẽ liên hệ xác nhận lịch hẹn của bạn.</p></div>
        <div className="booking-grid">
          <form action="#thanks">
            <label>HỌ VÀ TÊN *<input name="name" required placeholder="Nguyễn Văn A" /></label>
            <label>SỐ ĐIỆN THOẠI *<input name="phone" required inputMode="tel" placeholder="0912 345 678" /></label>
            <label>TỈNH/THÀNH PHỐ *<select name="city" required defaultValue=""><option value="" disabled>Chọn tỉnh/thành phố</option><option>Hà Nội</option><option>Đà Nẵng</option><option>TP. Hồ Chí Minh</option></select></label>
            <label>QUẬN/HUYỆN *<select name="district" required defaultValue=""><option value="" disabled>Chọn quận/huyện</option><option>Trung tâm</option><option>Khu vực lân cận</option></select></label>
            <button type="submit">ĐĂNG KÝ NGAY</button><p id="thanks">Cảm ơn bạn! Chúng tôi sẽ sớm liên hệ.</p>
          </form>
          <div className="booking-photo"><div><b>SẴN SÀNG<br/>LĂN BÁNH?</b><span>Trải nghiệm Sam.Loong hôm nay.</span></div></div>
        </div>
      </section>

      <footer id="support"><Logo /><p>Chuyển động xanh cho tương lai Việt Nam.</p><div><a href="#models">Sản phẩm</a><a href="#technology">Công nghệ</a><a href="#news">Tin tức</a><a href="#booking">Liên hệ</a></div><small>© 2026 Sam.Loong. All rights reserved.</small></footer>
    </main>
  );
}

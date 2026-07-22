package com.samlong.config;

import com.samlong.model.AdminUser;
import com.samlong.model.NewsArticle;
import com.samlong.model.Product;
import com.samlong.model.ProductColor;
import com.samlong.model.Slide;
import com.samlong.repository.AdminUserRepository;
import com.samlong.repository.NewsRepository;
import com.samlong.repository.ProductRepository;
import com.samlong.repository.SlideRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
  private final AdminUserRepository users; private final ProductRepository products; private final NewsRepository news; private final SlideRepository slides; private final PasswordEncoder encoder;
  @Value("${app.admin.username:admin}") private String username;
  @Value("${app.admin.password:ChangeMe123!}") private String password;
  public DataInitializer(AdminUserRepository users, ProductRepository products, NewsRepository news, SlideRepository slides, PasswordEncoder encoder){this.users=users;this.products=products;this.news=news;this.slides=slides;this.encoder=encoder;}

  public void run(String... args) {
    seedAdmin(); seedSlides(); seedProducts(); seedNews();
  }

  private void seedAdmin(){AdminUser u=users.findByUsername(username).orElseGet(AdminUser::new);u.setUsername(username);if(u.getPassword()==null||!encoder.matches(password,u.getPassword()))u.setPassword(encoder.encode(password));u.setRole("ADMIN");u.setEnabled(true);users.save(u);}

  private void seedSlides(){
    if(slides.count()!=0)return;
    slides.save(slide("TƯƠNG LAI","DI CHUYỂN","RA MẮT 2026","/images/campaign/hero-green-v1.png",0));
    slides.save(slide("BỨT PHÁ","GIỚI HẠN","SAM.LOONG F25 SPORT","/images/campaign/hero-red-v1.png",1));
  }
  private Slide slide(String title,String subtitle,String eyebrow,String image,int order){Slide v=new Slide();v.setTitle(title);v.setSubtitle(subtitle);v.setEyebrow(eyebrow);v.setImageUrl(image);v.setLinkUrl("#models");v.setStatus("ACTIVE");v.setSortOrder(order);return v;}

  private void seedProducts(){
    if(products.count()!=0)return;
    products.save(product("F25 SPORT","120 km","1.500 W","50 km/h",new BigDecimal("18500000"),0));
    products.save(product("F25 URBAN","110 km","1.500 W","48 km/h",new BigDecimal("18500000"),1));
    products.save(product("F25 PRO","135 km","1.800 W","55 km/h",new BigDecimal("18500000"),2));
  }
  private Product product(String title,String range,String power,String speed,BigDecimal price,int order){Product v=new Product();v.setTitle(title);v.setContent("Xe máy điện đô thị Sam.Loong, kết hợp thiết kế hiện đại với khả năng vận hành êm ái và tiết kiệm năng lượng.");v.setHighlights("Thiết kế thể thao hiện đại\nVận hành êm ái, tiết kiệm năng lượng\nPin thông minh và an toàn");v.setPrice(price);v.setImageUrl("/uploads/samlong-grid.png");v.getImageUrls().add("/uploads/samlong-grid.png");v.setBadge("MỚI");v.setRangeText(range);v.setPowerText(power);v.setSpeedText(speed);v.setBatteryText("Lithium-ion");v.setChargeTimeText("4–6 giờ");v.setWarrantyText("3 năm");v.setWeightText("110 kg");v.setFeatured(order<2);v.getColors().add(new ProductColor("Đỏ","#d61f2c"));v.getColors().add(new ProductColor("Đen","#111111"));v.getColors().add(new ProductColor("Trắng","#f2f2f2"));v.setStatus("ACTIVE");v.setSortOrder(order);return v;}

  private void seedNews(){
    if(news.count()!=0)return;
    news.save(article("Xe điện F25 Sport ra mắt – làn sóng mới trong phân khúc xe điện thể thao Việt Nam","SẢN PHẨM",LocalDateTime.of(2026,7,12,9,0)));
    news.save(article("Pin lithium thế hệ mới – sạc 80% chỉ trong 45 phút, tầm xa 180km","CÔNG NGHỆ",LocalDateTime.of(2026,7,8,9,0)));
    news.save(article("SAMLOONG đồng hành cùng 500.000 khách hàng – hành trình xanh tiếp tục","CỘNG ĐỒNG",LocalDateTime.of(2026,7,3,9,0)));
    news.save(article("Ưu đãi lên tới 5 triệu đồng – chương trình tri ân khách hàng tháng 7","KHUYẾN MÃI",LocalDateTime.of(2026,6,28,9,0)));
  }
  private NewsArticle article(String title,String category,LocalDateTime publishedAt){NewsArticle v=new NewsArticle();v.setTitle(title);v.setContent(title);v.setCategory(category);v.setImageUrl("/uploads/samlong-news.png");v.setStatus("ACTIVE");v.setPublishedAt(publishedAt);return v;}
}

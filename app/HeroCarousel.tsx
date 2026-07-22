"use client";

import { useCallback, useEffect, useState } from "react";

const slides = [
  {
    image: "/samlong-hero.png",
    className: "slide-launch",
    eyebrow: "RA MẮT 2026",
    title: <>TƯƠNG LAI<br /><em>DI CHUYỂN</em></>,
    label: "F25 Sport launch",
  },
  {
    image: "/og.png",
    className: "slide-performance",
    eyebrow: "SAM.LOONG F25 SPORT",
    title: <>BỨT PHÁ<br /><em>GIỚI HẠN</em></>,
    label: "F25 Sport performance",
  },
  {
    image: "/samlong-products.png",
    className: "slide-city",
    eyebrow: "NĂNG LƯỢNG XANH",
    title: <>CHẤT RIÊNG<br /><em>ĐÔ THỊ</em></>,
    label: "Sam.Loong urban electric scooters",
  },
];

export function HeroCarousel() {
  const [active, setActive] = useState(0);
  const [paused, setPaused] = useState(false);

  const changeSlide = useCallback((direction: number) => {
    setActive((current) => (current + direction + slides.length) % slides.length);
  }, []);

  useEffect(() => {
    if (paused) return;
    const timer = window.setInterval(() => changeSlide(1), 5500);
    return () => window.clearInterval(timer);
  }, [changeSlide, paused]);

  return (
    <section
      className="hero"
      aria-roledescription="carousel"
      aria-label="Sam.Loong featured vehicles"
      onMouseEnter={() => setPaused(true)}
      onMouseLeave={() => setPaused(false)}
      onFocus={() => setPaused(true)}
      onBlur={(event) => {
        if (!event.currentTarget.contains(event.relatedTarget)) setPaused(false);
      }}
      onKeyDown={(event) => {
        if (event.key === "ArrowLeft") changeSlide(-1);
        if (event.key === "ArrowRight") changeSlide(1);
      }}
    >
      <div className="hero-track" style={{ transform: `translateX(-${active * 100}%)` }}>
        {slides.map((slide, index) => (
          <article
            className={`hero-slide ${slide.className}`}
            key={slide.label}
            aria-hidden={index !== active}
            aria-label={`${index + 1} of ${slides.length}: ${slide.label}`}
          >
            <div className="hero-media" style={{ backgroundImage: `url('${slide.image}')` }} />
            <div className="hero-shade" />
            <div className="hero-copy">
              <span>{slide.eyebrow}</span>
              <h1>{slide.title}</h1>
              <a href="#models" tabIndex={index === active ? 0 : -1}>KHÁM PHÁ NGAY</a>
            </div>
          </article>
        ))}
      </div>
      <button className="hero-arrow left" onClick={() => changeSlide(-1)} aria-label="Previous slide">‹</button>
      <button className="hero-arrow right" onClick={() => changeSlide(1)} aria-label="Next slide">›</button>
      <div className="hero-dots" aria-label={`Slide ${active + 1} of ${slides.length}`}>
        {slides.map((slide, index) => (
          <button
            key={slide.label}
            className={index === active ? "active" : ""}
            onClick={() => setActive(index)}
            aria-label={`Show slide ${index + 1}`}
            aria-current={index === active ? "true" : undefined}
          />
        ))}
      </div>
    </section>
  );
}

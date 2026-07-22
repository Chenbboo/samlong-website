package com.samlong.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;

@Configuration
public class UploadWebConfig implements WebMvcConfigurer {
  @Value("${app.upload.dir:uploads}") private String uploadDir;

  @Override public void addResourceHandlers(ResourceHandlerRegistry registry) {
    File directory = new File(uploadDir).getAbsoluteFile();
    if (!directory.exists()) directory.mkdirs();
    registry.addResourceHandler("/uploads/**").addResourceLocations(directory.toURI().toString());
  }
}

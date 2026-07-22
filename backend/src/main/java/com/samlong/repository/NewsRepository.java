package com.samlong.repository;
import com.samlong.model.NewsArticle;import org.springframework.data.jpa.repository.JpaRepository;import org.springframework.data.jpa.repository.JpaSpecificationExecutor;import java.util.List;
public interface NewsRepository extends JpaRepository<NewsArticle,Long>,JpaSpecificationExecutor<NewsArticle>{List<NewsArticle> findByStatusOrderByPublishedAtDescIdDesc(String status);}

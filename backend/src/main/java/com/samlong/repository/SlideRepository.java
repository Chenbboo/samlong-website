package com.samlong.repository;
import com.samlong.model.Slide;import org.springframework.data.jpa.repository.JpaRepository;import org.springframework.data.jpa.repository.JpaSpecificationExecutor;import java.util.List;
public interface SlideRepository extends JpaRepository<Slide,Long>,JpaSpecificationExecutor<Slide>{List<Slide> findByStatusOrderBySortOrderAscIdAsc(String status);}

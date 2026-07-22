package com.samlong.repository;
import com.samlong.model.Product;import org.springframework.data.jpa.repository.JpaRepository;import org.springframework.data.jpa.repository.JpaSpecificationExecutor;import java.util.List;
public interface ProductRepository extends JpaRepository<Product,Long>,JpaSpecificationExecutor<Product>{List<Product> findByStatusOrderBySortOrderAscIdAsc(String status);}

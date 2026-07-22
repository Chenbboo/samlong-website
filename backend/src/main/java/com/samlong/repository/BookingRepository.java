package com.samlong.repository;
import com.samlong.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;
public interface BookingRepository extends JpaRepository<Booking,Long>,JpaSpecificationExecutor<Booking>{
  boolean existsByPhoneAndCreatedAtAfter(String phone,LocalDateTime after);
  long countByStatus(String status);
  long countByCreatedAtGreaterThanEqual(LocalDateTime start);
  List<Booking> findByCreatedAtGreaterThanEqualOrderByCreatedAtAsc(LocalDateTime start);
  List<Booking> findTop5ByStatusOrderByCreatedAtDesc(String status);
  @Query("select coalesce(b.city,'未填写'),count(b) from Booking b group by b.city order by count(b) desc")
  List<Object[]> countBookingsByCity();
}

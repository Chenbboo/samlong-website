package com.samlong.controller;

import com.samlong.model.Booking;
import com.samlong.repository.BookingRepository;
import com.samlong.service.AuditLogService;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController @RequestMapping("/api/bookings")
public class BookingController {
  private final BookingRepository repo;
  private final AuditLogService audit;
  public BookingController(BookingRepository r,AuditLogService a){repo=r;audit=a;}

  @GetMapping public Object all(@RequestParam(required=false) Integer page,@RequestParam(defaultValue="10") Integer size,@RequestParam(required=false) String keyword,@RequestParam(required=false) String status,@RequestParam(required=false) String city,@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate from,@RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate to){
    Specification<Booking> spec=(root,query,cb)->{List<Predicate> predicates=new ArrayList<Predicate>();if(keyword!=null&&!keyword.trim().isEmpty()){String value="%"+keyword.trim().toLowerCase()+"%";predicates.add(cb.or(cb.like(cb.lower(root.<String>get("name")),value),cb.like(cb.lower(root.<String>get("phone")),value)));}if(status!=null&&!status.isEmpty())predicates.add(cb.equal(root.get("status"),status));if(city!=null&&!city.isEmpty())predicates.add(cb.equal(root.get("city"),city));if(from!=null)predicates.add(cb.greaterThanOrEqualTo(root.<LocalDateTime>get("createdAt"),from.atStartOfDay()));if(to!=null)predicates.add(cb.lessThan(root.<LocalDateTime>get("createdAt"),to.plusDays(1).atStartOfDay()));return cb.and(predicates.toArray(new Predicate[0]));};
    Sort sort=Sort.by(Sort.Direction.DESC,"createdAt");if(page==null)return repo.findAll(spec,sort);return repo.findAll(spec,PageRequest.of(Math.max(0,page),Math.min(100,Math.max(1,size==null?10:size)),sort));
  }

  @PostMapping public Booking create(@Valid @RequestBody Booking v){String phone=normalizePhone(v.getPhone());if(repo.existsByPhoneAndCreatedAtAfter(phone,LocalDateTime.now().minusHours(24)))throw new ResponseStatusException(HttpStatus.CONFLICT,"A booking with this phone number was already submitted recently");v.setId(null);v.setPhone(phone);v.setStatus("NEW");v.setNotes(null);v.setFollowUp(null);v.setUnread(true);v.setCreatedAt(LocalDateTime.now());v.setUpdatedAt(LocalDateTime.now());return repo.save(v);}
  @PutMapping("/{id}") public Booking update(@PathVariable Long id,@Valid @RequestBody Booking v){Booking existing=repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));v.setId(id);v.setPhone(normalizePhone(v.getPhone()));v.setCreatedAt(existing.getCreatedAt());v.setUpdatedAt(LocalDateTime.now());if(v.getUnread()==null)v.setUnread(false);Booking saved=repo.save(v);audit.record("UPDATE","BOOKING",saved.getId(),saved.getName()+" / "+saved.getPhone());return saved;}
  @DeleteMapping("/{id}") public void delete(@PathVariable Long id){Booking v=repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));repo.delete(v);audit.record("DELETE","BOOKING",id,v.getName()+" / "+v.getPhone());}
  private String normalizePhone(String value){return value==null?"":value.replaceAll("[\\s.-]","");}
}

package com.samlong.controller;

import com.samlong.model.AuditLog;
import com.samlong.repository.AuditLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {
  private final AuditLogRepository repository;
  public AuditLogController(AuditLogRepository repository){this.repository=repository;}

  @GetMapping
  public Page<AuditLog> all(@RequestParam(defaultValue="0") Integer page,@RequestParam(defaultValue="20") Integer size,
                            @RequestParam(required=false) String keyword,@RequestParam(required=false) String action,
                            @RequestParam(required=false) String resourceType){
    Specification<AuditLog> spec=(root,query,cb)->{
      List<Predicate> items=new ArrayList<Predicate>();
      if(hasText(keyword)){String value="%"+keyword.trim().toLowerCase()+"%";items.add(cb.or(cb.like(cb.lower(root.<String>get("username")),value),cb.like(cb.lower(root.<String>get("summary")),value)));}
      if(hasText(action))items.add(cb.equal(root.get("action"),action));
      if(hasText(resourceType))items.add(cb.equal(root.get("resourceType"),resourceType));
      return cb.and(items.toArray(new Predicate[0]));
    };
    return repository.findAll(spec,PageRequest.of(Math.max(0,page),Math.min(100,Math.max(1,size)),Sort.by(Sort.Direction.DESC,"createdAt")));
  }
  private boolean hasText(String value){return value!=null&&!value.trim().isEmpty();}
}

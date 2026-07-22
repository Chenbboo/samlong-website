package com.samlong.service;

import com.samlong.model.AuditLog;
import com.samlong.repository.AuditLogRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {
  private final AuditLogRepository repository;
  public AuditLogService(AuditLogRepository repository){this.repository=repository;}

  public void record(String action,String resourceType,Long resourceId,String summary){
    Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
    AuditLog log=new AuditLog();
    log.setUsername(authentication==null?"unknown":authentication.getName());
    log.setAction(action);log.setResourceType(resourceType);log.setResourceId(resourceId);
    log.setSummary(summary==null?"":summary.substring(0,Math.min(summary.length(),500)));
    repository.save(log);
  }
}

package com.samlong.controller;

import com.samlong.model.FooterLink;
import com.samlong.model.FooterSettings;
import com.samlong.repository.FooterSettingsRepository;
import com.samlong.service.AuditLogService;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/footer")
public class FooterController {
  private final FooterSettingsRepository repository;
  private final AuditLogService audit;
  public FooterController(FooterSettingsRepository repository, AuditLogService audit){this.repository=repository;this.audit=audit;}

  @GetMapping
  @Transactional(readOnly = true)
  public FooterSettings get(){return repository.findById(1L).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));}

  @PutMapping
  @Transactional
  public FooterSettings update(@Valid @RequestBody FooterSettings input){
    FooterSettings current=repository.findById(1L).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    current.setCompanyName(input.getCompanyName());current.setTaxCode(input.getTaxCode());current.setAddress(input.getAddress());
    current.setHotline(input.getHotline());current.setEmail(input.getEmail());current.setCopyrightText(input.getCopyrightText());
    List<FooterLink> replacements=new ArrayList<FooterLink>();
    if(input.getLinks()!=null){for(FooterLink link:input.getLinks()){link.setId(null);link.setFooter(current);replacements.add(link);}}
    current.getLinks().clear();current.getLinks().addAll(replacements);
    FooterSettings saved=repository.save(current);audit.record("UPDATE","FOOTER",1L,"Footer settings");return saved;
  }
}

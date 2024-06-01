package com.verygoodbank.tes.web.controller;


import com.verygoodbank.tes.service.TradeDataFileEnricher;
import java.io.BufferedInputStream;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1")
public class TradeEnrichmentController {

  private final TradeDataFileEnricher fileEnricher;

  public TradeEnrichmentController(TradeDataFileEnricher fileEnricher) {
    this.fileEnricher = fileEnricher;
  }

  @RequestMapping(value = "/enrich", method = RequestMethod.POST)
  public ResponseEntity<Resource> uploadFile(@RequestParam("file") MultipartFile file) {
    return ResponseEntity.ok()
        .contentType(new MediaType("text","csv"))
        .body(new InputStreamResource(fileEnricher.enrich(file)));
  }
}



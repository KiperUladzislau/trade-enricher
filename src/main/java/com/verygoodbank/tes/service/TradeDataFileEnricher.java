package com.verygoodbank.tes.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TradeDataFileEnricher {

  private final ProductService productService;

  private static final Logger logger = LoggerFactory.getLogger(TradeDataFileEnricher.class);

  public TradeDataFileEnricher(ProductService productService) {
    this.productService = productService;
  }

  public InputStream enrich(MultipartFile csvFile) {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(csvFile.getInputStream()))) {
      // skip first line
      String line = br.readLine();
      line = line.replace("product_id", "product_name");
      StringBuilder builder = new StringBuilder();
      builder.append(line);
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(",");
        String date = parts[0];
        if (!isValidDate(date)) {
          logger.error("Invalid date: {}", date);
          continue;
        }
        builder.append("\r\n");
        builder.append(date).append(",");
        String productId = parts[1];
        String productName = productService.getProductName(productId).orElse("Missing Product Name");
        builder.append(productName).append(",");
        builder.append(parts[2]).append(",");
        builder.append(parts[3]);
      }
      return new ByteArrayInputStream(builder.toString().getBytes());
    } catch (Exception any) {
      logger.error("Error occurred during file processing", any);
      throw new RuntimeException(any);
    }
  }

  private boolean isValidDate(String date) {
    return GenericValidator.isDate(date, "yyyyMMdd", true);
  }
}

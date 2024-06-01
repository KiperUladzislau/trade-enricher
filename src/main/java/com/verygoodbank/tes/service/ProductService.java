package com.verygoodbank.tes.service;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private static final String FILE_NAME = "./src/main/resources/product.csv";
  private final Map<String, String> productIdToName = new HashMap<>();

  @PostConstruct
  public void init() throws Exception {
    try(BufferedReader bis = new BufferedReader(new FileReader(FILE_NAME))) {
      // skip first line with labels
      String line = bis.readLine();
      while ((line = bis.readLine()) != null) {
        String[] parts = line.split(",");
        productIdToName.put(parts[0], parts[1]);
      }
    }
  }

  public Optional<String> getProductName(String productId) {
    return Optional.ofNullable(productIdToName.get(productId));
  }
}

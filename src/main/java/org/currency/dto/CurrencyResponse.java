package org.currency.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyResponse {
  private Meta meta;
  private Map<String, CurrencyData> data;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Meta {
    private LocalDateTime lastUpdatedAt;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CurrencyData {
    private String code;
    private BigDecimal value;
  }
}
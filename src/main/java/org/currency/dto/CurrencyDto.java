package org.currency.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CurrencyDto {
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private String code;
  private Integer isoCode;
  private String description;
  private Boolean active;
  private Integer scale;
  private String symbol;
}

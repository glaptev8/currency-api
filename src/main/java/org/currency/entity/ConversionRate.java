package org.currency.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Table("conversion_rates")
public class ConversionRate {
  @Id
  private Long id;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private String sourceCode;
  private String destinationCode;
  private LocalDateTime rateBeginTime;
  private LocalDateTime rateEndTime;
  private BigDecimal rate;
  private String providerCode;
  private BigDecimal multiplier;
  private BigDecimal systemRate;
}


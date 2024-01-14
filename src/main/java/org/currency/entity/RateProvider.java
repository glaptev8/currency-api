package org.currency.entity;

import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
@Table("rate_providers")
public class RateProvider {
  private String providerCode;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private String providerName;
  private String description;
  private Integer priority;
  private Boolean active;
  private BigDecimal defaultMultiplier;
}

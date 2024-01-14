package org.currency.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Table("rate_correction_coefficients")
public class RateCorrectionCoefficient {
  @Id
  private Long id;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private Boolean archived;
  private String sourceCode;
  private String destinationCode;
  private BigDecimal multiplier;
  private String providerCode;
  private String creator;
  private String modifier;
  private LocalDateTime dateFrom;
  private LocalDateTime dateTo;
  private String profileType;
}


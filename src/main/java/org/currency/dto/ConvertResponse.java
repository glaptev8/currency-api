package org.currency.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvertResponse {
  private String sourceCode;
  private String destinationCode;
  private BigDecimal convertedAmount;
}

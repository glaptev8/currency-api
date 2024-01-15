package org.currency.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConvertRequest {
  private String sourceCode;
  private String destinationCode;
  private BigDecimal amountToConvert;
}

package org.currency.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("currencies")
public class Currency {
  @Id
  private Long id;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private String code;
  private Integer isoCode;
  private String description;
  private Boolean active;
  private Integer scale;
  private String symbol;
}

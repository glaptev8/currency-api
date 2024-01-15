package org.currency.config;

import org.currency.integration.currencyapi.CurrencyApiProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "integration")
public class IntegrationConfig {
  private CurrencyApiProperty currencyProperty;
}

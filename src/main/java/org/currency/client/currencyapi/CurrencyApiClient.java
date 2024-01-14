package org.currency.client.currencyapi;

import java.util.Map;

import org.currency.client.Client;
import org.currency.client.Sender;
import org.currency.config.IntegrationConfig;
import org.currency.dto.CurrencyResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CurrencyApiClient implements Client {
  private final Sender sender;
  private final IntegrationConfig integrationConfig;
  private final String PARAM_KEY_CURRENCIES_CONVERT_TO = "currencies";
  private final String PARAM_KEY_CURRENCY_CONVERT_FROM = "base_currency";

  public Mono<CurrencyResponse> getCurrencyRates(String currencies, String baseCurrency) {
    var headers = new HttpHeaders();

    integrationConfig
      .getCurrencyProperty()
      .getHeaders()
      .forEach(header -> headers.add(header.getKey(), header.getValue()));

    return sender.get(formUri(integrationConfig.getCurrencyProperty().getBaseUrl(), integrationConfig.getCurrencyProperty().getLatest()),
                      Map.of(PARAM_KEY_CURRENCIES_CONVERT_TO, baseCurrency,
                             PARAM_KEY_CURRENCY_CONVERT_FROM, currencies),
                      headers,
                      CurrencyResponse.class);
  }
}

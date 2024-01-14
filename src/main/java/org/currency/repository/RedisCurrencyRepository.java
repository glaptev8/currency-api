package org.currency.repository;

import reactor.core.publisher.Mono;

public interface RedisCurrencyRepository {
  Mono<Void> addCurrency(String currencyPair, String rate);

  Mono<String> findRate(String pair);

  Mono<Void> removeAllCurrencies();
}

package org.currency.repository;

import org.currency.entity.Currency;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import reactor.core.publisher.Mono;

public interface CurrencyRepository extends R2dbcRepository<Currency, Long> {
  Mono<Boolean> existsByCode(String code);

  Mono<Currency> findByCode(String code);
}

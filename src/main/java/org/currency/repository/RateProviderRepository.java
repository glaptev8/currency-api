package org.currency.repository;

import org.currency.entity.RateProvider;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import reactor.core.publisher.Mono;

public interface RateProviderRepository extends R2dbcRepository<RateProvider, String> {
  Mono<RateProvider> findByProviderCode(String providerCode);
  Mono<RateProvider> findByProviderName(String providerName);
}

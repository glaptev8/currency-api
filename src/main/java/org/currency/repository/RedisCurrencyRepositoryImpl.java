package org.currency.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class RedisCurrencyRepositoryImpl implements RedisCurrencyRepository {
  private final ReactiveRedisTemplate<String, String> redisTemplate;
  private final String CURRENCY_KEY = "currencies";

  public RedisCurrencyRepositoryImpl(@Qualifier("currenciesRedis") ReactiveRedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public Mono<Void> addCurrency(String currencyPair, String rate) {
    return redisTemplate.opsForHash().put(CURRENCY_KEY, currencyPair, rate)
      .then(Mono.empty());
  }

  @Override
  public Mono<String> findRate(String pair) {
    return redisTemplate.opsForHash().get(CURRENCY_KEY, pair)
      .map(rate -> (String) rate);
  }

  @Override
  public Mono<Void> removeAllCurrencies() {
    return redisTemplate.delete(CURRENCY_KEY)
      .then(Mono.empty());
  }
}

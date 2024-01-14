package org.currency.service;

import org.currency.entity.Currency;
import org.leantech.currency.dto.ConvertRequest;
import org.leantech.currency.dto.ConvertResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyService {
  Mono<Currency> addCurrency(Currency currency);

  Mono<Void> removeCurrency(Currency currency);

  Flux<Currency> getAllCurrencies();

  Mono<ConvertResponse> convertCurrencies(ConvertRequest convertRequest);
}

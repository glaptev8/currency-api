package org.currency.service;

import org.currency.dto.ConvertRequest;
import org.currency.dto.ConvertResponse;
import org.currency.entity.Currency;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CurrencyService {
  Mono<Currency> addCurrency(Currency currency);

  Mono<Void> removeCurrency(Currency currency);

  Flux<Currency> getAllCurrencies();

  Mono<ConvertResponse> convertCurrencies(ConvertRequest convertRequest);
}

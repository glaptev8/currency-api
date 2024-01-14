package org.currency.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

import org.currency.client.currencyapi.CurrencyApiClient;
import org.currency.dto.CurrencyResponse;
import org.currency.dto.RateProviderType;
import org.currency.entity.ConversionRate;
import org.currency.entity.RateCorrectionCoefficient;
import org.currency.entity.RateProvider;
import org.currency.repository.ConversionRateRepository;
import org.currency.repository.RateCorrectionCoefficientRepository;
import org.currency.repository.RateProviderRepository;
import org.currency.repository.RedisCurrencyRepository;
import org.currency.util.CurrencyUtil;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CurrencyJobService {

  private final CurrencyUtil currencyUtil;
  private final RateProviderRepository rateProviderRepository;
  private final CurrencyApiClient currencyApiClient;
  private final ConversionRateRepository conversionRateRepository;
  private final RedisCurrencyRepository redisCurrencyRepository;
  private final RateCorrectionCoefficientRepository rateCorrectionCoefficientRepository;

  public Mono<Void> createCurrencyRateJob() {
    return redisCurrencyRepository.removeAllCurrencies()
      .then(
        rateProviderRepository.findAll(Sort.by("priority"))
          .collectList()
          .flatMap(rateProviders -> rateCorrectionCoefficientRepository.findAll()
            .collectList()
            .flatMap(rateCorrectionCoefficients ->
                       currencyUtil.getCurrencyParameter()
                         .flatMap(currenciesPair ->
                                    Flux.fromIterable(currenciesPair.entrySet())
                                      .flatMap(currenciesPairEntry ->
                                                 Flux.fromStream(rateProviders.stream())
                                                   .flatMap(rateProvider -> {
                                                     if (rateProvider.getProviderName() == null || RateProviderType.valueOf(rateProvider.getProviderName()) == RateProviderType.CURRENCY_API) {
                                                       try {
                                                         return Mono.just(Map.entry(currencyApiClient.getCurrencyRates(currenciesPairEntry.getKey(), currenciesPairEntry.getValue()), rateProvider));
                                                       } catch (Exception e) {
                                                       }
                                                     }
                                                     if (RateProviderType.valueOf(rateProvider.getProviderName()) == RateProviderType.BLABLABLA) {
                                                       return Mono.empty();
                                                     }
                                                     return null;
                                                   })
                                                   .filter(Objects::nonNull)
                                                   .take(1)
                                                   .flatMap(currencyResponseWithProvider ->
                                                              currencyResponseWithProvider.getKey()
                                                                .flatMap(currencyResponse ->
                                                                           saveRates(currencyResponse, currenciesPairEntry, rateCorrectionCoefficients, currencyResponseWithProvider.getValue())))
                                      )
                                      .then(Mono.empty()))
            ))
          .then()
      );
  }

  private Mono<Void> saveRates(CurrencyResponse currencyResponse,
                               Entry<String, String> currenciesPairEntry,
                               List<RateCorrectionCoefficient> rateCorrectionCoefficients,
                               RateProvider rateProvider) {
    return Flux.fromIterable(currencyResponse.getData().entrySet())
      .flatMap(currencyResponseEntry ->
                 currencyUtil.getCorrection(rateCorrectionCoefficients,
                                            currenciesPairEntry.getKey(),
                                            currencyResponseEntry.getValue().getCode(),
                                            currencyResponse.getMeta().getLastUpdatedAt(),
                                            rateProvider.getProviderCode())
                   .flatMap(correction ->
                              conversionRateRepository.save(ConversionRate.builder()
                                                              .sourceCode(currenciesPairEntry.getKey())
                                                              .destinationCode(currencyResponseEntry.getValue().getCode())
                                                              .rateEndTime(LocalDateTime.now().plusMinutes(15L))
                                                              .rate(currencyResponseEntry.getValue().getValue())
                                                              .providerCode(rateProvider.getProviderCode())
                                                              .multiplier(correction.getMultiplier())
                                                              .systemRate(currencyResponseEntry.getValue().getValue().multiply(correction.getMultiplier()))
                                                              .build())
                                .then(redisCurrencyRepository.addCurrency(currenciesPairEntry.getKey() + currencyResponseEntry.getValue().getCode(), currencyResponseEntry.getValue().getValue().multiply(correction.getMultiplier()).toString())))
      )
      .then(Mono.empty());
  }
}

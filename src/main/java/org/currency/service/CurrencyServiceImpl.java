package org.currency.service;

import java.math.BigDecimal;
import java.util.Objects;

import org.currency.entity.Currency;
import org.currency.entity.RateCorrectionCoefficient;
import org.currency.repository.CurrencyRepository;
import org.currency.repository.RateCorrectionCoefficientRepository;
import org.currency.repository.RedisCurrencyRepository;
import org.leantech.currency.dto.ConvertRequest;
import org.leantech.currency.dto.ConvertResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

  private final CurrencyRepository currencyRepository;
  private final RateCorrectionCoefficientRepository rateCorrectionCoefficientRepository;
  private final TransactionalOperator transactionalOperator;
  private final RedisCurrencyRepository redisCurrencyRepository;

  @Override
  public Mono<Currency> addCurrency(Currency currencyToSave) {
    return transactionalOperator.transactional(
      currencyRepository.findByCode(currencyToSave.getCode())
        .flatMap(savedCurrency -> {
          if (savedCurrency.getActive()) {
            return Mono.error(new RuntimeException("currency exists"));
          }
          savedCurrency.setActive(Boolean.TRUE);
          return currencyRepository.save(savedCurrency);
        })
        .switchIfEmpty(currencyRepository.save(currencyToSave))
        .flatMap(savedCurrency -> currencyRepository.findAll()
          .flatMap(currency -> {
            if (!Objects.equals(currency.getCode(), currencyToSave.getCode())) {
              return rateCorrectionCoefficientRepository
                .findBySourceCodeAndDestinationCodeAndMultiplierAndArchivedFalseAndProviderCodeIsNullAndDateFromIsNullAndDateToIsNull(currencyToSave.getCode(),
                                                                                 currency.getCode(),
                                                                                 BigDecimal.ONE)
                .switchIfEmpty(rateCorrectionCoefficientRepository.save(RateCorrectionCoefficient.builder()
                                                                          .sourceCode(currencyToSave.getCode())
                                                                          .destinationCode(currency.getCode())
                                                                          .multiplier(BigDecimal.ONE)
                                                                          .build())
                                 .then(rateCorrectionCoefficientRepository.save(RateCorrectionCoefficient.builder()
                                                                                  .sourceCode(currency.getCode())
                                                                                  .destinationCode(currencyToSave.getCode())
                                                                                  .multiplier(BigDecimal.ONE)
                                                                                  .build())))
                .then(Mono.just(currency));
            }
            return Mono.just(currency);
          })
          .then(Mono.just(savedCurrency)))
    );
  }

  @Override
  public Mono<Void> removeCurrency(Currency currency) {
    return currencyRepository.findByCode(currency.getCode())
      .flatMap(savedCurrency -> {
        savedCurrency.setActive(Boolean.FALSE);
        return currencyRepository.save(savedCurrency)
          .then(Mono.empty());
      });
  }

  @Override
  public Flux<Currency> getAllCurrencies() {
    return currencyRepository.findAll();
  }

  @Override
  public Mono<ConvertResponse> convertCurrencies(ConvertRequest convertRequest) {
    return redisCurrencyRepository.findRate(convertRequest.getSourceCode() + convertRequest.getDestinationCode())
      .flatMap(rate -> {
        var convertedAmount = convertRequest.getAmountToConvert().multiply(new BigDecimal(rate));
        return Mono.just(ConvertResponse.builder()
                           .sourceCode(convertRequest.getSourceCode())
                           .destinationCode(convertRequest.getDestinationCode())
                           .convertedAmount(convertedAmount)
                           .build());
      });
  }
}

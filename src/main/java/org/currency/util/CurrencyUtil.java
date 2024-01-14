package org.currency.util;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.currency.entity.RateCorrectionCoefficient;
import org.currency.service.CurrencyService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CurrencyUtil {
  private final CurrencyService currencyService;
  public Mono<Map<String, String>> getCurrencyParameter() {
    Map<String, String> result = new HashMap<>();
    return currencyService.getAllCurrencies()
      .collectList()
      .flatMap(currencies -> {
        for (int i = 0; i < currencies.size(); i++) {
          StringBuilder pairs = new StringBuilder();
          for (int j = 0; j < currencies.size(); j++) {
            if (j == i) {
              continue;
            }
            pairs.append(currencies.get(j).getCode());
            if (!(j == currencies.size() - 1 || (j + 1 == i && j + 2 == currencies.size()))) {
              pairs.append(',');
            }
          }
          result.put(currencies.get(i).getCode(), pairs.toString());
        }
        return Mono.just(result);
      });
  }

  public Mono<RateCorrectionCoefficient> getCorrection(List<RateCorrectionCoefficient> rateCorrectionCoefficients,
                                                       String sourceCode,
                                                       String destinationCode,
                                                       LocalDateTime date,
                                                       String providerCode) {
    RateCorrectionCoefficient defaultCorrection = null;
    RateCorrectionCoefficient correctionByProviderAndDateFrom = null;
    RateCorrectionCoefficient correctionByProvider = null;
    RateCorrectionCoefficient correctionByDateFromAndDateTo = null;
    RateCorrectionCoefficient correctionByDateFrom = null;
    for (RateCorrectionCoefficient rateCorrectionCoefficient : rateCorrectionCoefficients) {
      if (!rateCorrectionCoefficient.getSourceCode().equals(sourceCode) || !rateCorrectionCoefficient.getDestinationCode().equals(destinationCode)) {
        continue;
      }
      if (providerCode != null &&
          date != null &&
          providerCode.equals(rateCorrectionCoefficient.getProviderCode()) &&
          date.isAfter(rateCorrectionCoefficient.getDateFrom()) &&
          date.isBefore(rateCorrectionCoefficient.getDateTo())
          ) {
        return Mono.just(rateCorrectionCoefficient);
      }
      if (providerCode != null &&
          date != null &&
          providerCode.equals(rateCorrectionCoefficient.getProviderCode()) &&
          date.isAfter(rateCorrectionCoefficient.getDateFrom()) &&
          rateCorrectionCoefficient.getDateTo() == null) {
        correctionByProviderAndDateFrom = rateCorrectionCoefficient;
      }
      else if (providerCode != null &&
               date != null &&
               providerCode.equals(rateCorrectionCoefficient.getProviderCode()) &&
               rateCorrectionCoefficient.getDateFrom() == null &&
               rateCorrectionCoefficient.getDateTo() == null) {
        correctionByProvider = rateCorrectionCoefficient;
      }
      else if (providerCode != null &&
               providerCode.equals(rateCorrectionCoefficient.getProviderCode()) &&
               rateCorrectionCoefficient.getDateFrom() == null &&
               rateCorrectionCoefficient.getDateTo() == null) {
        correctionByProvider = rateCorrectionCoefficient;
      }
      else if (date != null &&
               rateCorrectionCoefficient.getProviderCode() == null &&
               date.isAfter(rateCorrectionCoefficient.getDateFrom()) &&
               date.isBefore(rateCorrectionCoefficient.getDateTo())) {
        correctionByDateFromAndDateTo = rateCorrectionCoefficient;
      }
      else if (date != null &&
               rateCorrectionCoefficient.getProviderCode() == null &&
               date.isAfter(rateCorrectionCoefficient.getDateFrom()) &&
               rateCorrectionCoefficient.getDateTo() == null) {
        correctionByDateFrom = rateCorrectionCoefficient;
      }
      else if (rateCorrectionCoefficient.getProviderCode() == null &&
               rateCorrectionCoefficient.getDateFrom() == null &&
               rateCorrectionCoefficient.getDateTo() == null) {
        defaultCorrection = rateCorrectionCoefficient;
      }
    }
    if (correctionByProviderAndDateFrom != null) {
      return Mono.just(correctionByProviderAndDateFrom);
    }
    else if (correctionByProvider != null) {
      return Mono.just(correctionByProvider);
    }
    else if (correctionByDateFromAndDateTo != null) {
      return Mono.just(correctionByDateFromAndDateTo);
    }
    else if (correctionByDateFrom != null) {
      return Mono.just(correctionByDateFrom);
    }
    else if (defaultCorrection != null) {
      return Mono.just(defaultCorrection);
    }
    return Mono.empty();
  }
}

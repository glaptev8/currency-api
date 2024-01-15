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
}

package org.currency.controller;

import org.currency.entity.Currency;
import org.currency.mapper.MapStructMapper;
import org.currency.service.CurrencyService;
import org.leantech.currency.dto.ConvertRequest;
import org.leantech.currency.dto.ConvertResponse;
import org.leantech.currency.dto.CurrencyDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currency")
public class CurrencyController {

  private final CurrencyService currencyService;
  private final MapStructMapper mapper;

  @PostMapping("save")
  public Mono<CurrencyDto> saveCurrency(@RequestBody Currency currency) {
    return currencyService.addCurrency(currency)
      .map(mapper::currencyMapperToDto);
  }

  @PostMapping("convert")
  public Mono<ConvertResponse> convert(@RequestBody ConvertRequest convertRequest) {
    return currencyService.convertCurrencies(convertRequest);
  }
}

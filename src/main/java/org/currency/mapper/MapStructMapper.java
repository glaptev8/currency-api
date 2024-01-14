package org.currency.mapper;

import org.currency.entity.Currency;
import org.leantech.currency.dto.CurrencyDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
  CurrencyDto currencyMapperToDto(Currency currency);
}

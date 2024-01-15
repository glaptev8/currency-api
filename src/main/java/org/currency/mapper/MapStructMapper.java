package org.currency.mapper;

import org.currency.dto.CurrencyDto;
import org.currency.entity.Currency;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
  CurrencyDto currencyMapperToDto(Currency currency);
}

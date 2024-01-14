package org.currency.repository;

import org.currency.entity.ConversionRate;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ConversionRateRepository extends R2dbcRepository<ConversionRate, Long> {
}

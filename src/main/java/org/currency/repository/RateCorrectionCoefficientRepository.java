package org.currency.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.currency.entity.RateCorrectionCoefficient;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import reactor.core.publisher.Mono;


public interface RateCorrectionCoefficientRepository extends R2dbcRepository<RateCorrectionCoefficient, Long> {
  @Query("""
            SELECT 
                  COALESCE(
                    (SELECT * FROM rate_correction_coefficients WHERE provider_code = :providerCode AND date_from <= :date AND date_to > :date),
                    (SELECT * FROM rate_correction_coefficients WHERE provider_code = :providerCode AND date_from <= :date AND date_to IS NULL),
                    (SELECT * FROM rate_correction_coefficients WHERE provider_code = :providerCode AND date_from IS NULL AND date_to IS NULL),
                    (SELECT * FROM rate_correction_coefficients WHERE date_from <= :date AND date_to > :date),
                    (SELECT * FROM rate_correction_coefficients WHERE date_from <= :date AND date_to IS NULL),
                    (SELECT * FROM rate_correction_coefficients WHERE provider_code = :providerCode AND date_from IS NULL AND date_to IS NULL),
                    (SELECT * FROM rate_correction_coefficients WHERE provider_code IS NULL AND date_from IS NULL AND date_to IS NULL)
                  )
                  where rate_correction_coefficients.archived = FALSE
                LIMIT 1
""")
  Mono<RateCorrectionCoefficient> findRateCorrectionCoefficient(LocalDateTime date, String providerCode);

  Mono<RateCorrectionCoefficient> findBySourceCodeAndDestinationCodeAndMultiplierAndArchivedFalseAndProviderCodeIsNullAndDateFromIsNullAndDateToIsNull(String sourceCode,
                                                                                  String destinationCode,
                                                                                  BigDecimal multiplier);
}

package org.currency.job;

import org.currency.service.CurrencyJobService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class CurrencyRateJob {

  private final CurrencyJobService currencyJobService;

  @Scheduled(fixedRate = 15 * 60 * 1000)
  @SchedulerLock(name = "TaskScheduler_currencyRateTask", lockAtLeastFor = "14m")
  public void createCurrencyRateJob() {
    currencyJobService
      .createCurrencyRateJob()
      .subscribe();
  }
}

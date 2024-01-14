package org.currency.client;

import java.util.Map;

import org.springframework.http.HttpHeaders;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Sender {
  <T, K> Mono<T> post(String url,
                      K requestEntity,
                      Map<String, String> params,
                      HttpHeaders headers,
                      Class<T> clazz);

  <T, K> Flux<T> postFlux(String url,
                      K requestEntity,
                      Map<String, String> params,
                      HttpHeaders headers,
                      Class<T> clazz);

  <K> Mono<K> get(String url,
                  Map<String, String> params,
                  HttpHeaders headers,
                  Class<K> clazz);

  <K> Flux<K> getFlux(String url,
                  Map<String, String> params,
                  HttpHeaders headers,
                  Class<K> clazz);
}

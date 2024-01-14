package org.currency.client;

import java.net.URI;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SenderImpl implements Sender {

  private final WebClient webClient;

  @Override
  public <T, K> Mono<T> post(String url,
                             K requestEntity,
                             Map<String, String> params,
                             HttpHeaders headers,
                             Class<T> clazz) {
    var uri = getUri(url, params);

    final HttpHeaders httpHeaders = new HttpHeaders();
    if (Objects.nonNull(headers)) {
      httpHeaders.addAll(headers);
    }

    return this.webClient
      .post()
      .uri(uri)
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .headers(headers1 -> headers1.addAll(httpHeaders))
      .body(Mono.just(requestEntity), requestEntity.getClass())
      .retrieve()
      .bodyToMono(clazz);
  }

  @Override
  public <T, K> Flux<T> postFlux(String url,
                                 K requestEntity,
                                 Map<String, String> params,
                                 HttpHeaders headers,
                                 Class<T> clazz) {
    var uri = getUri(url, params);

    final HttpHeaders httpHeaders = new HttpHeaders();
    if (Objects.nonNull(headers)) {
      httpHeaders.addAll(headers);
    }

    return this.webClient
      .post()
      .uri(uri)
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .headers(headers1 -> headers1.addAll(httpHeaders))
      .body(Mono.just(requestEntity), requestEntity.getClass())
      .retrieve()
      .bodyToFlux(clazz);
  }

  @Override
  public <K> Mono<K> get(String url, Map<String, String> params, HttpHeaders headers, Class<K> clazz) {
    var uri = getUri(url, params);

    final HttpHeaders httpHeaders = new HttpHeaders();
    if (Objects.nonNull(headers)) {
      httpHeaders.addAll(headers);
    }

    return this.webClient
      .get()
      .uri(uri)
      .accept(MediaType.APPLICATION_JSON)
      .headers(headers1 -> headers1.addAll(httpHeaders))
      .retrieve()
      .bodyToMono(clazz);
  }

  @Override
  public <K> Flux<K> getFlux(String url, Map<String, String> params, HttpHeaders headers, Class<K> clazz) {
    var uri = getUri(url, params);

    final HttpHeaders httpHeaders = new HttpHeaders();
    if (Objects.nonNull(headers)) {
      httpHeaders.addAll(headers);
    }

    return this.webClient
      .get()
      .uri(uri)
      .accept(MediaType.APPLICATION_JSON)
      .headers(headers1 -> headers1.addAll(httpHeaders))
      .retrieve()
      .bodyToFlux(clazz);
  }

  private URI getUri(String relativeUrl, Map<String, String> params) {
    var builder = UriComponentsBuilder.fromHttpUrl(relativeUrl);
    params.forEach(builder::queryParam);

    return builder.build().toUri();
  }
}

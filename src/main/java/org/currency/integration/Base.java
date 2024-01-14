package org.currency.integration;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Base {
  private String baseUrl;
  private List<Header> headers = new ArrayList<>();
}

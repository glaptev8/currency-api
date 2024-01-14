package org.currency.client;

import java.util.Arrays;
import java.util.stream.Stream;

import io.netty.util.internal.StringUtil;

public interface Client {
  default String formUri(String... args) {
    StringBuilder sb = new StringBuilder();
    sb.append(args[0]);
    if (args.length > 1)
      Stream.of(Arrays.copyOfRange(args, 1, args.length))
        .filter((s)->!StringUtil.isNullOrEmpty(s))
        .map(arg -> "/" + arg)
        .forEachOrdered(sb::append);

    return sb.toString();
  }
}

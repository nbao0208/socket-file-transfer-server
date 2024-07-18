package org.example.socketfileserver.config;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class DIInjector {
  public static final Injector injector = Guice.createInjector(new DIConfiguration());
}

package org.example.socketfileserver.shared.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FilePrioritization {
  CRITICAL(10),
  HIGH(4),
  NORMAL(1);
  private final int prioritization;
}

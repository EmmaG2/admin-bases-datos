package com.fernandogranados.model;

import java.util.List;

public record ReportCard(List<Grade> grades, Double average) {
  public ReportCard {
    grades = List.copyOf(grades);
  }

  public record Grade(String subject, short period, short grade) {}
}

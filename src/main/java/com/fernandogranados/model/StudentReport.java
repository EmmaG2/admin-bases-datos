package com.fernandogranados.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public record StudentReport(List<Alumno> students, List<SubjectCount> subjects) {
  public StudentReport {
    students = List.copyOf(students);
    subjects = List.copyOf(subjects);
  }

  public Map<String, Long> specialtyCounts() {
    Map<String, Long> counts = new TreeMap<>();
    for (Alumno student : students) counts.merge(student.getEspecialidad(), 1L, Long::sum);
    return Collections.unmodifiableMap(counts);
  }

  public record SubjectCount(String code, String name, String specialty, long students) {}
}

package com.fernandogranados.pdf;

import static com.fernandogranados.pdf.InstitutionalPdf.*;

import com.fernandogranados.model.Alumno;
import com.fernandogranados.model.StudentReport;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

public final class StudentReportPdfExporter {
  public void export(StudentReport report, Path destination) throws IOException {
    Map<String, Long> specialties = report.specialtyCounts();
    InstitutionalPdf.write(destination, "Reporte de alumnos", "REPORTE DE ALUMNOS",
        "ITD  /  Reporte de alumnos", (document, bold) -> {
          document.add(new Paragraph("Generado: " + LocalDateTime.now()
              .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).setFontSize(9));
          document.add(new Paragraph("Total de alumnos inscritos: " + report.students().size())
              .setFont(bold).setFontSize(18).setFontColor(GUINDA));
          document.add(new Paragraph("Se incluyen todos los alumnos registrados. Los conteos por materia "
              + "consideran alumnos distintos con calificaciones en cualquier periodo; no representan "
              + "inscripciones activas. Un alumno puede aparecer en varias materias.")
              .setFontSize(9).setMarginBottom(18));

          heading(document, "Alumnos por especialidad", bold);
          if (specialties.isEmpty()) {
            document.add(new Paragraph("No hay alumnos registrados."));
          } else {
            Table table = table(new float[] {7, 3}, bold, "Especialidad", "Alumnos");
            int row = 0;
            for (var entry : specialties.entrySet()) {
              addRow(table, row++, entry.getKey(), entry.getValue().toString());
            }
            document.add(table);
            charts(document, new ArrayList<>(specialties.entrySet()), "Alumnos por especialidad", bold, PlotOrientation.VERTICAL);
          }

          if (!report.subjects().isEmpty()) document.add(new AreaBreak());
          heading(document, "Alumnos registrados por materia", bold);
          if (report.subjects().isEmpty()) {
            document.add(new Paragraph("No hay materias registradas."));
          } else {
            Table table = table(new float[] {2.3f, 4.5f, 3, 1.7f}, bold,
                "Clave", "Materia", "Especialidad", "Alumnos");
            int row = 0;
            List<Map.Entry<String, Long>> counts = new ArrayList<>();
            for (var subject : report.subjects()) {
              addRow(table, row++, subject.code(), subject.name(), subject.specialty(),
                  Long.toString(subject.students()));
              counts.add(Map.entry(subject.code(), subject.students()));
            }
            document.add(table);
            charts(document, counts, "Alumnos por materia (clave)", bold, PlotOrientation.HORIZONTAL);
          }

          if (!report.students().isEmpty()) document.add(new AreaBreak());
          heading(document, "Listado completo de alumnos", bold);
          if (report.students().isEmpty()) {
            document.add(new Paragraph("No hay alumnos registrados."));
          } else {
            Table table = table(new float[] {1, 4.5f, 2.2f, 3, 2.3f}, bold,
                "ID", "Nombre", "Matrícula", "Especialidad", "Fecha de ingreso");
            int row = 0;
            for (Alumno student : report.students()) {
              addRow(table, row++, Integer.toString(student.getId()), student.getNombre(),
                  student.getMatricula(), student.getEspecialidad(), student.getFechaIngreso());
            }
            document.add(table);
          }
        });
  }

  private static void heading(Document document, String title, PdfFont bold) {
    document.add(new Paragraph(title).setFont(bold).setFontSize(17).setFontColor(GUINDA)
        .setMarginBottom(14).setKeepWithNext(true));
  }

  private static Table table(float[] widths, PdfFont bold, String... titles) {
    Table table = new Table(UnitValue.createPercentArray(widths)).useAllAvailableWidth()
        .setMarginBottom(16).setFontSize(9);
    for (String title : titles) table.addHeaderCell(headerCell(title, bold));
    return table;
  }

  private static void addRow(Table table, int row, String... values) {
    for (String value : values) table.addCell(cell(value, row % 2 == 0 ? TINT : PAPER).setPadding(8));
  }

  private static void charts(Document document, List<Map.Entry<String, Long>> counts,
      String title, PdfFont bold, PlotOrientation orientation) throws IOException {
    long maximum = counts.stream().mapToLong(Map.Entry::getValue).max().orElse(0);
    for (int start = 0; start < counts.size(); start += 15) {
      int end = Math.min(start + 15, counts.size());
      DefaultCategoryDataset dataset = new DefaultCategoryDataset();
      for (var entry : counts.subList(start, end)) {
        dataset.addValue(entry.getValue(), "Alumnos", entry.getKey());
      }
      JFreeChart chart = ChartFactory.createBarChart(null, null, "Alumnos", dataset,
          orientation, false, false, false);
      chart.setBackgroundPaint(new Color(250, 248, 244));
      var plot = chart.getCategoryPlot();
      plot.setBackgroundPaint(new Color(250, 248, 244));
      plot.setOutlineVisible(false);
      plot.setRangeGridlinePaint(new Color(220, 214, 216));
      plot.getDomainAxis().setTickLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
      plot.getDomainAxis().setMaximumCategoryLabelWidthRatio(orientation == PlotOrientation.VERTICAL ? 0.9f : 0.35f);
      plot.getDomainAxis().setMaximumCategoryLabelLines(4);
      if (orientation == PlotOrientation.VERTICAL) {
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
      }
      NumberAxis axis = (NumberAxis) plot.getRangeAxis();
      axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      axis.setRange(0, Math.max(1, Math.ceil(maximum * 1.15)));
      axis.setTickLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
      axis.setLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
      BarRenderer renderer = (BarRenderer) plot.getRenderer();
      renderer.setSeriesPaint(0, new Color(153, 0, 77));
      renderer.setBarPainter(new StandardBarPainter());
      renderer.setShadowVisible(false);
      renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
      renderer.setDefaultItemLabelsVisible(true);
      renderer.setDefaultItemLabelFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
      renderer.setMaximumBarWidth(0.12);
      ByteArrayOutputStream image = new ByteArrayOutputStream();
      ChartUtils.writeChartAsPNG(image, chart, 1300,
          orientation == PlotOrientation.VERTICAL ? 700 : Math.max(360, (end - start) * 65 + 100));
      heading(document, title + (counts.size() > 15 ? " (" + (start + 1) + "-" + end + ")" : ""), bold);
      document.add(new Image(ImageDataFactory.create(image.toByteArray())).setWidth(515));
    }
  }
}

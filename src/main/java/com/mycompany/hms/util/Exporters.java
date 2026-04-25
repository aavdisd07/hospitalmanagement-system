package com.mycompany.hms.util;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.awt.Color;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;

public final class Exporters {

    private Exporters() {}

    public static void toCsv(JTable table, Path file) throws IOException {
        TableModel m = table.getModel();
        try (CSVWriter w = new CSVWriter(new FileWriter(file.toFile()))) {
            String[] headers = new String[m.getColumnCount()];
            for (int c = 0; c < headers.length; c++) headers[c] = m.getColumnName(c);
            w.writeNext(headers);
            for (int r = 0; r < m.getRowCount(); r++) {
                String[] row = new String[m.getColumnCount()];
                for (int c = 0; c < row.length; c++) row[c] = String.valueOf(m.getValueAt(r, c));
                w.writeNext(row);
            }
        }
    }

    public static void toXlsx(JTable table, String sheetName, Path file) throws IOException {
        TableModel m = table.getModel();
        try (Workbook wb = new XSSFWorkbook();
             FileOutputStream out = new FileOutputStream(file.toFile())) {
            Sheet sh = wb.createSheet(sheetName);
            CellStyle headerStyle = wb.createCellStyle();
            org.apache.poi.ss.usermodel.Font font = wb.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            Row header = sh.createRow(0);
            for (int c = 0; c < m.getColumnCount(); c++) {
                Cell cell = header.createCell(c);
                cell.setCellValue(m.getColumnName(c));
                cell.setCellStyle(headerStyle);
            }
            for (int r = 0; r < m.getRowCount(); r++) {
                Row row = sh.createRow(r + 1);
                for (int c = 0; c < m.getColumnCount(); c++) {
                    Object v = m.getValueAt(r, c);
                    Cell cell = row.createCell(c);
                    if (v instanceof Number n) cell.setCellValue(n.doubleValue());
                    else cell.setCellValue(v == null ? "" : v.toString());
                }
            }
            for (int c = 0; c < m.getColumnCount(); c++) sh.autoSizeColumn(c);
            wb.write(out);
        }
    }

    public static void toPdf(JTable table, String title, Path file) throws IOException {
        TableModel m = table.getModel();
        Document doc = new Document(PageSize.A4.rotate(), 36, 36, 48, 48);
        try (FileOutputStream out = new FileOutputStream(file.toFile())) {
            PdfWriter.getInstance(doc, out);
            doc.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph t = new Paragraph(title, titleFont);
            t.setAlignment(Element.ALIGN_LEFT);
            doc.add(t);

            Font subFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, new Color(0x888888));
            Paragraph sub = new Paragraph("Generated " + LocalDateTime.now().withNano(0), subFont);
            sub.setSpacingAfter(10f);
            doc.add(sub);

            PdfPTable t2 = new PdfPTable(m.getColumnCount());
            t2.setWidthPercentage(100);
            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
            for (int c = 0; c < m.getColumnCount(); c++) {
                PdfPCell h = new PdfPCell(new Phrase(m.getColumnName(c), headFont));
                h.setBackgroundColor(new Color(0xE67E22));
                h.setPadding(6f);
                t2.addCell(h);
            }
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            for (int r = 0; r < m.getRowCount(); r++) {
                for (int c = 0; c < m.getColumnCount(); c++) {
                    Object v = m.getValueAt(r, c);
                    PdfPCell cell = new PdfPCell(new Phrase(v == null ? "" : v.toString(), cellFont));
                    cell.setPadding(5f);
                    if (r % 2 == 1) cell.setBackgroundColor(new Color(0xF7F7F7));
                    t2.addCell(cell);
                }
            }
            doc.add(t2);
            doc.close();
        } catch (com.lowagie.text.DocumentException e) {
            throw new IOException("PDF export failed", e);
        }
    }
}

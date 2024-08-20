package com.example.dashboard_service.service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dashboard_service.exception.ReportGenerationException;
import com.example.dashboard_service.model.NicInvalid;
import com.example.dashboard_service.model.NicRecord;
@Service
public class ReportService {

    @Autowired
    private DashService dashService;

    public ByteArrayInputStream generateReport(
    String format, 
    boolean includeFemaleNics, 
    boolean includeMaleNics, 
    boolean includeTotalRecords, 
    boolean includeInvalidRecords, 
    boolean includeTotalInvalidRecords, 
    boolean includeTotalValidRecords) throws IOException {
        try {
            switch (format.toLowerCase()) {
                case "pdf" -> {
                    return generatePdfReport(includeFemaleNics, includeMaleNics, includeTotalRecords, includeInvalidRecords,includeTotalInvalidRecords,includeTotalValidRecords);
                }
                case "csv" -> {
                    return generateCsvReport(includeFemaleNics, includeMaleNics, includeTotalRecords, includeInvalidRecords);
                }
                case "xlsx" -> {
                    return generateXlsxReport(includeFemaleNics, includeMaleNics, includeTotalRecords, includeInvalidRecords);
                }
                default -> throw new IllegalArgumentException("Invalid format: " + format);
            }
        } catch (RuntimeException e) {
            throw new ReportGenerationException("Error generating report in format: " + format, e);
        }
    }

    private ByteArrayInputStream generatePdfReport(
    boolean includeFemaleNics,
    boolean includeMaleNics,
    boolean includeTotalRecords,
    boolean includeInvalidRecords,
    boolean includeTotalInvalidRecords,
    boolean includeTotalValidRecords) {

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try (PDDocument document = new PDDocument()) {
        PDPage page = new PDPage();
        document.addPage(page);

        float margin = 72; // 1 inch margin
        float yPosition = page.getMediaBox().getHeight() - margin;
        float rowHeight = 20f;
        float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
        float headerHeight = 25f;

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            // Title and date
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 24);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText("NIC REPORT");
            contentStream.endText();
            contentStream.moveTo(margin, yPosition - 5);
            contentStream.lineTo(page.getMediaBox().getWidth() - margin, yPosition - 5);
            contentStream.stroke();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition - 20);
            contentStream.showText("Generated on: " + java.time.LocalDateTime.now().toString());
            contentStream.endText();

            yPosition -= 40;

            // Add summary lines
            if (includeTotalRecords) {
                yPosition = addSummaryLine(contentStream, "Total Records", (int) dashService.getTotalRecords(), yPosition, margin);
            }
            if (includeMaleNics) {
                yPosition = addSummaryLine(contentStream, "Male Users", (int) dashService.getMaleUsers(), yPosition, margin);
            }
            if (includeFemaleNics) {
                yPosition = addSummaryLine(contentStream, "Female Users", (int) dashService.getFemaleUsers(), yPosition, margin);
            }
            if (includeInvalidRecords) {
                yPosition = addSummaryLine(contentStream, "Invalid Records", (int) dashService.getTotalInvalidRecords(), yPosition, margin);
            }

            
            
            // Draw valid NIC table
            if (includeTotalValidRecords) {
                List<NicRecord> validRecords = dashService.getAllRecords();
                String[] validHeaders = {"NIC Number", "Gender", "Birth Date", "Age", "File Name"};
                yPosition = drawTable(contentStream, yPosition, margin, validRecords, validHeaders, document, rowHeight, headerHeight, tableWidth);
            }
            
            // Draw invalid NIC table
            if (includeTotalInvalidRecords) {
                List<NicInvalid> invalidRecords = dashService.getAllInvalidRecords();
                String[] invalidHeaders = {"NIC Number", "File Name", "Error Message"};
                yPosition = drawTable(contentStream, yPosition, margin, invalidRecords, invalidHeaders, document, rowHeight, headerHeight, tableWidth);
            }
            
        }

        document.save(out);
    } catch (IOException e) {
        throw new ReportGenerationException("Error generating PDF report", e);
    }

    return new ByteArrayInputStream(out.toByteArray());
}
private float addSummaryLine(PDPageContentStream contentStream, String label, int value, float yPosition, float margin) throws IOException {
    contentStream.setStrokingColor(Color.BLACK);
    contentStream.setNonStrokingColor(Color.LIGHT_GRAY);
    contentStream.addRect(margin, yPosition - 14, 200, 14); // Increase height for padding
    contentStream.stroke();
    contentStream.setNonStrokingColor(Color.BLACK);
    contentStream.beginText();
    contentStream.newLineAtOffset(margin + 2, yPosition - 10); // Adjust vertical offset
    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
    contentStream.showText(label + ": ");
    contentStream.endText();

    contentStream.beginText();
    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
    contentStream.setNonStrokingColor(Color.BLUE);
    contentStream.newLineAtOffset(margin + 150, yPosition - 10); // Adjust vertical offset
    contentStream.showText(String.valueOf(value));
    contentStream.endText();

    return yPosition - 25; // Increase space between lines
}

private float drawTable(PDPageContentStream contentStream, float yPosition, float margin, List<?> records, String[] headers, PDDocument document, float rowHeight, float headerHeight, float tableWidth) throws IOException {
    float cellMargin = 5f;
    float[] columnWidths = calculateColumnWidths(headers.length, tableWidth);

    // Draw table headers
    contentStream.setNonStrokingColor(Color.decode("#071952"));
    contentStream.addRect(margin, yPosition, tableWidth, headerHeight);
    contentStream.fill();
    contentStream.setNonStrokingColor(Color.WHITE);
    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);

    // Position headers based on column widths
    float textXPosition = margin + cellMargin;
    contentStream.beginText();
    contentStream.newLineAtOffset(textXPosition, yPosition + headerHeight / 2 - 6);
    for (int i = 0; i < headers.length; i++) {
        contentStream.showText(headers[i]);
        textXPosition += columnWidths[i]; // Increment position by column width
        contentStream.newLineAtOffset(columnWidths[i], 0);
    }
    contentStream.endText();

    yPosition -= headerHeight;

    // Draw table rows
    boolean isAlternate = false;
    for (Object record : records) {
        if (yPosition < margin + rowHeight) { // Check if new page is needed
            contentStream.close();
            PDPage newPage = new PDPage();
            document.addPage(newPage);
            contentStream = new PDPageContentStream(document, newPage);
            yPosition = newPage.getMediaBox().getHeight() - margin;

            // Redraw table headers
            contentStream.setNonStrokingColor(Color.decode("#071952"));
            contentStream.addRect(margin, yPosition, tableWidth, headerHeight);
            contentStream.fill();
            contentStream.setNonStrokingColor(Color.WHITE);
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);

            textXPosition = margin + cellMargin;
            contentStream.beginText();
            contentStream.newLineAtOffset(textXPosition, yPosition + headerHeight / 2 - 6);
            for (int i = 0; i < headers.length; i++) {
                contentStream.showText(headers[i]);
                textXPosition += columnWidths[i]; // Increment position by column width
                contentStream.newLineAtOffset(columnWidths[i], 0);
            }
            contentStream.endText();

            yPosition -= headerHeight;
        }

        contentStream.setNonStrokingColor(isAlternate ? new Color(0xF6, 0xF7, 0xC4) : new Color(0xFD, 0xFF, 0xAB));
        contentStream.addRect(margin, yPosition, tableWidth, rowHeight);
        contentStream.fill();

        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);

        // Position text within each column
        textXPosition = margin + cellMargin;
        contentStream.beginText();
        contentStream.newLineAtOffset(textXPosition, yPosition + rowHeight / 2 - 5);

        // Handle different record types and null values
        if (record instanceof NicRecord nicRecord) {
            contentStream.showText(nicRecord.getNic_no() != null ? nicRecord.getNic_no() : "");
            textXPosition += columnWidths[0];
            contentStream.newLineAtOffset(columnWidths[0], 0);

            contentStream.showText(nicRecord.getGender() != null ? nicRecord.getGender() : "");
            textXPosition += columnWidths[1];
            contentStream.newLineAtOffset(columnWidths[1], 0);

            contentStream.showText(nicRecord.getBirthDate() != null ? nicRecord.getBirthDate().toString() : "");
            textXPosition += columnWidths[2];
            contentStream.newLineAtOffset(columnWidths[2], 0);

            contentStream.showText(nicRecord.getAge() >= 0 ? String.valueOf(nicRecord.getAge()) : ""); // Default to empty string if age is negative or invalid
            textXPosition += columnWidths[3];
            contentStream.newLineAtOffset(columnWidths[3], 0);

            contentStream.showText(nicRecord.getFileName() != null ? nicRecord.getFileName() : "");

        } else if (record instanceof NicInvalid nicInvalid) {
            contentStream.showText(nicInvalid.getNic_no() != null ? nicInvalid.getNic_no() : "");
            textXPosition += columnWidths[0];
            contentStream.newLineAtOffset(columnWidths[0], 0);

            contentStream.showText(nicInvalid.getFileName() != null ? nicInvalid.getFileName() : "");
            textXPosition += columnWidths[1];
            contentStream.newLineAtOffset(columnWidths[1], 0);

            contentStream.showText(nicInvalid.getErrorMessage() != null ? nicInvalid.getErrorMessage() : ""); // Handle null for error message
        } else {
            // Log or handle unexpected record type
            System.err.println("Unexpected record type: " + record.getClass().getName());
        }
        contentStream.endText();

        yPosition -= rowHeight;
        isAlternate = !isAlternate;
    }

    return yPosition;
}





private float getTextLinesHeight(String text, float columnWidth, PDPageContentStream contentStream) throws IOException {
    if (text == null || text.isEmpty()) return 10f; // Default line height if text is empty

    float fontSize = 10f;
    PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    contentStream.setFont(font, fontSize);

    String[] lines = splitTextIntoLines(text, columnWidth, font, fontSize);
    return lines.length * (fontSize + 2); // Line height plus padding
}

private String[] splitTextIntoLines(String text, float columnWidth, PDType1Font font, float fontSize) throws IOException {
    List<String> lines = new ArrayList<>();
    if (text != null && !text.isEmpty()) {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            String testLine = line.length() > 0 ? line + " " + word : word;
            if (font.getStringWidth(testLine) / 1000 * fontSize > columnWidth) {
                lines.add(line.toString());
                line = new StringBuilder(word);
            } else {
                line.append(line.length() > 0 ? " " : "").append(word);
            }
        }
        if (line.length() > 0) {
            lines.add(line.toString());
        }
    }
    return lines.toArray(new String[0]);
}

private float getMax(float[] values) {
    float max = values[0];
    for (float value : values) {
        if (value > max) max = value;
    }
    return max;
}

private float[] calculateColumnWidths(int numColumns, float tableWidth) {
    float[] columnWidths = new float[numColumns];
    float baseWidth = tableWidth / numColumns;
    for (int i = 0; i < numColumns; i++) {
        columnWidths[i] = baseWidth;
    }
    return columnWidths;
}



    private ByteArrayInputStream generateCsvReport(boolean includeFemaleNics, boolean includeMaleNics, boolean includeTotalRecords, boolean includeInvalidRecords) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StringBuilder builder = new StringBuilder();

        builder.append("NIC Number,Gender,Birth Date,Age,File Name\n");

        List<NicRecord> records = dashService.getAllRecords();
        for (NicRecord record : records) {
            builder.append(record.getNic_no()).append(',')
                    .append(record.getGender()).append(',')
                    .append(record.getBirthDate()).append(',')
                    .append(record.getAge()).append(',')
                    .append(record.getFileName()).append('\n');
        }

        if (includeTotalRecords) {
            builder.append("Total Records,").append(dashService.getTotalRecords()).append('\n');
        }
        if (includeMaleNics) {
            builder.append("Male Users,").append(dashService.getMaleUsers()).append('\n');
        }
        if (includeFemaleNics) {
            builder.append("Female Users,").append(dashService.getFemaleUsers()).append('\n');
        }
        if (includeInvalidRecords) {
            builder.append("Invalid Records,").append(dashService.getTotalInvalidRecords()).append('\n');
        }

        try {
            out.write(builder.toString().getBytes());
        } catch (IOException e) {
            throw new ReportGenerationException("Error generating CSV report", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private ByteArrayInputStream generateXlsxReport(boolean includeFemaleNics, boolean includeMaleNics, boolean includeTotalRecords, boolean includeInvalidRecords) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("NIC Report");
    
        // Create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("NIC Number");
        header.createCell(1).setCellValue("Gender");
        header.createCell(2).setCellValue("Birth Date");
        header.createCell(3).setCellValue("Age");
        header.createCell(4).setCellValue("File Name");

        // Fetch records from the service
        List<NicRecord> records = dashService.getAllRecords();
        int rowIdx = 1;
        for (NicRecord record : records) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(record.getNic_no());
            row.createCell(1).setCellValue(record.getGender());
            row.createCell(2).setCellValue(record.getBirthDate().toString());
            row.createCell(3).setCellValue(record.getAge());
            row.createCell(4).setCellValue(record.getFileName());
        }

        int summaryRowIdx = rowIdx + 1;
        if (includeTotalRecords) {
            Row row = sheet.createRow(summaryRowIdx++);
            row.createCell(0).setCellValue("Total Records");
            row.createCell(1).setCellValue(dashService.getTotalRecords());
        }
        if (includeMaleNics) {
            Row row = sheet.createRow(summaryRowIdx++);
            row.createCell(0).setCellValue("Male Users");
            row.createCell(1).setCellValue(dashService.getMaleUsers());
        }
        if (includeFemaleNics) {
            Row row = sheet.createRow(summaryRowIdx++);
            row.createCell(0).setCellValue("Female Users");
            row.createCell(1).setCellValue(dashService.getFemaleUsers());
        }
        if (includeInvalidRecords) {
            Row row = sheet.createRow(summaryRowIdx++);
            row.createCell(0).setCellValue("Invalid Records");
            row.createCell(1).setCellValue(dashService.getTotalInvalidRecords());
        }
    
        try {
            workbook.write(baos);
            workbook.close();
        } catch (IOException e) {
            throw new ReportGenerationException("Error generating Excel report", e);
        }
    
        return new ByteArrayInputStream(baos.toByteArray());
    }
    
    
   
}

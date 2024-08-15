package com.example.dashboard_service.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import com.example.dashboard_service.model.NicRecord;
@Service
public class ReportService {

    @Autowired
    private DashService dashService;

    public ByteArrayInputStream generateReport(String format) throws IOException {
      
        try {
            System.out.println("Generating report in format: " + format);
            switch (format.toLowerCase()) {
                case "pdf" -> {
                    return generatePdfReport();
                }
                case "csv" -> {
                    return generateCsvReport();
                }
                case "xlsx" -> {
                    return generateXlsxReport();
                }
                default -> throw new IllegalArgumentException("Invalid format: " + format);
            }
        } catch (RuntimeException e) {
            throw new ReportGenerationException("Error generating report in format: " + format, e);
        }
    }

    private ByteArrayInputStream generatePdfReport() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
    
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
    
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Set font for the title
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 750);
                contentStream.showText("NIC Report");
                contentStream.endText();
    
                // Add report details
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 725);
                contentStream.showText("Total Records: " + dashService.getTotalRecords());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Male Users: " + dashService.getMaleUsers());
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText("Female Users: " + dashService.getFemaleUsers());
                contentStream.endText();
    
                // Draw table headers
                float tableYStart = 680;
                float rowHeight = 15f;
                float tableWidth = 450f;
                float yPosition = tableYStart;
    
                // Draw table border
                contentStream.setLineWidth(1f);
                contentStream.moveTo(100, yPosition);
                contentStream.lineTo(100 + tableWidth, yPosition);
                contentStream.stroke();
    
                // Draw table headers
                contentStream.beginText();
                contentStream.newLineAtOffset(100, yPosition - 12);
                contentStream.showText("NIC Number");
                contentStream.newLineAtOffset(100 + 100, yPosition - 12);
                contentStream.showText("Gender");
                contentStream.newLineAtOffset(100 + 200, yPosition - 12);
                contentStream.showText("Birth Date");
                contentStream.newLineAtOffset(100 + 300, yPosition - 12);
                contentStream.showText("Age");
                contentStream.newLineAtOffset(100 + 400, yPosition - 12);
                contentStream.showText("File Name");
                contentStream.endText();
    
                // Draw header border
                yPosition -= rowHeight;
                contentStream.moveTo(100, yPosition);
                contentStream.lineTo(100 + tableWidth, yPosition);
                contentStream.stroke();
    
                // Draw table content
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
                List<NicRecord> records = dashService.getAllRecords();
                for (NicRecord record : records) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(100, yPosition - 12);
                    contentStream.showText(record.getNic_no());
                    contentStream.newLineAtOffset(100 + 100, yPosition - 12);
                    contentStream.showText(record.getGender());
                    contentStream.newLineAtOffset(100 + 200, yPosition - 12);
                    contentStream.showText(record.getBirthDate().toString());
                    contentStream.newLineAtOffset(100 + 300, yPosition - 12);
                    contentStream.showText(String.valueOf(record.getAge()));
                    contentStream.newLineAtOffset(100 + 400, yPosition - 12);
                    contentStream.showText(record.getFileName());
                    contentStream.endText();
                    yPosition -= rowHeight;
    
                    // Draw row border
                    contentStream.moveTo(100, yPosition);
                    contentStream.lineTo(100 + tableWidth, yPosition);
                    contentStream.stroke();
                }
    
                // Draw bottom border
                contentStream.moveTo(100, yPosition);
                contentStream.lineTo(100 + tableWidth, yPosition);
                contentStream.stroke();
    
            } catch (IOException e) {
                throw new ReportGenerationException("Error generating PDF report", e);
            }
    
            try {
                document.save(out);
            } catch (IOException e) {
                throw new ReportGenerationException("Error saving PDF report", e);
            }
        } catch (IOException e) {
            throw new ReportGenerationException("Error creating PDF document", e);
        }
    
        return new ByteArrayInputStream(out.toByteArray());
    }
    
    

    private ByteArrayInputStream generateCsvReport() {
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

        try {
            out.write(builder.toString().getBytes());
        } catch (IOException e) {
            throw new ReportGenerationException("Error generating CSV report", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private ByteArrayInputStream generateXlsxReport() throws IOException {
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
    
        try {
            workbook.write(baos);
            workbook.close();
        } catch (IOException e) {
            throw new ReportGenerationException("Error generating Excel report", e);
        }
    
        return new ByteArrayInputStream(baos.toByteArray());
    }
    
    
   
}

package com.example.demo.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Service
public class PdfGenerationService {

    public byte[] generateCoverLetterPdf(String coverLetterText, String companyName, String roleName) {
        try {
            Document document = new Document(PageSize.A4, 72, 72, 90, 72);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
            Font metaFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

            Paragraph title = new Paragraph(roleName + " at " + companyName, headerFont);
            title.setSpacingAfter(2);
            document.add(title);

            Paragraph date = new Paragraph(LocalDate.now().toString(), metaFont);
            date.setSpacingAfter(20);
            document.add(date);

            String cleanText = stripMarkdown(coverLetterText);
            for (String para : cleanText.split("\n\n")) {
                String trimmed = para.trim();
                if (!trimmed.isEmpty()) {
                    Paragraph p = new Paragraph(trimmed, bodyFont);
                    p.setSpacingAfter(10);
                    p.setAlignment(Element.ALIGN_JUSTIFIED);
                    document.add(p);
                }
            }

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    private String stripMarkdown(String text) {
        return text
            .replaceAll("(?m)^#{1,6}\\s*", "")
            .replaceAll("\\*\\*(.+?)\\*\\*", "$1")
            .replaceAll("\\*(.+?)\\*", "$1")
            .replaceAll("__(.+?)__", "$1")
            .replaceAll("_(.+?)_", "$1")
            .replaceAll("(?m)^-{3,}$", "")
            .replaceAll("(?m)^[*\\-]\\s+", "")
            .trim();
    }
}

//package com.aurionpro.BankingApp.service;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.aurionpro.BankingApp.dto.TransactionDTO;
//import com.aurionpro.BankingApp.util.EmailService;
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.Paragraph;
//
//import com.itextpdf.layout.element.Table;
//
//@Service
//public class PdfService {
//
//    @Autowired private EmailService emailService;
//
//    public void sendTransactionPassbookPdf(String email, List<TransactionDTO> txs) throws IOException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PdfWriter writer = new PdfWriter(baos);
//        PdfDocument pdf = new PdfDocument(writer);
//        Document document = new Document(pdf);
//
//        document.add(new Paragraph("Bank Passbook").setBold().setFontSize(16));
//        document.add(new Paragraph("Email: " + email));
//        document.add(new Paragraph("\n"));
//
//        Table table = new Table(new float[]{2, 2, 2, 2});
//        table.addHeaderCell("Date");
//        table.addHeaderCell("Type");
//        table.addHeaderCell("Amount");
//        table.addHeaderCell("Balance After");
//
//        for (TransactionDTO t : txs) {
//            table.addCell(t.getDate().toString());
//            table.addCell(t.getTransType());
//            table.addCell(String.valueOf(t.getAmount()));
//            table.addCell(String.valueOf(t.getBalanceAfterTransaction()));
//        }
//
//        document.add(table);
//        document.close();
//
//        // send email
//        emailService.sendEmailWithAttachment(email, "Your Passbook", "Please find attached your latest passbook.", baos.toByteArray(), "passbook.pdf");
//    }
//}

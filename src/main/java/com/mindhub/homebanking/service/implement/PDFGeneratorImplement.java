package com.mindhub.homebanking.service.implement;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.service.PDFGeneratorService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class PDFGeneratorImplement implements PDFGeneratorService {
    public void export(HttpServletResponse response, List<Transaction> transactions, Account account, String start, String end) throws IOException {
        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

//      Fonts
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA, Font.DEFAULTSIZE, Font.BOLD, new Color(18, 22, 42));
        fontTitle.setSize(18);
        Font fontTableTitle = FontFactory.getFont(FontFactory.HELVETICA, Font.DEFAULTSIZE, new Color(18, 22, 42));
        fontTableTitle.setSize(12);
        Font fontBody = FontFactory.getFont(FontFactory.HELVETICA, Font.DEFAULTSIZE, new Color(18, 22, 42));
        fontBody.setSize(12);

//        Logo
        Image img = Image.getInstance("src/main/resources/static/web/assets/images/darkLogo.png");
        img.scaleAbsoluteWidth(100);
        img.scaleAbsoluteHeight(100);

        document.add(img);

//        Title
        Paragraph title = new Paragraph("MindHub Brothers", fontTitle);
        title.setAlignment(Element.ALIGN_LEFT);
        title.setSpacingAfter(10);
        document.add(title);

        Paragraph paragraph = new Paragraph("Welcome " + account.getClient().getFirstName() + " here are the transactions of your account: " + account.getNumber());
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(10);
        document.add(paragraph);

//        Table header
        PdfPTable pdfPTable = new PdfPTable(4);

        PdfPCell headerCell1 = new PdfPCell(new Paragraph("DESCRIPTION", fontTableTitle));
        PdfPCell headerCell2 = new PdfPCell(new Paragraph("DATE", fontTableTitle));
        PdfPCell headerCell3 = new PdfPCell(new Paragraph("TYPE", fontTableTitle));
        PdfPCell headerCell4 = new PdfPCell(new Paragraph("AMOUNT", fontTableTitle));

        headerCell1.setBackgroundColor(new Color(192, 192, 192));
        headerCell1.setBorder(Rectangle.TOP | Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT);
        headerCell1.setBorderColor(Color.BLACK);
        headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell1.setFixedHeight(25f);

        headerCell2.setBackgroundColor(new Color(192, 192, 192));
        headerCell2.setBorder(Rectangle.TOP | Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT);
        headerCell2.setBorderColor(Color.BLACK);
        headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell2.setFixedHeight(25f);

        headerCell3.setBackgroundColor(new Color(192, 192, 192));
        headerCell3.setBorder(Rectangle.TOP | Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT);
        headerCell3.setBorderColor(Color.BLACK);
        headerCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell3.setFixedHeight(25f);

        headerCell4.setBackgroundColor(new Color(192, 192, 192));
        headerCell4.setBorder(Rectangle.TOP | Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT);
        headerCell4.setBorderColor(Color.BLACK);
        headerCell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell4.setFixedHeight(25f);

        pdfPTable.addCell(headerCell1);
        pdfPTable.addCell(headerCell2);
        pdfPTable.addCell(headerCell3);
        pdfPTable.addCell(headerCell4);

//        Table body
        try{
            for (int i = transactions.size() - 1; i >= 0; i--){
                PdfPCell pdfPCell5 = new PdfPCell(new Paragraph(transactions.get(i).getDescription(), fontBody));
                PdfPCell pdfPCell6 = new PdfPCell(new Paragraph(transactions.get(i).getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), fontBody));
                PdfPCell pdfPCell7 = new PdfPCell(new Paragraph(String.valueOf(transactions.get(i).getType()), fontBody));
                PdfPCell pdfPCell8 = new PdfPCell(new Paragraph("$" + String.valueOf(transactions.get(i).getAmount()), fontBody));

//            Horizontal & vertical alignment
                pdfPCell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell5.setVerticalAlignment(Element.ALIGN_CENTER);
                pdfPCell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell6.setVerticalAlignment(Element.ALIGN_CENTER);
                pdfPCell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell7.setVerticalAlignment(Element.ALIGN_CENTER);
                pdfPCell8.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell8.setVerticalAlignment(Element.ALIGN_CENTER);

//            Border
                pdfPCell5.setBorder(Rectangle.TOP | Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT);
                pdfPCell5.setBorderColor(Color.BLACK);
                pdfPCell5.setFixedHeight(40f);

                pdfPCell6.setBorder(Rectangle.TOP | Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT);
                pdfPCell6.setBorderColor(Color.BLACK);
                pdfPCell6.setFixedHeight(40f);

                pdfPCell7.setBorder(Rectangle.TOP | Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT);
                pdfPCell7.setBorderColor(Color.BLACK);
                pdfPCell7.setFixedHeight(40f);

                pdfPCell8.setBorder(Rectangle.TOP | Rectangle.BOTTOM | Rectangle.LEFT | Rectangle.RIGHT);
                pdfPCell8.setBorderColor(Color.BLACK);
                pdfPCell8.setFixedHeight(40f);

//            Adding cell to the table
                pdfPTable.addCell(pdfPCell5);
                pdfPTable.addCell(pdfPCell6);
                pdfPTable.addCell(pdfPCell7);
                pdfPTable.addCell(pdfPCell8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        document.add(pdfPTable);

//        Date
        if (start.equals("all") || end.equals("all")){
            Paragraph date = new Paragraph("All transaction history", fontBody);
            date.setAlignment(Element.ALIGN_LEFT);
            date.setSpacingAfter(10);
            document.add(date);
        } else {
            Paragraph date = new Paragraph("From " + start.substring(0,10) + " to " + end.substring(0,10), fontBody);
            date.setAlignment(Element.ALIGN_LEFT);
            date.setSpacingAfter(10);
            document.add(date);
        }


        document.close();
    }

}

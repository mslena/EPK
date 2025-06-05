package org.example.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class ReportController {

    @GetMapping("/reports")
    public ModelAndView user(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("reports");
        mav.addObject("requestURI", request.getRequestURI());
        return mav;
    }

    @PostMapping("/reports/download")
    public void downloadExcelReport(
            @RequestParam String reportType,
            @RequestParam String period,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpServletResponse response) throws IOException {

        // Создание Excel книги
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Отчёт");

        // Заголовки
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Месяц");
        header.createCell(1).setCellValue(reportType.equals("sales") ? "Продажи" : "Остатки");

        // Примерные данные
        String[] months = {"Янв", "Фев", "Мар", "Апр", "Май"};
        int[] data = {120, 150, 180, 90, 200};

        for (int i = 0; i < months.length; i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(months[i]);
            row.createCell(1).setCellValue(data[i]);
        }

        // Настройки HTTP-ответа
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "report_" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        // Запись в поток
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}

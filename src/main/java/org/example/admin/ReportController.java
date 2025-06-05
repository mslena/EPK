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

        @GetMapping("/accounting")
        public ModelAndView accounting(HttpServletRequest request) {
            ModelAndView mav = new ModelAndView("accounting");
            mav.addObject("requestURI", request.getRequestURI());
            return mav;
        }

        @GetMapping("/reports")
        public ModelAndView reports(HttpServletRequest request) {
            ModelAndView mav = new ModelAndView("reports");
            mav.addObject("requestURI", request.getRequestURI());
            return mav;
        }

        @PostMapping("/reports/download")
        public void downloadExcelReport(
                @RequestParam String reportType,
                @RequestParam String period, @RequestParam String year,
                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                HttpServletResponse response) throws IOException {

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Отчёт");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Период");
            header.createCell(1).setCellValue(reportType.equals("sales") ? "Продажи" : "Остатки");

            int rowIndex = 1;

            if ("month".equals(period)) {
                LocalDate today = LocalDate.now();
                LocalDate firstDay = today.withDayOfMonth(1);
                int days = today.lengthOfMonth();

                for (int i = 0; i < days; i++) {
                    LocalDate date = firstDay.plusDays(i);
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(date.toString());
                    row.createCell(1).setCellValue(getRandomValue(reportType));
                }

            } else if ("year".equals(period)) {
                    for (int month = 1; month <= 12; month++) {
                        Row row = sheet.createRow(rowIndex++);
                        row.createCell(0).setCellValue(year + "-" + String.format("%02d", month));
                        row.createCell(1).setCellValue(getRandomValue(reportType));
                    }


            } else if ("quarter".equals(period)) {

                    for (int q = 1; q <= 4; q++) {
                        Row row = sheet.createRow(rowIndex++);
                        row.createCell(0).setCellValue("Quarter" + q + " " + year);
                        row.createCell(1).setCellValue(getRandomValue(reportType));
                    }

            } else if ("fixed".equals(period)) {
                LocalDate fixedDate = LocalDate.of(2025, 5, 1);
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(fixedDate.toString());
                row.createCell(1).setCellValue(getRandomValue(reportType));
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String fileName = "report_" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            workbook.write(response.getOutputStream());
            workbook.close();
        }

        private int getRandomValue(String type) {
            return switch (type) {
                case "sales" -> (int) (100 + Math.random() * 500);
                case "stock" -> (int) (50 + Math.random() * 1000);
                default -> 0;
            };
        }

    }

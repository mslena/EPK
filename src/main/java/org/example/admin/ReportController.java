package org.example.admin;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
public class ReportController {

    /*@GetMapping("/reports/generate")
    public String generateReport(@RequestParam String reportType,
                                 @RequestParam String period,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                 Model model) {
        // Получи данные по параметрам
        List<ReportData> data = reportService.getReportData(reportType, period, startDate, endDate);
        model.addAttribute("chartData", data); // используешь в JSON для Chart.js
        return "reports";
    }

    @PostMapping("/reports/download")
    public void downloadExcelReport(@RequestParam String reportType,
                                    @RequestParam String period,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                    HttpServletResponse response) throws IOException {
        Workbook workbook = reportService.generateExcel(reportType, period, startDate, endDate);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=report.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }*/

    @GetMapping("/reports")
    public ModelAndView user(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("reports");
        mav.addObject("requestURI", request.getRequestURI());
        return mav;
    }

}

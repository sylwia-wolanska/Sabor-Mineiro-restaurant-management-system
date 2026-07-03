package pjatk.mas.dto.response;

import java.math.BigDecimal;

public record EmployeeSummaryDTO(
        Long id,
        String name,
        String surname,
        String role,
        BigDecimal salaryPerHour
) {}
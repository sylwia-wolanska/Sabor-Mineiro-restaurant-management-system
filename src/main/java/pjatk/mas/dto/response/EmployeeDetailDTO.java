package pjatk.mas.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record EmployeeDetailDTO(
        Long id,
        String name,
        String surname,
        String role,
        BigDecimal salaryPerHour,
        List<String> extraInfo
) {}
package payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO used to receive employee data from REST request bodies.
 *
 * Decouples the API surface from the JPA entity, preventing
 * mass-assignment of internal fields like `id` or `createdBy`.
 *
 * Example JSON payload:
 * {
 *   "name": "Amarpa Keerthi Kumar",
 *   "profileImage": "ellipse-1.png",
 *   "gender": "Female",
 *   "departments": ["Sales", "HR", "Finance"],
 *   "salary": 10000.00,
 *   "startDate": "2019-10-29",
 *   "notes": "Senior specialist account manager."
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private String name;
    private String profileImage;
    private String gender;
    private List<String> departments;
    private BigDecimal salary;
    private LocalDate startDate;
    private String notes;
}

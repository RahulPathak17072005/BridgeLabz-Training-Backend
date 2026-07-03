package payroll.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * JPA Entity representing an employee payroll record.
 *
 * Maps to the "employees" table plus the "employee_departments"
 * junction table (via @ElementCollection) to support
 * multi-valued department selections in 1NF.
 */
@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** Full display name. Max 100 chars. */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Profile image filename (e.g. "ellipse-1.png").
     * Stored as a filename reference, not binary blob.
     */
    @Column(name = "profile_image", nullable = false, length = 100)
    private String profileImage;

    /** Gender: Male or Female. */
    @Column(nullable = false, length = 10)
    private String gender;

    /**
     * Multi-valued department list (HR, Sales, Finance, Engineer, Others).
     * Stored in a separate "employee_departments" junction table
     * with a composite PK on (employee_id, department).
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "employee_departments",
            joinColumns = @JoinColumn(name = "employee_id")
    )
    @Column(name = "department")
    private List<String> departments;

    /** Monthly salary. Stored as NUMERIC(10,2). */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salary;

    /** Employment start date. */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /** Optional free-text notes about the employee. */
    @Column(columnDefinition = "TEXT")
    private String notes;

    /**
     * Foreign key reference to the user who created this record.
     * Populated automatically from the JWT authentication context.
     */
    @Column(name = "created_by")
    private int createdBy;
}

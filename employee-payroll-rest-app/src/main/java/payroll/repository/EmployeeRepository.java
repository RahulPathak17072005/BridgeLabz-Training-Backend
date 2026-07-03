package payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import payroll.model.Employee;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for Employee entity.
 *
 * Extends JpaRepository to inherit standard CRUD methods:
 *   save(), findById(), findAll(), deleteById(), etc.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    /**
     * Finds all employees created by a specific user (by userId FK).
     * Derived query → SELECT * FROM employees WHERE created_by = ?
     */
    List<Employee> findByCreatedBy(int userId);

    /**
     * Finds a single employee record created by a specific user.
     * Used for ownership checks before edit/delete.
     */
    Optional<Employee> findByIdAndCreatedBy(int id, int userId);

    /**
     * Calculates the total payroll for a specific department.
     *
     * Uses a native H2 SQL query joining employees with the
     * employee_departments junction table to SUM salaries
     * where department matches the parameter.
     *
     * Note: H2 does not support stored procedures, so we use
     * a native inline aggregate query here.
     */
    @Query(value = """
        SELECT COALESCE(SUM(e.salary), 0)
        FROM employees e
        JOIN employee_departments ed ON e.id = ed.employee_id
        WHERE ed.department = :dept
        """, nativeQuery = true)
    BigDecimal getDeptPayroll(@Param("dept") String dept);
}

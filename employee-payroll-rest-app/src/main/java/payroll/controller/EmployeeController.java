package payroll.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import payroll.dto.EmployeeDTO;
import payroll.model.Employee;
import payroll.model.User;
import payroll.repository.EmployeeRepository;
import payroll.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Employee Payroll REST Controller.
 *
 * Base path: /api/employees
 * All endpoints require a valid Bearer JWT token.
 *
 * Endpoints:
 *   POST   /api/employees              → Add new employee
 *   GET    /api/employees              → Get all employees
 *   GET    /api/employees/{id}         → Get employee by ID
 *   PUT    /api/employees/{id}         → Update employee by ID
 *   DELETE /api/employees/{id}         → Delete employee by ID
 *   GET    /api/employees/summary      → Department payroll total (ADMIN only)
 *
 * Role-based access:
 *   ADMIN → Full CRUD + summary
 *   USER  → Read-only (GET endpoints only)
 */
@RestController
@RequestMapping("/api/employees")
@Slf4j
@Tag(name = "Employees", description = "Employee CRUD and payroll summary endpoints")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    // ─── Helper: Get Authenticated User ──────────────────────────────────────

    /**
     * Retrieves the currently authenticated User entity from the database.
     *
     * Uses the username stored in the SecurityContext (set by JwtRequestFilter)
     * to look up the full User record including the numeric ID needed for
     * the createdBy foreign key on Employee records.
     */
    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found: " + username));
    }

    // ─── POST /api/employees ──────────────────────────────────────────────────

    /**
     * Creates a new employee payroll record.
     *
     * Maps EmployeeDTO fields to a new Employee entity.
     * Sets createdBy = authenticated user's ID.
     * Returns 201 Created with the persisted employee (including generated ID).
     *
     * Example request body:
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
    @Operation(summary = "Add a new employee record")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addEmployee(@RequestBody EmployeeDTO dto) {
        User authUser = getAuthenticatedUser();
        log.info("User [{}] adding new employee: {}", authUser.getUsername(), dto.getName());

        Employee employee = Employee.builder()
                .name(dto.getName())
                .profileImage(dto.getProfileImage())
                .gender(dto.getGender())
                .departments(dto.getDepartments())
                .salary(dto.getSalary())
                .startDate(dto.getStartDate())
                .notes(dto.getNotes())
                .createdBy(authUser.getId())
                .build();

        Employee saved = employeeRepository.save(employee);
        log.debug("Employee saved with ID: {}", saved.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ─── GET /api/employees ───────────────────────────────────────────────────

    /**
     * Returns all employee records in the database.
     * ADMINs see all employees; USERs also see all (read-only).
     */
    @Operation(summary = "Get all employees")
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("Fetching all employees");
        List<Employee> employees = employeeRepository.findAll();
        return ResponseEntity.ok(employees);
    }

    // ─── GET /api/employees/{id} ──────────────────────────────────────────────

    /**
     * Returns a single employee by their database ID.
     * Returns 404 Not Found if the ID does not exist.
     */
    @Operation(summary = "Get employee by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable int id) {
        log.info("Fetching employee with ID: {}", id);

        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            log.warn("Employee ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employee.get());
    }

    // ─── PUT /api/employees/{id} ──────────────────────────────────────────────

    /**
     * Updates an existing employee record by ID.
     *
     * Only ADMIN users can edit employees.
     * Applies partial update: only non-null fields in the DTO are updated.
     * Returns 404 if the employee does not exist.
     *
     * Example request body (partial update):
     * {
     *   "salary": 12500.00,
     *   "notes": "Updated salary for account specialist."
     * }
     */
    @Operation(summary = "Update an employee record by ID")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editEmployee(@PathVariable int id, @RequestBody EmployeeDTO dto) {
        log.info("Updating employee ID: {}", id);

        Optional<Employee> existing = employeeRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Employee record with ID " + id + " does not exist"));
        }

        Employee employee = existing.get();

        // Apply partial updates only if fields are provided in the request
        if (dto.getName() != null)         employee.setName(dto.getName());
        if (dto.getProfileImage() != null) employee.setProfileImage(dto.getProfileImage());
        if (dto.getGender() != null)       employee.setGender(dto.getGender());
        if (dto.getDepartments() != null)  employee.setDepartments(dto.getDepartments());
        if (dto.getSalary() != null)       employee.setSalary(dto.getSalary());
        if (dto.getStartDate() != null)    employee.setStartDate(dto.getStartDate());
        if (dto.getNotes() != null)        employee.setNotes(dto.getNotes());

        Employee updated = employeeRepository.save(employee);
        log.debug("Employee ID {} updated successfully", id);

        return ResponseEntity.ok(updated);
    }

    // ─── DELETE /api/employees/{id} ───────────────────────────────────────────

    /**
     * Deletes an employee record by ID.
     *
     * Only ADMIN users can delete employees.
     * Returns 404 if the ID does not exist.
     * Returns 200 OK with success message on deletion.
     *
     * Note: PDF spec shows 200 with message body.
     * REST purists would use 204 No Content — both are valid.
     */
    @Operation(summary = "Delete an employee record by ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteEmployee(@PathVariable int id) {
        log.info("Deleting employee ID: {}", id);

        if (!employeeRepository.existsById(id)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Employee record with ID " + id + " does not exist"));
        }

        employeeRepository.deleteById(id);
        log.debug("Employee ID {} deleted", id);

        return ResponseEntity.ok("Employee profile deleted");
    }

    // ─── GET /api/employees/summary ───────────────────────────────────────────

    /**
     * Returns the total payroll sum for a specific department.
     *
     * ADMIN only endpoint (enforced by SecurityConfig + @PreAuthorize).
     * Uses a native SQL aggregate query in EmployeeRepository.
     *
     * Example: GET /api/employees/summary?department=HR
     * Response: 10000.00
     *
     * Returns 0.00 if no employees exist in the department.
     */
    @Operation(summary = "Get total payroll sum for a department (ADMIN only)")
    @GetMapping("/summary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BigDecimal> getDeptPayrollSummary(@RequestParam String department) {
        log.info("Calculating payroll summary for department: {}", department);

        BigDecimal total = employeeRepository.getDeptPayroll(department);
        if (total == null) total = BigDecimal.ZERO;

        return ResponseEntity.ok(total);
    }
}

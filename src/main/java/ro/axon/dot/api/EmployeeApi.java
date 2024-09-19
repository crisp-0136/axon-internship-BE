package ro.axon.dot.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.axon.dot.exception.BusinessErrorCode;
import ro.axon.dot.exception.BusinessException;
import ro.axon.dot.service.EmployeeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeApi {

    private final EmployeeService employeeService;

    @PatchMapping("/{employeeId}/inactivate")
    public ResponseEntity<Void> inactivateEmployee(@PathVariable String employeeId) {

        boolean isInactivated = employeeService.inactivateEmployee(employeeId);

        if (isInactivated) {
            return ResponseEntity.noContent().build();
        } else {
            throw new BusinessException(BusinessErrorCode.EMPLOYEE_NOT_FOUND);
        }
    }
}

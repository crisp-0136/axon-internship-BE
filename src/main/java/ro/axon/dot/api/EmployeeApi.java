package ro.axon.dot.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.axon.dot.service.EmployeeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeApi {

    private final EmployeeService employeeService;

    @PatchMapping("/{employeeId}/inactivate")
    public ResponseEntity<Void> inactivateEmployee(@PathVariable String employeeId) {

        employeeService.inactivateEmployee(employeeId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{employeeId}/requests/{requestId}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable String employeeId, @PathVariable Long requestId) {

        employeeService.deleteLeaveRequest(employeeId, requestId);

        return ResponseEntity.noContent().build();
    }
}

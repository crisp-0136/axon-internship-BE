package ro.axon.dot.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.axon.dot.model.*;
import ro.axon.dot.service.EmployeeService;
import ro.axon.dot.service.LeaveReqService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeApi {

    private final EmployeeService employeeService;
    private final LeaveReqService leaveReqService;

    @PatchMapping("/{employeeId}/inactivate")
    public ResponseEntity<Void> inactivateEmployee(@PathVariable String employeeId) {

        employeeService.inactivateEmployee(employeeId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("")
    public ResponseEntity<Void> addEmployee(@RequestBody AddEmployeeDto employeeDto) {

        employeeService.addEmployee(employeeDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PatchMapping("/{employeeId}")
    public ResponseEntity<Void> updateEmployee(@PathVariable String employeeId,
                                               @RequestBody UpdateEmployeeDto updateEmployeeDto){

        employeeService.updateEmployee(employeeId, updateEmployeeDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{employeeId}/requests/{requestId}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable String employeeId, @PathVariable Long requestId) {

        leaveReqService.deleteLeaveRequest(employeeId, requestId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{employeeId}/requests")
    public ResponseEntity<?> addLeaveRequest(@PathVariable String employeeId,
                                             @RequestBody LeaveReqDto leaveReqDto) {
        leaveReqService.addLeaveRequest(employeeId, leaveReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{employeeId}/requests")
    public LeaveReqDetailsList getLeaveRequests(
            @PathVariable String employeeId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        return leaveReqService.getLeaveRequests(employeeId, startDate, endDate);
    }

    @PutMapping("/{employeeId}/requests/{requestId}")
    public ResponseEntity<Void> updateLeaveRequest(@PathVariable String employeeId, @PathVariable Long requestId,
                                                   @Valid @RequestBody UpdateLeaveReqDTO updateLeaveReqDTO) {

        leaveReqService.updateLeaveRequest(employeeId, requestId, updateLeaveReqDTO);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getEmployees(@RequestParam(required = false) String name) {
        List<EmployeeDto> employees = employeeService.getEmployees(name);
        return ResponseEntity.ok(employees);
    }
}

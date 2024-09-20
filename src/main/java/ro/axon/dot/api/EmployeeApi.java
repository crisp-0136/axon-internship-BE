package ro.axon.dot.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.axon.dot.domain.EmployeeEty;
import ro.axon.dot.domain.repositories.EmployeeRepository;
import ro.axon.dot.domain.repositories.TeamRepository;
import ro.axon.dot.exception.BusinessErrorCode;
import ro.axon.dot.exception.BusinessException;
import ro.axon.dot.model.AddEmployeeDto;
import ro.axon.dot.model.UpdateEmployeeDto;
import ro.axon.dot.service.EmployeeService;

import javax.validation.Valid;

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

}

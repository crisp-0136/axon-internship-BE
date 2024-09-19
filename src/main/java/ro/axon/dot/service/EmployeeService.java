package ro.axon.dot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.axon.dot.domain.EmployeeEty;
import ro.axon.dot.domain.enums.Status;
import ro.axon.dot.domain.repositories.EmployeeRepository;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public boolean inactivateEmployee(String id) {
        Optional<EmployeeEty> optionalEmployeeEty = employeeRepository.findById(id);

        if(optionalEmployeeEty.isPresent()) {
            EmployeeEty employeeEty = optionalEmployeeEty.get();

            employeeEty.setStatus(Status.INACTIVE);
            employeeEty.setMdfUsr("initial.load");
            employeeEty.setMdfTms(Instant.now());

            employeeRepository.save(employeeEty);

            return true;
        }
        return false;
    }
}

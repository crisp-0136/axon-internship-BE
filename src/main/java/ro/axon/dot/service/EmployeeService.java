package ro.axon.dot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.axon.dot.domain.EmpYearlyDaysOffEty;
import ro.axon.dot.domain.EmpYearlyDaysOffHistEty;
import ro.axon.dot.domain.EmployeeEty;
import ro.axon.dot.domain.TeamEty;
import ro.axon.dot.domain.enums.EmpYearlyDaysOffHistType;
import ro.axon.dot.domain.enums.Status;
import ro.axon.dot.domain.repositories.EmpYearlyDaysOffHistRepository;
import ro.axon.dot.domain.repositories.EmpYearlyDaysOffRepository;
import ro.axon.dot.domain.repositories.EmployeeRepository;
import ro.axon.dot.domain.repositories.TeamRepository;
import ro.axon.dot.exception.BusinessErrorCode;
import ro.axon.dot.exception.BusinessException;
import ro.axon.dot.mapper.EmployeeMapper;
import ro.axon.dot.model.AddEmployeeDto;
import ro.axon.dot.model.UpdateEmployeeDto;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final TeamRepository teamRepository;
    private final EmpYearlyDaysOffRepository empYearlyDaysOffRepository;
    private final EmpYearlyDaysOffHistRepository empYearlyDaysOffHistRepository;

    @Transactional
    public void inactivateEmployee(String id) {
        Optional<EmployeeEty> optionalEmployeeEty = employeeRepository.findById(id);

        if(optionalEmployeeEty.isPresent()) {
            EmployeeEty employeeEty = optionalEmployeeEty.get();

            employeeEty.setStatus(Status.INACTIVE);
            employeeEty.setMdfUsr("initial.load");
            employeeEty.setMdfTms(LocalDate.now());

            employeeRepository.save(employeeEty);
        }
        else {
            throw new BusinessException(BusinessErrorCode.EMPLOYEE_INACTIVATION_FAILURE);
        }
    }

    @Transactional
    public void addEmployee(AddEmployeeDto employeeDto) {

        if (!teamRepository.existsById(employeeDto.getTeamId())) {
            throw new BusinessException(BusinessErrorCode.TEAM_NOT_FOUND);
        }
        else {
            EmployeeEty employeeEty = employeeMapper.dtoToEntity(employeeDto);

            TeamEty teamEty = teamRepository.findById(employeeDto.getTeamId()).orElseThrow(()
                    -> new EntityNotFoundException("Team not found"));

            employeeEty.setTeam(teamEty);

            employeeEty.setCrtUsr("test");
            employeeEty.setCrtTms(LocalDate.now());
            employeeEty.setMdfUsr("test");
            employeeEty.setMdfTms(LocalDate.now());
            employeeEty.setPassword("test");
            employeeEty.setStatus(Status.ACTIVE);
            employeeEty.setContractStartDate(LocalDate.now());
            LocalDate contractEndDate = LocalDate.of(2099, 12, 31);
            employeeEty.setContractEndDate(LocalDate.from(contractEndDate));
            employeeRepository.save(employeeEty);

            EmpYearlyDaysOffEty empYearlyDaysOffEty = new EmpYearlyDaysOffEty();
            empYearlyDaysOffEty.setEmployeeEty(employeeEty);
            empYearlyDaysOffEty.setTotalNoDays(employeeDto.getNoDaysOff());
            empYearlyDaysOffEty.setYear(LocalDate.now().getYear());

            EmpYearlyDaysOffHistEty historyEntry = new EmpYearlyDaysOffHistEty();
            historyEntry.setNoDays(employeeDto.getNoDaysOff());
            historyEntry.setDescription("Initial");
            historyEntry.setType(EmpYearlyDaysOffHistType.INCREASE);
            historyEntry.setCrtUsr("test");
            historyEntry.setCrtTms(LocalDate.now());
            historyEntry.setEmpYearlyDaysOffEty(empYearlyDaysOffEty);

            empYearlyDaysOffEty.getHistoryRecords().add(historyEntry);

            empYearlyDaysOffRepository.save(empYearlyDaysOffEty);
        }
    }

    @Transactional
    public void updateEmployee(String employeeId, @Valid UpdateEmployeeDto updateEmployeeDto) {

        EmployeeEty employeeEty = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.EMPLOYEE_NOT_FOUND));

        if (!teamRepository.existsById(updateEmployeeDto.getTeamId())) {
            throw new BusinessException(BusinessErrorCode.TEAM_NOT_FOUND);
        }

        if (updateEmployeeDto.getV() < employeeEty.getV()) {
            throw new BusinessException(BusinessErrorCode.INVALID_DB_VERSION);
        }

        TeamEty teamEty = teamRepository.findById(updateEmployeeDto.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid teamId"));

        employeeEty.setFirstName(updateEmployeeDto.getFirstName());
        employeeEty.setLastName(updateEmployeeDto.getLastName());
        employeeEty.setEmail(updateEmployeeDto.getEmail());
        employeeEty.setRole(updateEmployeeDto.getRole());
        employeeEty.setTeam(teamEty);

        employeeRepository.save(employeeEty);
    }

}

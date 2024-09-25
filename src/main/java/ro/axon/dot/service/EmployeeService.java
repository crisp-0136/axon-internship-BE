package ro.axon.dot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.axon.dot.domain.*;
import ro.axon.dot.domain.enums.EmpYearlyDaysOffHistType;
import ro.axon.dot.domain.enums.LeaveRequestStatus;
import ro.axon.dot.domain.enums.LeaveRequestType;
import ro.axon.dot.domain.enums.Status;
import ro.axon.dot.domain.repositories.*;
import ro.axon.dot.exception.BusinessErrorCode;
import ro.axon.dot.exception.BusinessException;
import ro.axon.dot.mapper.EmployeeMapper;
import ro.axon.dot.model.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final TeamRepository teamRepository;
    private final EmpYearlyDaysOffRepository empYearlyDaysOffRepository;
    private final EmpYearlyDaysOffHistRepository empYearlyDaysOffHistRepository;
    private final LeaveReqRepository leaveReqRepository;

    @Transactional
    public void inactivateEmployee(String id) {
        Optional<EmployeeEty> optionalEmployeeEty = employeeRepository.findById(id);

        if(optionalEmployeeEty.isPresent()) {
            EmployeeEty employeeEty = optionalEmployeeEty.get();

            employeeEty.setStatus(Status.INACTIVE);
            employeeEty.setMdfUsr("initial.load");
            employeeEty.setMdfTms(Instant.now());

            employeeRepository.save(employeeEty);
        }
        else {
            throw new BusinessException(BusinessErrorCode.EMPLOYEE_NOT_FOUND);
        }
    }

    @Transactional
    public void addEmployee(AddEmployeeDto employeeDto) {

        TeamEty teamEty = teamRepository.findById(employeeDto.getTeamId())
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.TEAM_NOT_FOUND));

        EmployeeEty employeeEty = employeeMapper.dtoToEntity(employeeDto);

        employeeEty.setTeam(teamEty);

        employeeEty.setCrtUsr("test");
        employeeEty.setCrtTms(Instant.now());
        employeeEty.setMdfUsr("test");
        employeeEty.setMdfTms(Instant.now());

        String password = employeeDto.getFirstName() + "-" + employeeDto.getLastName() + "-" + "2024";
        String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        employeeEty.setPassword(hashPassword);

        employeeEty.setStatus(Status.ACTIVE);
        employeeEty.setContractStartDate(employeeDto.getContractStartDate());
        LocalDate contractEndDate = LocalDate.of(2099, 12, 31);
        employeeEty.setContractEndDate(LocalDate.from(contractEndDate));

        EmpYearlyDaysOffEty empYearlyDaysOffEty = new EmpYearlyDaysOffEty();
        empYearlyDaysOffEty.setEmployeeEty(employeeEty);
        empYearlyDaysOffEty.setTotalNoDays(employeeDto.getNoDaysOff());
        empYearlyDaysOffEty.setYear(LocalDate.now().getYear());
        employeeEty.getEmpYearlyDaysOffEties().add(empYearlyDaysOffEty);

        EmpYearlyDaysOffHistEty historyEntry = new EmpYearlyDaysOffHistEty();
        historyEntry.setNoDays(employeeDto.getNoDaysOff());
        historyEntry.setDescription("Initial");
        historyEntry.setType(EmpYearlyDaysOffHistType.INCREASE);
        historyEntry.setCrtUsr("test");
        historyEntry.setCrtTms(Instant.now());
        historyEntry.setEmpYearlyDaysOffEty(empYearlyDaysOffEty);

        empYearlyDaysOffEty.getHistoryRecords().add(historyEntry);

        employeeRepository.save(employeeEty);

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
        employeeEty.setMdfUsr("update");
        employeeEty.setMdfTms(Instant.now());

        employeeRepository.save(employeeEty);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDto> getEmployees(String name) {
        List<EmployeeEty> employees = employeeRepository.findByNameIgnoreCase(name);
        return employees.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private EmployeeDto convertToDTO(EmployeeEty employee) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setCrtUsr(employee.getCrtUsr());
        dto.setCrtTms(employee.getCrtTms());
        dto.setMdfUsr(employee.getMdfUsr());
        dto.setMdfTms(employee.getMdfTms());
        dto.setRole(employee.getRole());
        dto.setStatus(employee.getStatus());
        dto.setContractStartDate(employee.getContractStartDate());
        dto.setV(employee.getV());
        dto.setTotalVacationDays(
                employee.getEmpYearlyDaysOffEties().stream()
                        .filter(ety -> ety.getYear() == Year.now().getValue())
                        .findFirst().map(EmpYearlyDaysOffEty::getTotalNoDays).orElse(0));
        dto.setUsername(employee.getUsername());

        if (employee.getTeam() != null) {
            TeamDto teamDTO = new TeamDto();
            teamDTO.setId(employee.getTeam().getId());
            teamDTO.setName(employee.getTeam().getName());
            teamDTO.setCrtUsr(employee.getTeam().getCrtUsr());
            teamDTO.setCrtTms(employee.getTeam().getCrtTms());
            teamDTO.setMdfUsr(employee.getTeam().getMdfUsr());
            teamDTO.setMdfTms(employee.getTeam().getMdfTms());
            teamDTO.setStatus(employee.getTeam().getStatus());

            dto.setTeamDetails(teamDTO);
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public RemainingDaysOffDto getRemainingDaysOff(String employeeId) {

        var employeeEty = employeeRepository.findById(employeeId).orElseThrow(()
                -> new BusinessException(BusinessErrorCode.EMPLOYEE_NOT_FOUND));

        var freeDays = employeeEty.getEmpYearlyDaysOffEties().stream()
                .filter(daysOff -> daysOff.getYear() == LocalDate.now().getYear()).findFirst()
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.DAYS_OFF_NOT_FOUND));

        var usedDays = employeeEty.getLeaveRequestEties().stream()
                .filter(req -> req.getStatus() == LeaveRequestStatus.APPROVED ||
                        req.getStatus() == LeaveRequestStatus.PENDING)
                .filter(req -> req.getStartDate().getYear() == LocalDate.now().getYear())
                .mapToInt(LeaveReqEty::getNoDays).sum();

        RemainingDaysOffDto remainingDays = new RemainingDaysOffDto();
        remainingDays.setRemainingDays(freeDays.getTotalNoDays() - usedDays);

        return remainingDays;
    }
}

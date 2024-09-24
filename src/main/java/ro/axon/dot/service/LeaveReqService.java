package ro.axon.dot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.axon.dot.domain.EmpYearlyDaysOffEty;
import ro.axon.dot.domain.EmployeeEty;
import ro.axon.dot.domain.LeaveReqEty;
import ro.axon.dot.domain.enums.LeaveRequestStatus;
import ro.axon.dot.domain.repositories.*;
import ro.axon.dot.exception.BusinessErrorCode;
import ro.axon.dot.exception.BusinessException;
import ro.axon.dot.mapper.LeaveReqMapper;
import ro.axon.dot.model.LeaveReqDetailsList;
import ro.axon.dot.model.LeaveReqDto;
import ro.axon.dot.domain.LegallyDaysOffEty;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveReqService {
    private final EmployeeRepository employeeRepository;
    private final LeaveReqRepository leaveRequestRepository;
    private final EmpYearlyDaysOffRepository empYearlyDaysOffRepository;
    private final LegallyDaysOffRepository legallyDaysOffRepository;
    private final LeaveReqMapper leaveReqMapper;

    @Transactional
    public void addLeaveRequest(String employeeId, LeaveReqDto leaveReqDto) {
        EmployeeEty employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.EMPLOYEE_NOT_FOUND));

        validateLeaveRequest(employee, leaveReqDto);

        EmpYearlyDaysOffEty yearlyDaysOff = empYearlyDaysOffRepository
                .findByEmployeeEtyAndYear(employee, leaveReqDto.getStartDate().getYear())
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.DAYS_OFF_NOT_FOUND));


        int effectiveLeaveDays = calculateEffectiveDaysRequested(leaveReqDto);
        long totalDaysTaken = calculateTotalDaysTaken(employee);

        if (effectiveLeaveDays + totalDaysTaken > yearlyDaysOff.getTotalNoDays()) {
            throw new BusinessException(BusinessErrorCode.INSUFFICIENT_DAYS_OFF);
        }

        LeaveReqEty leaveRequest = leaveReqMapper.toEntity(leaveReqDto);

        leaveRequest.setEmployeeEty(employee);
        leaveRequest.setStatus(LeaveRequestStatus.PENDING);
        leaveRequest.setCrtUsr(employee.getUsername());
        leaveRequest.setCrtTms(Instant.now());
        leaveRequest.setMdfUsr(employee.getUsername());
        leaveRequest.setMdfTms(Instant.now());
        leaveRequest.setNoDays(effectiveLeaveDays);

        employee.getLeaveRequestEties().add(leaveRequest);

        leaveRequestRepository.save(leaveRequest);
    }

    @Transactional
    public LeaveReqDetailsList getLeaveRequests(String employeeId, LocalDate startDate, LocalDate endDate) {

        EmployeeEty employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.EMPLOYEE_NOT_FOUND));

        List<LeaveReqEty> leaveRequests = leaveRequestRepository.findByEmployeeEty(employee);

        if (startDate != null) {
            leaveRequests = leaveRequests.stream()
                    .filter(req -> !req.getStartDate().isBefore(startDate))
                    .collect(Collectors.toList());
        }
        if (endDate != null) {
            leaveRequests = leaveRequests.stream()
                    .filter(req -> !req.getEndDate().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        LeaveReqDetailsList leaveReqDetailsList = new LeaveReqDetailsList();
        leaveReqDetailsList.setItems(leaveRequests.stream()
                .map(leaveReqMapper::toDto)
                .collect(Collectors.toList()));

        return leaveReqDetailsList;
    }


    private void validateLeaveRequest(EmployeeEty employeeEty, LeaveReqDto leaveReqDto) {
        if (leaveReqDto.getStartDate().getYear() != leaveReqDto.getEndDate().getYear()) {
            throw new BusinessException(BusinessErrorCode.LEAVE_DIFFERENT_YEARS);
        }

        if (leaveReqDto.getEndDate().isBefore(leaveReqDto.getStartDate())) {
            throw new BusinessException(BusinessErrorCode.END_DATE_BEFORE_START_DATE);
        }

        if (leaveReqDto.getStartDate().isBefore(LocalDate.now())) {
            throw new BusinessException(BusinessErrorCode.LEAVE_IN_PAST);
        }

        checkForOverlappingRequests(employeeEty, leaveReqDto.getStartDate(), leaveReqDto.getEndDate());


        boolean requestExists = leaveRequestRepository
                .findDuplicateRequests(
                        employeeEty,
                        leaveReqDto.getStartDate(),
                        leaveReqDto.getEndDate(),
                        leaveReqDto.getType())
                .isPresent();

        if (requestExists) {
            throw new BusinessException(BusinessErrorCode.DUPLICATE_LEAVE_REQUEST);
        }
    }

    private void checkForOverlappingRequests(EmployeeEty employeeEty, LocalDate startDate, LocalDate endDate) {
        LocalDate currentYear = LocalDate.now();
        LocalDate startOfYear = currentYear.withDayOfYear(1);
        LocalDate endOfYear = currentYear.withDayOfYear(currentYear.lengthOfYear());

        List<LeaveReqEty> existingRequests = leaveRequestRepository.findByEmployeeEtyAndStatusIn(
                employeeEty,
                List.of(LeaveRequestStatus.PENDING, LeaveRequestStatus.APPROVED),
                startOfYear,
                endOfYear
        );

        for (LeaveReqEty request : existingRequests) {

            boolean overlaps = !(endDate.isBefore(request.getStartDate()) || startDate.isAfter(request.getEndDate()));
            if (overlaps) {
                throw new BusinessException(BusinessErrorCode.LEAVE_REQUEST_OVERLAP);
            }
        }
    }

    public int calculateEffectiveDaysRequested(LeaveReqDto leaveReqDto) {

        List<LocalDate> legallyDaysOff = fetchLegallyDaysOff(leaveReqDto);

        return (int) leaveReqDto.getStartDate().datesUntil(leaveReqDto.getEndDate().plusDays(1))
                .filter(date -> {
                    DayOfWeek dayOfWeek = date.getDayOfWeek();
                    boolean isWeekend = (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
                    boolean isLegalOff = legallyDaysOff.contains(date);

                    return !(isWeekend || isLegalOff);
                })
                .count();
    }

    private List<LocalDate> fetchLegallyDaysOff(LeaveReqDto leaveReqDto) {
        LocalDate startDate = leaveReqDto.getStartDate();
        LocalDate endDate = leaveReqDto.getEndDate();


        List<String> yearMonths = startDate.datesUntil(endDate.plusDays(1))
                .map(date -> date.toString().substring(0, 7))
                .distinct()
                .collect(Collectors.toList());


        return legallyDaysOffRepository.findByPeriodIn(yearMonths)
                .stream()
                .map(LegallyDaysOffEty::getDate)
                .collect(Collectors.toList());
    }

    private long calculateTotalDaysTaken(EmployeeEty employee) {
        LocalDate currentYear = LocalDate.now();
        LocalDate startOfYear = currentYear.withDayOfYear(1);
        LocalDate endOfYear = currentYear.withDayOfYear(currentYear.lengthOfYear());

        List<LeaveReqEty> existingRequests = leaveRequestRepository.findByEmployeeEtyAndStatusIn(
                employee,
                List.of(LeaveRequestStatus.PENDING, LeaveRequestStatus.APPROVED),
                startOfYear,
                endOfYear
        );

        return existingRequests.stream()
                .mapToLong(LeaveReqEty::getNoDays)
                .sum();
    }

}

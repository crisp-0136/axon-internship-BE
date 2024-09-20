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
import ro.axon.dot.model.LeaveReqDto;
import ro.axon.dot.domain.LegallyDaysOffEty;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveReqService {
    private final EmployeeRepository employeeRepository;
    private final LeaveReqRepository leaveRequestRepository;
    private final EmpYearlyDaysOffRepository empYearlyDaysOffRepository;
    private final LegallyDaysOffRepository legallyDaysOffRepository;
    private final LeaveReqMapper leaveReqMapper;


    public void addLeaveRequest(String employeeId, LeaveReqDto leaveReqDto) {
        EmployeeEty employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.EMPLOYEE_NOT_FOUND));

        if (leaveReqDto.getStartDate().getYear() != leaveReqDto.getEndDate().getYear()) {
            throw new BusinessException(BusinessErrorCode.LEAVE_DIFFERENT_YEARS);
        }

        if (leaveReqDto.getStartDate().isBefore(LocalDate.now())) {
            throw new BusinessException(BusinessErrorCode.LEAVE_IN_PAST);
        }

        boolean requestExists = leaveRequestRepository
                .findDuplicateRequests(
                        employee,
                        leaveReqDto.getStartDate(),
                        leaveReqDto.getEndDate(),
                        leaveReqDto.getType())
                .isPresent();

        if (requestExists) {
            throw new BusinessException(BusinessErrorCode.DUPLICATE_LEAVE_REQUEST);
        }

        EmpYearlyDaysOffEty yearlyDaysOff = empYearlyDaysOffRepository
                .findByEmployeeEtyAndYear(employee, leaveReqDto.getStartDate().getYear())
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.DAYS_OFF_NOT_FOUND));

        int daysRequested = (int) ChronoUnit.DAYS.between(leaveReqDto.getStartDate(), leaveReqDto.getEndDate()) + 1;

        List<LocalDate> legallyDaysOff = legallyDaysOffRepository.findByPeriodIn(
                        List.of(leaveReqDto.getStartDate().toString().substring(0, 7)))
                .stream()
                .map(LegallyDaysOffEty::getDate)
                .toList();

        long overlappingDays = leaveReqDto.getStartDate().datesUntil(leaveReqDto.getEndDate().plusDays(1))
                .filter(legallyDaysOff::contains)
                .count();


        int effectiveLeaveDays = daysRequested - (int) overlappingDays;
        if (effectiveLeaveDays > yearlyDaysOff.getTotalNoDays()) {
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

        leaveRequestRepository.save(leaveRequest);

        yearlyDaysOff.setTotalNoDays(yearlyDaysOff.getTotalNoDays() - effectiveLeaveDays);
        empYearlyDaysOffRepository.save(yearlyDaysOff);
    }
}
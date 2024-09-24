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

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

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


        int effectiveLeaveDays = calculateEffectiveDaysRequested(leaveReqDto, employee);
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

    public int calculateEffectiveDaysRequested(LeaveReqDto leaveReqDto, EmployeeEty employee) {

        List<LocalDate> legallyDaysOff = fetchLegallyDaysOff(leaveReqDto);

        return (int) leaveReqDto.getStartDate().datesUntil(leaveReqDto.getEndDate().plusDays(1))
                .filter(date -> {
                    DayOfWeek dayOfWeek = date.getDayOfWeek();
                    boolean isWeekend = (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
                    boolean isLegalOff = legallyDaysOff.contains(date);


                    if (dayOfWeek == DayOfWeek.SUNDAY && isLegalOff || dayOfWeek == DayOfWeek.SATURDAY && isLegalOff ) {
                        return false;
                    }

                    return !(isWeekend || isLegalOff);
                })
                .count();
    }

    private List<LocalDate> fetchLegallyDaysOff(LeaveReqDto leaveReqDto) {
        String yearMonth = leaveReqDto.getStartDate().toString().substring(0, 7);
        return legallyDaysOffRepository.findByPeriodIn(List.of(yearMonth))
                .stream()
                .map(LegallyDaysOffEty::getDate)
                .toList();
    }

    private long calculateTotalDaysTaken(EmployeeEty employee) {
        List<LeaveReqEty> existingRequests = leaveRequestRepository.findByEmployeeEtyAndStatusIn(
                employee,
                List.of(LeaveRequestStatus.PENDING, LeaveRequestStatus.APPROVED)
        );

        return existingRequests.stream()
                .filter(request -> request.getStartDate().getYear() == LocalDate.now().getYear())
                .flatMap(request -> request.getStartDate().datesUntil(request.getEndDate().plusDays(1)))
                .count();
    }
}

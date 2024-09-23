package ro.axon.dot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.axon.dot.domain.EmpYearlyDaysOffEty;
import ro.axon.dot.domain.EmployeeEty;
import ro.axon.dot.domain.LeaveReqEty;
import ro.axon.dot.domain.LegallyDaysOffEty;
import ro.axon.dot.domain.enums.LeaveRequestStatus;
import ro.axon.dot.domain.repositories.EmpYearlyDaysOffRepository;
import ro.axon.dot.domain.repositories.EmployeeRepository;
import ro.axon.dot.domain.repositories.LeaveReqRepository;
import ro.axon.dot.domain.repositories.LegallyDaysOffRepository;
import ro.axon.dot.exception.BusinessErrorCode;
import ro.axon.dot.exception.BusinessException;
import ro.axon.dot.model.UpdateLeaveReqDTO;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LeaveReqService {

    private final LeaveReqRepository leaveReqRepository;
    private final EmployeeRepository employeeRepository;
    private final LegallyDaysOffRepository legallyDaysOffRepository;
    private final EmpYearlyDaysOffRepository empYearlyDaysOffRepository;

    @Transactional
    public void deleteLeaveRequest(String employeeId, Long leaveReqId) {

        LeaveReqEty leaveReqEty = getLeaveRequest(employeeId, leaveReqId);

        leaveReqRepository.delete(leaveReqEty);
    }

    @Transactional
    public void updateLeaveRequest(String employeeId, Long leaveReqId, UpdateLeaveReqDTO updateLeaveReqDTO){

        validateLeaveReqPeriod(updateLeaveReqDTO.getStartDate(), updateLeaveReqDTO.getEndDate());

        LeaveReqEty leaveReqEty = getLeaveRequest(employeeId, leaveReqId);

        if(updateLeaveReqDTO.getV() < leaveReqEty.getV()){
            throw new BusinessException(BusinessErrorCode.INVALID_LEAVE_REQUEST_V);
        }

        int workingDays = getNoOfWorkingDays(updateLeaveReqDTO.getStartDate(), updateLeaveReqDTO.getEndDate());

        if(!hasSufficientYearlyDaysOff(employeeId, workingDays, LocalDate.now().getYear())){
            throw new BusinessException(BusinessErrorCode.INSUFFICIENT_YEARLY_DAYS_OFF);
        }

        leaveReqEty.setNoDays(workingDays);

        if(leaveReqEty.getStatus().equals(LeaveRequestStatus.APPROVED)){
            leaveReqEty.setStatus(LeaveRequestStatus.PENDING);
        }

        leaveReqEty.setStartDate(updateLeaveReqDTO.getStartDate());
        leaveReqEty.setEndDate(updateLeaveReqDTO.getEndDate());
        leaveReqEty.setType(updateLeaveReqDTO.getType());
        leaveReqEty.setDescription(updateLeaveReqDTO.getDescription());

        leaveReqEty.setMdfUsr("initial.load");
        leaveReqEty.setMdfTms(Instant.now());

        leaveReqRepository.save(leaveReqEty);
    }

    private LeaveReqEty getLeaveRequest(String employeeId, Long leaveReqId) {

        LeaveReqEty leaveReqEty = leaveReqRepository.findById(leaveReqId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.LEAVE_REQUEST_NOT_FOUND));

        if(!employeeRepository.existsById(employeeId)) {
            throw new BusinessException(BusinessErrorCode.EMPLOYEE_NOT_FOUND);
        }

        if(!employeeRepository.existsById(leaveReqEty.getEmployeeEty().getEmployeeId())) {
            throw new BusinessException(BusinessErrorCode.COMBINATION_NOT_FOUND);
        }

        if(leaveReqEty.getStatus().equals(LeaveRequestStatus.REJECTED)){
            throw new BusinessException(BusinessErrorCode.LEAVE_REQUEST_REJECTED);
        }

        if(leaveReqEty.getStatus().equals(LeaveRequestStatus.APPROVED)){

            LocalDate currentDate = LocalDate.now();
            LocalDate leaveReqStartDate = leaveReqEty.getStartDate();

            if(leaveReqStartDate.getYear() < currentDate.getYear() || (
                    leaveReqStartDate.getYear() == currentDate.getYear() &&
                            leaveReqStartDate.getMonthValue() < currentDate.getMonthValue())) {

                throw new BusinessException(BusinessErrorCode.LEAVE_REQUEST_APPROVED_IN_PAST);
            }
        }

        return leaveReqEty;
    }

    private int getNoOfWorkingDays(LocalDate startDate, LocalDate endDate) {

        if(startDate.isAfter(endDate)) {
            throw new BusinessException(BusinessErrorCode.INVALID_DATE_RANGE);
        }

        int workingDays = 0;

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (!(isWeekendDay(date) || isLegallyDayOff(date)) ) {
                workingDays++;
            }
        }

        return workingDays;
    }

    private boolean isWeekendDay(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    private boolean isLegallyDayOff(LocalDate date) {
        Set<LocalDate> dates = legallyDaysOffRepository.findAll()
                .stream().map(LegallyDaysOffEty::getDate).collect(Collectors.toSet());

        return dates.contains(date);
    }

    private boolean hasSufficientYearlyDaysOff(String employeeId, int noOfDays, int year){
        EmployeeEty employeeEty = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.EMPLOYEE_NOT_FOUND));

        Optional<EmpYearlyDaysOffEty> empYearlyDaysOffEty = employeeEty.getEmpYearlyDaysOffEties()
                .stream().filter(obj -> obj.getYear() == year).findFirst();

        return empYearlyDaysOffEty.isPresent() &&
                empYearlyDaysOffEty.get().getTotalNoDays() >= noOfDays;
    }

    private void validateLeaveReqPeriod(LocalDate startDate, LocalDate endDate){
        if(startDate.isAfter(endDate)){
            throw new BusinessException(BusinessErrorCode.INVALID_DATE_RANGE);
        }

        if(startDate.getYear() != endDate.getYear()){
            throw new BusinessException(BusinessErrorCode.LEAVE_REQ_PERIOD_NOT_IN_SAME_YEAR);
        }

        if(startDate.isBefore(LocalDate.of(
                LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1))) {
            throw new BusinessException(BusinessErrorCode.LEAVE_REQUEST_PERIOD_IN_PAST);
        }
    }
}


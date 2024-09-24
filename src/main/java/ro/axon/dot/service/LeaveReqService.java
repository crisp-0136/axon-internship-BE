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
import ro.axon.dot.model.UpdateLeaveReqDTO;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveReqService {
    private final EmployeeRepository employeeRepository;
    private final LeaveReqRepository leaveRequestRepository;
    private final LegallyDaysOffRepository legallyDaysOffRepository;
    private final LeaveReqMapper leaveReqMapper;

    @Transactional
    public void addLeaveRequest(String employeeId, LeaveReqDto leaveReqDto) {
        EmployeeEty employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.EMPLOYEE_NOT_FOUND));

        validateLeaveRequest(employee, leaveReqDto);

        int noOfWorkingDays = getNoOfWorkingDays(leaveReqDto.getStartDate(), leaveReqDto.getEndDate());

        if (!hasSufficientYearlyDaysOff(employeeId, noOfWorkingDays, leaveReqDto.getStartDate().getYear())) {
            throw new BusinessException(BusinessErrorCode.INSUFFICIENT_DAYS_OFF);
        }

        LeaveReqEty leaveRequest = leaveReqMapper.toEntity(leaveReqDto);

        leaveRequest.setEmployeeEty(employee);
        leaveRequest.setStatus(LeaveRequestStatus.PENDING);
        leaveRequest.setCrtUsr(employee.getUsername());
        leaveRequest.setCrtTms(Instant.now());
        leaveRequest.setMdfUsr(employee.getUsername());
        leaveRequest.setMdfTms(Instant.now());
        leaveRequest.setNoDays(noOfWorkingDays);

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

        validateLeaveReqPeriod(leaveReqDto.getStartDate(), leaveReqDto.getEndDate());

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

    @Transactional
    public void deleteLeaveRequest(String employeeId, Long leaveReqId) {

        LeaveReqEty leaveReqEty = getLeaveRequest(employeeId, leaveReqId);

        leaveRequestRepository.delete(leaveReqEty);
    }

    @Transactional
    public void updateLeaveRequest(String employeeId, Long leaveReqId, UpdateLeaveReqDTO updateLeaveReqDTO){

        validateLeaveReqPeriod(updateLeaveReqDTO.getStartDate(), updateLeaveReqDTO.getEndDate());

        LeaveReqEty leaveReqEty = getLeaveRequest(employeeId, leaveReqId);

        if(updateLeaveReqDTO.getV() < leaveReqEty.getV()){
            throw new BusinessException(BusinessErrorCode.INVALID_LEAVE_REQUEST_V);
        }

        checkForOverlappingRequests(leaveReqEty.getEmployeeEty(), updateLeaveReqDTO.getStartDate(), updateLeaveReqDTO.getEndDate());

        int workingDays = getNoOfWorkingDays(updateLeaveReqDTO.getStartDate(), updateLeaveReqDTO.getEndDate());

        if(!hasSufficientYearlyDaysOff(employeeId, workingDays, updateLeaveReqDTO.getStartDate().getYear(), leaveReqId)){
            throw new BusinessException(BusinessErrorCode.INSUFFICIENT_DAYS_OFF);
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

        leaveRequestRepository.save(leaveReqEty);
    }

    private LeaveReqEty getLeaveRequest(String employeeId, Long leaveReqId) {

        LeaveReqEty leaveReqEty = leaveRequestRepository.findById(leaveReqId)
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
            throw new BusinessException(BusinessErrorCode.END_DATE_BEFORE_START_DATE);
        }

        int workingDays = 0;
        Set<LocalDate> legallyDaysOff = legallyDaysOffRepository.findAll()
                .stream().map(LegallyDaysOffEty::getDate).collect(Collectors.toSet());

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (!(isWeekendDay(date) || legallyDaysOff.contains(date)) ) {
                workingDays++;
            }
        }

        return workingDays;
    }

    private boolean isWeekendDay(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    private boolean hasSufficientYearlyDaysOff(String employeeId, int noOfDays, int year, Long ... ignoreLeaveReqIds){
        EmployeeEty employeeEty = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new BusinessException(BusinessErrorCode.EMPLOYEE_NOT_FOUND));

        List<LeaveReqEty>  leaveReqEtyList = employeeEty.getLeaveRequestEties().stream()
                .filter(obj -> obj.getStatus() != LeaveRequestStatus.REJECTED &&
                        obj.getStartDate().getYear() == year).toList();

        List<Long> ignoreLeaveReqIdsList = Arrays.stream(ignoreLeaveReqIds).toList();

        int noOfDaysFromOtherLeaveReqs = 0;

        for (LeaveReqEty leaveReqEty : leaveReqEtyList) {
            if(!ignoreLeaveReqIdsList.contains(leaveReqEty.getId())){
                noOfDaysFromOtherLeaveReqs += leaveReqEty.getNoDays();
            }
        }

        Optional<EmpYearlyDaysOffEty> empYearlyDaysOffEty = employeeEty.getEmpYearlyDaysOffEties()
                .stream().filter(obj -> obj.getYear() == year).findFirst();

        return empYearlyDaysOffEty.isPresent() &&
                empYearlyDaysOffEty.get().getTotalNoDays() >= noOfDays + noOfDaysFromOtherLeaveReqs;
    }

    private void validateLeaveReqPeriod(LocalDate startDate, LocalDate endDate){
        if(startDate.isAfter(endDate)){
            throw new BusinessException(BusinessErrorCode.END_DATE_BEFORE_START_DATE);
        }

        if(startDate.getYear() != endDate.getYear()){
            throw new BusinessException(BusinessErrorCode.LEAVE_REQ_PERIOD_NOT_IN_SAME_YEAR);
        }

        if(startDate.isBefore(LocalDate.now())) {
            throw new BusinessException(BusinessErrorCode.LEAVE_REQUEST_PERIOD_IN_PAST);
        }
    }
}

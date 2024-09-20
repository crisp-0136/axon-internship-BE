package ro.axon.dot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.axon.dot.domain.EmployeeEty;
import ro.axon.dot.domain.LeaveReqEty;
import ro.axon.dot.domain.enums.LeaveRequestStatus;
import ro.axon.dot.domain.enums.Status;
import ro.axon.dot.domain.repositories.EmployeeRepository;
import ro.axon.dot.domain.repositories.LeaveReqRepository;
import ro.axon.dot.exception.BusinessErrorCode;
import ro.axon.dot.exception.BusinessException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
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
            throw new BusinessException(BusinessErrorCode.EMPLOYEE_INACTIVATION_FAILURE);
        }
    }

    @Transactional
    public void deleteLeaveRequest(String userId, Long leaveReqId) {
        Optional<EmployeeEty> optionalEmployeeEty = employeeRepository.findById(userId);
        Optional<LeaveReqEty> optionalLeaveReqEty = leaveReqRepository.findById(leaveReqId);

        if(optionalEmployeeEty.isEmpty() || optionalLeaveReqEty.isEmpty()) {
            throw new BusinessException(BusinessErrorCode.LEAVE_REQUEST_DELETION_FAILURE);
        }

        EmployeeEty employeeEty = optionalEmployeeEty.get();
        LeaveReqEty leaveReqEty = optionalLeaveReqEty.get();

        if(!leaveReqEty.getEmployeeEty().getEmployeeId().equals(employeeEty.getEmployeeId())) {
            throw new BusinessException(BusinessErrorCode.LEAVE_REQUEST_DELETION_FAILURE);
        }

        if(leaveReqEty.getStatus().equals(LeaveRequestStatus.REJECTED)){
            throw new BusinessException(BusinessErrorCode.LEAVE_REQUEST_DELETION_FAILURE);
        }

        if(leaveReqEty.getStatus().equals(LeaveRequestStatus.APPROVED)){

            LocalDate currentDate = LocalDate.now();
            LocalDate leaveReqStartDate = leaveReqEty.getStartDate();

            if(leaveReqStartDate.getYear() < currentDate.getYear() || (
                    leaveReqStartDate.getYear() == currentDate.getYear() &&
                            leaveReqStartDate.getMonthValue() < currentDate.getMonthValue())) {

                throw new BusinessException(BusinessErrorCode.LEAVE_REQUEST_DELETION_FAILURE);
            }
        }

        leaveReqRepository.deleteById(leaveReqId);
    }
}

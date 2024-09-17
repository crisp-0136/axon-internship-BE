package ro.axon.dot.model;

import lombok.Data;
import ro.axon.dot.domain.EmployeeEty;
import ro.axon.dot.domain.Enums.LeaveRequestStatus;
import ro.axon.dot.domain.Enums.LeaveRequestType;

import java.time.Instant;
import java.util.Date;

@Data
public class LeaveReqItem {

    private Long id;
    //TODO: Change it to Employee type
    private String employeeId;
    private String crtUsr;
    private Instant crtTms;
    private String mdfUsr;
    private Instant mdfTms;
    private Date startDate;
    private Date endDate;
    private int noDays;
    private LeaveRequestType type;
    private LeaveRequestStatus status;
    private String description;
    private String rejectReason;
}

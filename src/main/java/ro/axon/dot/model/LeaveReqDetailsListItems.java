package ro.axon.dot.model;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class LeaveReqDetailsListItems {
    private Long id;
    private String crtUsr;
    private Instant crtTms;
    private String mdfUsr;
    private Instant mdfTms;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String type;
    private String description;
    private int noOfDays;
    private EmployeeDetailsDto employeeDetails;
    private int v;

}
